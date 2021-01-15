package com.pojo;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_supplier")
public class TSupplier {
    /**
     * 供应商id
     */
    @GeneratedValue(generator = "JDBC")
    private static final long serialVersionUID = 1L;
    @TableId(value = "gid", type = IdType.ASSIGN_UUID)
    private String gid;
    /**
     * 供应商名称
     */
    private String name;
    //客户名称
    private String customName;
    //客户手机号
    private String customPhone;

    //邮箱
    private String customEmail;

    //状态，1在状态，0不在
    private String state;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyy-MM-dd")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyy-MM-dd")
    private Date updateTime;

    /**
     * 供应商类型
     */
    private String type;

    /**
     * 文件夹ID
     */
    private String fid;
    /**
     * 文件名称
     */
    private String folordName;

    @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
    @TableField(fill = FieldFill.INSERT, value = "is_delete")
//    @TableLogic(value = "0",delval = "1")
    @TableLogic
    private Integer isDelete;

    //不对应数据库的字段
    @TableField(exist = false)
    private List<TFileInfo> tFileInfo;

}
