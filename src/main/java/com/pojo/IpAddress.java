package com.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;


/**
 * @Description:
 * @author pengkun Shan
 *
 * @date 2021/3/2 9:55
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class IpAddress implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
    * ai服务ip地址+端口号
    */
    private String aiAddress;

    /**
    * ceph ip地址
    */
    private String cephAddress;

    /**
    * 功能模块名称
    */
    private String modular;

    /**
    * ceph accesskey
    */
    private String cephAccessKey;

    /**
    * ceph secretkey
    */
    private String cephSecretKey;

    /**
    * ceph 桶名称
    */
    private String bucketName;
}