package com.pojo;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * @author pengkun shan
 * @Description:
 * @date 2021/1/19 10:32
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "container_config")
public class ContainerConfig {

    private static final long serialVersionUID = 1L;
    @GeneratedValue(generator = "JDBC")
    @TableId(value = "uid", type = IdType.ASSIGN_UUID)
    private String uid;

    @TableField("supplierName")
    private String supplierName;

    /**
     * 供应商id
     */
    @TableField("supplierId")
    private String supplierId;

    /**
     *
     *
     * 端口组80=80,8081=8081
     */
    private String ports;

    /**
     * 卷列表
     */
    @TableField("volumes")
    private String volumes;

    /**
     * 最大cpu
     */
    @TableField("cpuMAX")
    private String cpuMAX;

    /**
     * 最大gpu
     */
    @TableField("memoryMAX")
    private String memoryMAX;

    /**
     * 最大硬盘
     */
    @TableField("hardDiskMAX")
    private String hardDiskMAX;

    /**
     * 暴露端口
     */
    @TableField("exports")
    private String exports;

    /**
     * 镜像名称
     */
    @TableField("imageName")
    private String imageName;

    /**
     * 创建时间
     */
    @TableField("createTime")
    private Date createTime;

    /**
     * 文件uuid
     */
    @TableField("fileId")
    private String fileId;

    /**
     * 环境变量
     */
    @TableField(value = "env")
    private String env;

    /**
     * 算法类型
     */
    private String type;

    /**
     * 镜像版本
     */
    @TableField("imageTag")
    private String imageTag;

    /**
     * 分支
     */
    @TableField("branches")
    private String branches;

    @TableField(exist = false)
    private TFileInfo tFileInfo;
}