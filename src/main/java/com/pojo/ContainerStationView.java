package com.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("containerInfo_stationInfo")
@Data
public class ContainerStationView {
    @TableField(value = "stationName")
    private String stationName;
    @TableField(value = "imageTag")
    private String imageTag;
    @TableField(value = "provinceName")
    private String provinceName;
    @TableField(value = "detailedAddress")
    private String detailedAddress;
    @TableField(value = "stationIp")

    //ip 站点状态 通信状态 备注
    private String stationIp;
    //状态
    @TableField(value = "status")
    private String status;
    //镜像名
    @TableField(value = "imageName")
    private String imageName;
    //说明
    @TableField(value = "explains")
    private String explains;
    //授权状态
    @TableField(value = "authorizationStatus")
    private String authorizationStatus;
}
