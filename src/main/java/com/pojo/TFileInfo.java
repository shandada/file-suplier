package com.pojo;

import java.util.Date;

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


/**
 * @Description:
 * @Auther: logo丶西亚卡姆
 * @Date: 2021/1/6 16:19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_file_info")
public class TFileInfo {
    /**
    * 文件id
    */
    @Id
    @GeneratedValue(generator = "JDBC")
    private static final long serialVersionUID = 1L;
    @TableId(value = "uid", type = IdType.ASSIGN_UUID)
    private String uid;

    /**
    * 文件名称
    */
    private String fileName;

    /**
     * 版本号
     */
    private String version;

    /**
    * 供应商id
    */
    private String supplierId;

    /**
    * 供应商
    */
    private String supplierName;

    /**
    * 描述
    */
    private String remark;

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
     * 类型
     */
    private String type;
    /**
    * 大小
    */
    private String size;

    /**
    * ceph读取key
    */
    private String fileKey;

    /**
     * 标签
     */
    private String tag;

    @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
    @TableField(fill = FieldFill.INSERT,value = "is_delete")
//    @TableLogic(value = "0",delval = "1")
    @TableLogic
    private Integer isDelete;
}