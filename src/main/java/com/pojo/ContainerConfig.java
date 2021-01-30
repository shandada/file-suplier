package com.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * @author pengkun shan
 * @Description:  算法库管理
 * @date 2021/1/19 10:32
 */

@Data
@EqualsAndHashCode(callSuper =  false)
@TableName("container_config")
@AllArgsConstructor
@NoArgsConstructor
public class ContainerConfig implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "uid", type = IdType.ASSIGN_UUID)
    private String uid;

    @TableField(value = "supplierName")
    private String supplierName;

    /**
     * 供应商id
     */

    @TableField(value = "supplierId")
    private String supplierId;

    /**
     *
     *
     * 端口组80=80,8081=8081
     */
    @TableField(value= "ports")
    private String ports;

    /**
     * 卷列表
     */
    @TableField(value= "volumes")
    private String volumes;

    /**
     * 最大cpu
     */
    @TableField(value= "cpuMAX")
    private String cpuMAX;

    /**
     * 最大gpu
     */
    @TableField(value = "memoryMAX")
    private String memoryMAX;

    /**
     * 最大硬盘
     */
    @TableField(value = "hardDiskMAX")
    private String hardDiskMAX;

    /**
     * 暴露端口
     */
    @TableField(value = "exports")
    private String exports;

    /**
     * 镜像名称
     */
    @TableField(value= "imageName")
    private String imageName;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @TableField(value = "createTime", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyy-MM-dd")
    private Date createTime;

    /**
     * 文件uuid
     */
    @TableField(value= "fileId")
    private String fileId;

    /**
     * 环境变量
     */
    @TableField(value= "env")
    private String env;

    /**
     * 算法类型
     */
    @TableField(value= "type")

    private String type;

    /**
     * 镜像版本
     */
    @TableField(value = "imageTag")
    private String imageTag;

    /**
     * 分支
     */
    @TableField(value = "master")
    private Boolean master;

    /**
     * 状态
     */
    @TableField(value = "status")
    private Boolean status;
    /**
     * 说明
     */
    @TableField(value = "explains")
    private String explains;

    @TableField(exist = false)
    private FileInfo fileInfo;

    @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
    @TableField(fill = FieldFill.INSERT, value = "isDelete")
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    @ApiModelProperty(value = "下发状态")
    private String containerStatus ;
    @TableField(exist = false)
    @ApiModelProperty(value = "创建时间")
    private Date upCreateTime;
    @ApiModelProperty(value = "所属站点")
    @TableField(exist = false)
    private List<Station> stationList;
}