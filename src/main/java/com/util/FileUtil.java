package com.util;


import com.ceph.CephClient;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @author Administrator
 */ // 文件下载工具类  加ceph
public class FileUtil {
    public  void download(String fileName, HttpServletResponse response, String fileKey) throws IOException {
        // 发送给客户端的数据
        OutputStream outputStream = response.getOutputStream();
        ByteArrayInputStream bis = null;
        // 读取filename   "a.docx"/a.docx
        System.out.println("FileUtil下载的文件名: " + fileName);
        System.out.println("拼接的文件名: " + fileKey);

        // TODO   连接ceph 下载文件  // void writeFile(String fileName, byte[] file);
        CephClient cephClient = new CephClient();
        cephClient.getConnect("admin", CephPropertiesUtil.CEPH_IP,CephPropertiesUtil.KEY);
//        MyCeph myCeph = new CephUtils(UUID.randomUUID().toString().substring(0,5),CephPropertiesUtil.CEPH_IP,CephPropertiesUtil.KEY);
        //拼接 文件 key
        //  下载的字节 bytes
        byte[] bytes = cephClient.readFile(fileKey);
        System.out.println("ceph拿到的bytes: " + bytes);
        //字节转为文件
        bis = new ByteArrayInputStream(bytes);
        int i = bis.read(bytes);
        while (i != -1) {
            outputStream.write(bytes, 0, bytes.length);
            outputStream.flush();
            i = bis.read(bytes);
        }
        System.out.println("文件下载成功！");
    }
}



