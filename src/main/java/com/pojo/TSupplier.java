package com.pojo;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_supplier")
@ApiModel(value = "供应商表基本信息", description = "组织管理")
public class TSupplier {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "供应商id")
    @TableId(value = "gid", type = IdType.ASSIGN_UUID)
    private String gid;

    @ApiModelProperty(value = "供应商名称")
    private String name;

    @ApiModelProperty(value = "客户名称")
    private String customName;

    @ApiModelProperty(value = "客户手机号")
    private String customPhone;

    @ApiModelProperty(value = "邮箱")
    private String customEmail;

    @ApiModelProperty(value = "状态，1在状态，0不在")
    private String state;

    @ApiModelProperty(value = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyy-MM-dd")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyy-MM-dd")
    private Date updateTime;

    @ApiModelProperty(value = "供应商类型")
    private String type;


    @ApiModelProperty(value = "分组ID")
    private String group_id;

    @ApiModelProperty(value = "文件夹ID")
    private String fid;

    @ApiModelProperty(value = "文件名称")
    private String folordName;

    @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
    @TableField(fill = FieldFill.INSERT, value = "is_delete")
//    @TableLogic(value = "0",delval = "1")
    @TableLogic
    private Integer isDelete;

    @ApiModelProperty(value = "一对多 不对应数据库的字段")
    @TableField(exist = false)
    private List<TFileInfo> tFileInfo;


    @ApiModelProperty(value = "获取前端传的角色id",required = true)
    @TableField(exist = false)
    private String roleId;


}
