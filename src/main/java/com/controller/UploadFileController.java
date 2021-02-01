package com.controller;

import com.pojo.FileInfo;
import com.service.FileInfoService;
import com.service.SupplierFileService;
import com.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * @author Administrator
 * @author pengkun shan
 * @Description: 文件上传下载+CePh
 * @Date: 2021/1/7 10:03
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
             *   添加上传的文件信息 到数据库
             */
            FileInfo fileInfo = supplierFileService.addFileINfo(file, gid);
            fileInfoService.save(fileInfo);
            String fileKey = fileInfo.getFileKey();
            String uid = fileInfo.getUid();
            /**
             * 文件上传到CEph
             */
            String bucketName = "nanjing";
            supplierFileService.upload(bucketName, fileKey, file);
            /**
             * 上传成功 返回 uid
             */
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
            fileInfoService.download(uid, response);
            return Result.ok();
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error();
        }
    }
}
