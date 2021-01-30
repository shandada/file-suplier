package com.ceph;

import com.ceph.rados.IoCTX;
import com.ceph.rados.Rados;
import com.ceph.rados.exceptions.RadosException;
import com.ceph.rbd.Rbd;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Description:
 * date: 2020/12/28 11:22
 *
 * @author pengxu
 * @since JDK 1.8
 */
public class CephClient {
    private  Rados rados;
    private  IoCTX ioctx;
    private  Rbd rbd;

    public CephClient() {
    }

    /**
     * 连接ceph集群
     * @param mon_host monitor节点ip
     * @param key 秘钥
     */

//    public CephUtils(String clusterName,String mon_host, String key) {
//        try {
//            rados = new Rados(clusterName);
//            rados.confSet("mon_host", mon_host);
//            rados.confSet("key", key);
//            rados.connect();
//            ioctx = rados.ioCtxCreate("testPool");
//            System.out.println("cpeh-connect success");
//            rbd = new Rbd(ioctx);
//        } catch (Exception e) {
//            e.printStackTrace();
//            // TODO: handle exception
//        }
//    }
    public synchronized  void getConnect(String clusterName,String mon_host, String key) {
        try {
            rados = new Rados(clusterName);
            rados.confSet("mon_host", mon_host);
            rados.confSet("key", key);
            rados.connect();
            ioctx = rados.ioCtxCreate("testPool");
            System.out.println("cpeh-connect success");
            rbd = new Rbd(ioctx);
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }
    }

    /**
     * ceph文件写入
     * @param file
     * @param fileName 文件名称
     */
    public  void writeFile(String fileName,byte[] file)  {
        if(ioctx==null){
            return;
        }
        try {
            ioctx.write(fileName,file);
            rados.ioCtxDestroy(ioctx);
        } catch (RadosException e) {
            e.printStackTrace();
        }
    }

    /**
     * 文件读取
     * @param fileName
     * @return
     */
    public  byte[] readFile(String fileName)  {
        if(ioctx==null){
            return null;
        }
        try {
            FileOutputStream out;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int len=1024;
            byte[]buf=new byte[len];
            //定义读取字节的数量
            long count=0;
            while (ioctx.read(fileName,len,count,buf)>0){
                try {
                    bos.write(buf);
                    count+=len;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            rados.ioCtxDestroy(ioctx);
            return bos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除 ceph 某个文件
     * @param fileName 文件名字
     * @return
     */
    public boolean deleteFile(String fileName){
        if(ioctx==null){
            return false;
        }
        try {
            ioctx.remove(fileName);
            return true;
        } catch (RadosException e) {
            e.printStackTrace();
            return false;
        }
    }
}
