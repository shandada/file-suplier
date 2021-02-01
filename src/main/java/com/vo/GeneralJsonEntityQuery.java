package com.vo;

import com.pojo.query.ConditionList;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Create on 2020/11/12 10:29
 * <p>
 * 本类对应《通用HTTP接口设计》文档中 【一、实体CRUD接口设计-4.查询操作】的 参数格式。
 *
 * @author Yang Shuolin
 */
@Data
@NoArgsConstructor
@ApiModel(value = "通用JSON实体查询对象", description = "详情见《通用HTTP接口设计》文档")
public class GeneralJsonEntityQuery {

  @ApiModelProperty(value = "当前页码")
  private Integer current;

  @ApiModelProperty(value = "每页条目数")
  private Integer pageSize;

  @ApiModelProperty(value = "条件对象数组（条件对象之间关系为“或”）")
  private List<ConditionList> conditions;
}
