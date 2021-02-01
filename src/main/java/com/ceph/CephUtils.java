package com.ceph;

/**
 * Description:
 * date: 2021/1/22 14:44
 *
 * @author pengxu
 * @since JDK 1.8
 */

import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import com.vo.Result;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.List;

/**
 * s3 接口 连接ceph工具类
 * px
 */
public class CephUtils {

    private static Logger log = LoggerFactory.getLogger(CephUtils.class);

    public void CephUtils() {
    }

    /**
     * 【你的 access_key】
     */
    private static final String AWS_ACCESS_KEY = " ";
    /**
     * 【你的 aws_secret_key】
     */
    private static final String AWS_SECRET_KEY = "5q8hfDupnvA3KS1icyq8XMRqYfbXARCUkOO7bK0X";
    /**
     * 【你的 endpoint】
     */
    private static final String ENDPOINT = "http://192.168.1.37:7480";
    private static AmazonS3 conn;

    /**
     * //     * 静态块：初始化S3的连接对象AmazonS3！ 需要参数：AWS_ACCESS_KEY，AWS_SECRET_KEY
     * rgw 网关 ip   和rgw网关 ceph 的access key  secret key
     * <p>
     * //
     */
    public static void connectCpeh(String accessKey, String secretKey, String ip) {
        //radosgw 用户
        AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        ClientConfiguration clientConfig = new ClientConfiguration();
        clientConfig.setProtocol(Protocol.HTTP);
        conn = new AmazonS3Client(awsCredentials, clientConfig);
        //ceph rgw ip
        conn.setEndpoint(ip);

    }

    /**
     * 上传文件字节流到ceph
     *
     * @param bucketName
     * @param fileName
     * @param contents
     */
    public static Result uploadByte(String bucketName, String fileName, byte[] contents) {
        try (ByteArrayInputStream input = new ByteArrayInputStream(contents)) {
            PutObjectResult putObjectResult = conn.putObject(bucketName, fileName, input, new ObjectMetadata());
//            MultipartUpload upload = new MultipartUpload();
            return Result.ok();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new IllegalStateException("文件上传到ceph服务报错!", e);
        }
    }

    public static Object getObjectUrl(String bucketName, String fileName) {
        try {
            S3Object object = conn.getObject(new GetObjectRequest(bucketName, fileName));
        } catch (AmazonServiceException e) {
            return null;
        }
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, fileName);
        //设置到期时间   这里设置 24  小时
        Date date = new Date("86,400");
        request.setExpiration(date);
        URL url = conn.generatePresignedUrl(request);
        return url;

    }

    /**
     * 从ceph系统上下载流对象
     *
     * @param bucketName
     * @param fileName
     */
    public static InputStream readStreamObject(String bucketName, String fileName) {
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, fileName);
        S3Object object = null;
        try {
            object = conn.getObject(getObjectRequest);
        } catch (AmazonServiceException e) {
            return null;
        }
        return object.getObjectContent();
    }

    /**
     * 创建桶
     *
     * @param bucketName
     * @return
     */
    public static String createBucket(String bucketName) {
        Bucket bucket = conn.createBucket(bucketName);
        return bucketName;
    }

    /**
     * 判断桶是否存在
     *
     * @param bucketName
     * @return
     */
    public static boolean bucketIsExist(String bucketName) {
        List<Bucket> buckets = conn.listBuckets();
        for (Bucket bucket : buckets) {
            String name = bucket.getName();
            if (name.equals(bucketName)) {
                return true;
            }
        }
        return false;
    }

    public static void mulutiUpload(String bucketName, String objectKey, MultipartFile file) {
        TransferManager tm = new TransferManager(conn);
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Upload upload = tm.upload(
                bucketName, objectKey, inputStream, new ObjectMetadata());
        try {
            upload.waitForCompletion();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
