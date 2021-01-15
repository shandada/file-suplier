package com.controller;

import com.excepetion.ServiceException;
import com.pojo.TFileInfo;
import com.pojo.TSupplier;
import com.service.FileImpl;
import com.service.SupplierFileService;
import com.service.TFileInfoService;
import com.util.FileSizeUtil;
import com.util.FileUtil;
import com.util.FileUtil2;
import com.vo.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;


/**
 * @Description: 供应商文件上传下载
 * @Auther: Shan PengKun
 * @Date: 2021/1/7 10:03
 */

@RestController
@RequestMapping("/supperFile")
public class UploadFileController {
    //注入供应商service
    @Resource
    private SupplierFileService supplierFileService;
    //注入文件信息
    @Resource
    private TFileInfoService tFileInfoService;
    @Resource
    private FileImpl file;

    /**
     * 客户端,ip,秘钥
     */
    //返回数据类型
//    Data data = new Data();
//    //返回数据信息
//    Result result = new Result();

    /**
     * 文件上传+ceph   文件信息存入数据库
     *
     * @param file
     * @param gid
     * @return
     */
    @PostMapping(value = "/uploadFile/{id}", headers = "content-type=multipart/form-data")
    public Result upload(
            @RequestParam("file") MultipartFile file, @PathVariable("id") String gid) {
        try {
            //文件大小
            FileSizeUtil sizeUtil = new FileSizeUtil();
            long size = file.getSize();
            String sizes = sizeUtil.getPrintSize(size);
            System.out.println("文件大小sizes: " + sizes);
            //文件全名
            String filenameAll = file.getOriginalFilename();
            // gbk转utf-8，需要确定原编码格式，不知道的话就试一下
            byte[] s = filenameAll.getBytes("utf-8");
            String filename = new String(s, "UTF-8");

            //切割
            String[] split = filename.split("\\.");
            //文件上传名
            String UploadFileName = split[0];
            //后缀名
            String suffix = split[1];
            System.out.println(" filename 文件名: " + filename);
            //接受供应商gid 获取供应商信息
            TSupplier supplier1 = supplierFileService.getById(gid);
            System.out.println(supplier1 + "supplier1");
            //获取id
            String gid1 = supplier1.getGid();
            //获取供应商name
            String name = supplier1.getName();
            //供应商类型
            String type = supplier1.getType();

            //版本号 暂定当前时间
            SimpleDateFormat df = new SimpleDateFormat("HH.mm.ss");//设置日期格式
            //版本号
            String version = df.format(new Date());
            //传给ceph的信息   供应商id+文件名.后缀名+版本号
            String file_key = gid1 + "-" + filename + "-" + version;
            System.out.println("写入ceph的 file_key :  " + file_key);
            //向数据库文件表 添加上传文件信息
            TFileInfo tFileInfo = new TFileInfo(
                    //随机文件id
                    UUID.randomUUID().toString().replaceAll("-", ""),
                    //文件名   //版本号 供应商id 名称          描述       创建时间       更新时间         类型   大小  ceph_key    标签          //逻辑删除,0 显示
                    filename, version, gid, name, null, null, null, type, sizes, file_key, "tag", 0);

            tFileInfo.setUid(UUID.randomUUID().toString().replaceAll("-", ""));
            //添加上传文件信息到数据库
            tFileInfoService.save(tFileInfo);
            //上传到ceph
            supplierFileService.upload(file, gid, file_key);
            System.out.println(file_key + "文件上传成功！");
            //返回数据信息
            Result result = new Result();
            //成功,返回数据
            result.ok();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            //返回数据信息
            Result result = new Result();
            //失败,返回错误信息
            result.error();
            return result;
        }
    }

    /**
     * 文件下载 +ceph
     * 接收文件id
     *
     * @param
     * @throws Exception
     */
    @GetMapping(value = "/downFile/{id}")
    public Result download(@PathVariable("id") String uid) throws IOException {
        System.out.println("下载的文件id: " + uid);
        try {
            tFileInfoService.download(uid);
            Result result = new Result();
            result.ok();
            return result;
        } catch (IOException e) {
            Result result = new Result();
            result.error();
            return result;
        }
    }

    @GetMapping("/list")
    public List<TFileInfo> id() {
        return file.all();
    }
}
