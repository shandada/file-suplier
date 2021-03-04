package com.controller;

import com.ceph.CephUtils;
import com.pojo.FileInfo;
import com.pojo.IpAddress;
import com.service.FileInfoService;
import com.service.IpAddressService;
import com.service.SupplierFileService;
import com.util.CephPropertiesUtil;
import com.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;


/**
 * @author Administrator
 * @author pengkun shan
 * @Description: 文件上传下载+CePh
 */

@RestController
@Api(tags = "文件上传下载+ceph")
@RequestMapping("/supperFile")
public class UploadFileController {

    /**
     * 注入供应商service
     */
    @Resource
    private SupplierFileService supplierFileService;

    /**
     * 注入文件service
     */
    @Resource
    private FileInfoService fileInfoService;

    /**
     * 远程调用
     */
    @Resource
    private RestTemplate restTemplate;

    /**
     * 连接ceph service
     */
    @Resource
    private IpAddressService ipAddressService;


    /**
     * 2.0 文件上传+ceph   文件信息存入数据库
     *
     * @param file
     * @param gid
     * @return
     */
    @ApiOperation(value = "文件上传+ceph  ", notes = "应用本体")
    @PostMapping(value = "/uploadFile/{id}", headers = "content-type=multipart/form-data")
    public Result upload(
            @ApiParam(value = "上传文件", required = true) @RequestParam("file") MultipartFile file,
            @ApiParam(value = "组织id", required = true) @PathVariable("id") String gid) {
        try {
            /**
             获取ceph连接信息
             */
//            IpAddress byId = ipAddressService.getIpAddress();
//            System.out.println("连接ceph信息 = " + byId);

            FileInfo fileInfo = supplierFileService.addFileINfo(file, gid);
            String fileKey = fileInfo.getFileKey();
            /**
             * 上传文件名称不能重复
             */
            FileInfo reuseName = fileInfoService.ReuseName(fileInfo.getFileName());
            if (reuseName != null) {
                System.out.println("上传失败,文件已存在!");
                return Result.exist().message("上传失败,文件已存在!");
            }
            /**
             * 文件上传到CEph
             */
            supplierFileService.upload(CephPropertiesUtil.BucketName, fileKey, file);

            /**
             * 连接ceph查询文件是否上传成功
             */
            CephUtils.connectCpeh(CephPropertiesUtil.AACCESSKEY, CephPropertiesUtil.SECRETKEY, CephPropertiesUtil.CEPH_IP);
            InputStream inputStream = CephUtils.readStreamObject(CephPropertiesUtil.BucketName, fileKey);
            System.out.println("inputStream = " + inputStream);

            /**
             * 如果ceph中无该文件 返回上传失败
             */
            if (inputStream == null) {
                System.out.println("文件上传失败");
                return Result.error().code(304).message("文件上传失败!");
            }

            /**
             * 上传成功,信息添加数据库
             */
            fileInfoService.save(fileInfo);

            /**
             * 上传成功 返回 uid
             */
            String uid = fileInfo.getUid();
            System.out.println("文件上传成功 返回的uid = " + uid);
            return Result.okData(uid);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error();
        }
    }

    /**
     * 2.0
     * 文件下载 +ceph
     * 接收文件id
     *
     * @param
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "文件下载 +ceph ", notes = "应用本体")
    @GetMapping(value = "/downFile/{id}")
    public Result download(@ApiParam(value = "文件id", required = true) @PathVariable("id") String uid, HttpServletResponse response) throws IOException {
        try {
            /**
             * 获取ceph连接信息
             */
//            IpAddress byId = ipAddressService.getIpAddress();
//            System.out.println("连接ceph信息 = " + byId);

            System.out.println("下载文件的id ==== " + uid);
            FileInfo fileInfo = fileInfoService.getById(uid);
            String fileKey = fileInfo.getFileKey();
            String fileName = fileInfo.getFileName();
            /**
             * 连接ceph
             */
            CephUtils.connectCpeh(CephPropertiesUtil.AACCESSKEY, CephPropertiesUtil.SECRETKEY, CephPropertiesUtil.CEPH_IP);
            InputStream inputStream = CephUtils.readStreamObject(CephPropertiesUtil.BucketName, fileKey);
            System.out.println("inputStream = " + inputStream);
            /**
             * 判断ceph中是否有该文件
             */
            if (inputStream == null) {
                System.out.println("======ceph文件不存在=====");
                return Result.exist().message("文件不存在");
            }
            /**
             * 文件存在 下载
             */
            fileInfoService.download(fileName, fileKey, response, inputStream);
            return Result.ok();
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error();
        }
    }
}
