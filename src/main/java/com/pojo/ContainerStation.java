package com.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author yangshiling
 * @since 2021-01-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("container_station")
@ApiModel(value="ContainerStation对象", description="")
public class ContainerStation extends Model<ContainerStation> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "站点镜像中间表id")
    @TableId("uid")
    private String uid;

    @ApiModelProperty(value = "镜像表id")
    @TableField("container_uid")
    private String containerUid;

    @ApiModelProperty(value = "站点id")
    @TableField("station_uid")
    private String stationUid;
    @ApiModelProperty(value = "状态")
    @TableField(value = "container_status")
    private String containerStatus;
    @ApiModelProperty(value = "创建时间")
    @TableField(value = "up_createTime")
    private Date upCreateTime;
    @Override
    protected Serializable pkVal() {
        return this.uid;
    }

}
