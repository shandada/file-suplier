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
    @Value("${ceph.file.admin}")
    private String admin;

    @Value("${ceph.file.ip}")
    private String ip;

    @Value("${ceph.file.key}")
    private String key;

    public static String ADMIN;
    public static String CEPH_IP;
    public static String KEY;
    @Override
    public void afterPropertiesSet() throws Exception {
        ADMIN = admin;
        CEPH_IP = ip;
        KEY = key;
    }
}
