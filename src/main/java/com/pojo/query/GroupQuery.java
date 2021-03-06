package com.pojo.query;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description:
 * @Auther: logo丶西亚卡姆
 * @Date: 2021/1/6 17:00
 */
@Data
@ApiModel(value = "组织-封装对象", description = "")

public class GroupQuery {

    @ApiModelProperty(value = "文件id")
    private String uuid;

    @ApiModelProperty(value = "文件名称")
    private String groupName;

    @ApiModelProperty(value = "角色id")
    private String roleId;

    @ApiModelProperty(value = "描述")
    private String overview;

    @ApiModelProperty(value = "创建时间")
    @TableField(value = "createTime", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyy-MM-dd")
    private Date createTime;
}
