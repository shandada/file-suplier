package com.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author yangshiling
 * @since 2021-01-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("station")
@ApiModel(value = "Station对象", description = "")
public class Station extends Model<Station> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "uuid")
    @TableId("UID")
    private String uid;

    @ApiModelProperty(value = "地区")
    @TableField("cityName")
    private String cityName;

    @ApiModelProperty(value = "地区名称")
    @TableField("stationName")
    private String stationName;

    @ApiModelProperty(value = "主机ip")
    @TableField("stationIp")
    private String stationIp;

    @ApiModelProperty(value = "具体地址")
    @TableField("detailedAddress")
    private String detailedAddress;

    @ApiModelProperty(value = "站点类型")
    @TableField("stationType")
    private String stationType;

    @ApiModelProperty(value = "地区编码")
    @TableField("stationCode")
    private String stationCode;

    @ApiModelProperty(value = "经度")
    @TableField("longitude")
    private String longitude;

    @ApiModelProperty(value = "纬度")
    @TableField("latitude")
    private String latitude;

    @ApiModelProperty(value = "上级节点id")
    @TableField("parentUid")
    private String parentUid;

    @ApiModelProperty(value = "上级节点名称")
    @TableField("parentName")
    private String parentName;

    @ApiModelProperty(value = "创建时间")
    @TableField("createTime")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField("updateTime")
    private Date updateTime;

    @ApiModelProperty(value = "状态")
    @TableField("status")
    private Boolean status;

    @ApiModelProperty(value = "系统版本")
    @TableField("osName")
    private String osName;

    @ApiModelProperty(value = "cpu使用量")
    @TableField("availableCpu")
    private String availableCpu;

    @ApiModelProperty(value = "内存使用量")
    @TableField("availablePhysicalMem")
    private String availablePhysicalMem;

    @ApiModelProperty(value = "剩余硬盘")
    @TableField("availableDisk")
    private String availableDisk;

    @ApiModelProperty(value = "总显存量")
    @TableField("totalVRam")
    private String totalVRam;

    @ApiModelProperty(value = "运行中容器数量")
    @TableField("containersRunning")
    private Integer containersRunning;

    @ApiModelProperty(value = "暂停容器数量")
    @TableField("containersPaused")
    private Integer containersPaused;

    @ApiModelProperty(value = "停止容器数量")
    @TableField("containersStopped")
    private Integer containersStopped;

    @ApiModelProperty(value = "停止容器数量")
    @TableField("provinceName")
    private String provinceName;

    @TableField("voltLevel")
    private Integer voltLevel;

    @ApiModelProperty(value = "备注")
    @TableField("remark")
    private String remark;

    @ApiModelProperty(value = "授权状态")
    @TableField("authorizationStatus")
    private Boolean authorizationStatus;

    @ApiModelProperty("拥有的镜像")
    @TableField(exist = false)
    private List<ContainerConfig> containerConfigs;

    @Override
    protected Serializable pkVal() {
        return this.uid;
    }

//    //不对应数据库字段
//    @TableField(exist = false)
//    private List<Station> children;
}
