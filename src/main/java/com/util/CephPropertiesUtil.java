package com.util;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author pengkun shan
 * @Description:
 * @date 2021/1/18 15:31
 */

/**
 * InitializingBean：Spring启动的时候加载配置文件
 * 连接ceph
 */
@Component
public class CephPropertiesUtil implements InitializingBean {

    @Value("${ceph.file.accessKey}")
    private String accessKey;

    @Value("${ceph.file.secretKey}")
    private String secretKey;

    @Value("${ceph.file.ip}")
    private String ip;

    @Value("${ceph.file.bucketName}")
    private String bucketName;

    public static String AACCESSKEY;
    public static String SECRETKEY;
    public static String CEPH_IP;
    public static String BucketName;

    @Override
    public void afterPropertiesSet() throws Exception {
        AACCESSKEY = accessKey;
        SECRETKEY = secretKey;
        CEPH_IP = ip;
        BucketName = bucketName;
    }
}
