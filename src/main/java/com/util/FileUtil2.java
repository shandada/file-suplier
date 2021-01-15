package com.util;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

// 文件下载工具类 测试
// test
public class FileUtil2 {
    public static void download2(String fileName,HttpServletResponse res) throws IOException {
        // 发送给客户端的数据
        OutputStream outputStream = res.getOutputStream();
        byte[] buff = new byte[1024];
        BufferedInputStream bis = null;
        // 读取filename    "a.docx"/a.docx

        //
        bis = new BufferedInputStream(new FileInputStream(new File("D:\\a.docx")));
        int i = bis.read(buff);
        while (i != -1) {
//            name 文件名, NewType .文件类型 例如: .docx  .pdf .png
//            res.setHeader("Content-Disposition", "attachment;filename=" + new String(("file").getBytes("utf-8"), "iso-8859-1"));

            outputStream.write(buff, 0, buff.length);
            outputStream.flush();
            i = bis.read(buff);
        }
        System.out.println("文件下载成功2！！");
    }
}



//        ////        //TODO  文件下载 // void writeFile(String fileName, byte[] file);
//        MyCeph myCeph = new CephUtils("admin", "192.168.1.13", "AQArI9hfvp36IRAAFhB7U6t6ltcLSfHciZiy0A==");
////        下载的字节 bytes
//        byte[] bytes = myCeph.readFile(fileKey);
//        System.out.println("ceph拿到的bytes: "+bytes);
//        outputStream.write(bytes);
//        //文件下载的本地文件路径

//
//        int i = bis.read(bytes);
//        while (i != -1) {
//            // buff 替换 bytes
//            outputStream.write(bytes, 0, bytes.length);
//            //name 文件名, NewType .文件类型 例如: .docx  .pdf .png
////            response.setHeader("Content-Disposition", "attachment;filename=" + new String(("D:\\work\\fuma\\code\\springboot-test\\fiel\\" +"a.docx").getBytes("utf-8"), "iso-8859-1"));
//            outputStream.flush();
//            i = bis.read(bytes);
//        }


//        bis = new BufferedInputStream(new FileInputStream(new File(downFileName)));
//
//
//        int i = bis.read(buff);
//        while (i != -1) {
//            // buff 替换 bytes
//            outputStream.write(buff, 0, buff.length);
//            //name 文件名, NewType .文件类型 例如: .docx  .pdf .png
////            response.setHeader("Content-Disposition", "attachment;filename=" + new String(("D:\\work\\fuma\\code\\springboot-test\\fiel\\" +"a.docx").getBytes("utf-8"), "iso-8859-1"));
//            outputStream.flush();
//            i = bis.read(buff);
//        }
