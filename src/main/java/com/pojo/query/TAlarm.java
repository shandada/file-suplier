package com.pojo.query;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@ApiModel(value = "告警信息模型-封装对象")
public class TAlarm {
    /**
     * 告警信息id
     */
    @TableId
    private String alarmId;
    /**
     * 告警号
     */
    @ApiModelProperty(value = "告警号")
    private String alarmNumber;

    /**
     * 告警状态
     */
    @ApiModelProperty(value = "告警状态")
    private String alarmState;

    /**
     * 变电站名称
     */
    @ApiModelProperty(value = "变电站名称")
    private String stationName;

    /**
     * 间隔名称
     */
    @ApiModelProperty(value = "间隔名称")
    private String bayName;

    /**
     * 设备名称
     */
    @ApiModelProperty(value = "设备名称")
    private String deviceName;

    /**
     * 点位名称
     */
    @ApiModelProperty(value = "点位名称")
    private String pointName;

    /**
     * 告警时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd yyyy-MM-dd HH-mm-ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss", timezone = "GMT")
    @ApiModelProperty(value = "告警时间")
    private Date time;
    /**
     * 缺陷数量
     */
    @ApiModelProperty(value = "缺陷数量")
    private Integer defectNumber;

    /**
     * 判别数量
     */
    @ApiModelProperty(value = "判别数量")
    private Integer distNumber;

    /**
     * 原始图像
     */
    @ApiModelProperty(value = "原始图像(用于获取未压缩图像)")
    private String picRaw;

    /**
     * 缺陷图像
     */
    @ApiModelProperty(value = "缺陷图像")
    private String picDefect;

    /**
     * 判别图像
     */
    @ApiModelProperty(value = "判别图像")
    private String picDifferent;

    /**
     * 判别基准图像
     */
    private String picDiffBase;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    /**
     * 缺陷id， 以 ， 分隔存储
     */
    private String defectIds;
    /**
     * 告警分类id
     */
    private String alarmTypeId;
    /**
     * 图片宽度
     */
    private Integer picWidth;
    /**
     * 图片高度
     */
    private Integer picHeight;
    /**
     * 告警原图缩略图
     */
    @ApiModelProperty(value = "原始图像缩略图")
    private String picRawAbbr;
    @ApiModelProperty(value = "缺陷图像缩略图")
    private String picDefectAbbr;
    @ApiModelProperty(value = "判别图像缩略图")
    private String picDiffAbbr;
    @ApiModelProperty(value = "判别基准图像缩略图")
    private String picDiffBaseAbbr;
}
