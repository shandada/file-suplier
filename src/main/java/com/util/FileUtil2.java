package com.util;


import com.ceph.MyCeph;
import com.ceph.utils.CephUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

// 文件下载工具类
public class FileUtil2 {
    public static void download2(String fileName, HttpServletResponse response, String fileKey) throws IOException {
        // 发送给客户端的数据
        OutputStream outputStream = response.getOutputStream();
        byte[] buff = new byte[1024];
        BufferedInputStream bis = null;
        // 读取filename   "a.docx"/a.docx
        System.out.println("FileUtil下载的文件名: " + fileName);
        System.out.println("拼接的文件名: " + fileKey);
        //文件下载的本地文件路径

//        bis = new BufferedInputStream(new FileInputStream(new File("")));
        bis = new BufferedInputStream(new FileInputStream(new File("D:\\a.docx")));

        int i = bis.read(buff);
        while (i != -1) {
            // buff 替换 bytes
            outputStream.write(buff, 0, buff.length);
            //name 文件名, NewType .文件类型 例如: .docx  .pdf .png
//            response.setHeader("Content-Disposition", "attachment;filename=" + new String(("D:\\work\\fuma\\code\\springboot-test\\fiel\\" +"a.docx").getBytes("utf-8"), "iso-8859-1"));
            outputStream.flush();
            i = bis.read(buff);
//        }
        }
    }
}
