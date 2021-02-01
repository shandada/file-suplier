package com.pojo.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Create on 2020/11/19 18:19
 *
 * @author Yang Shuolin
 */
@Data
@NoArgsConstructor
@ApiModel(value = "通用JSON实体统计查询对象", description = "详情见《通用HTTP接口设计》文档")
public class GeneralJsonStatisticsViewQuery {

    @ApiModelProperty(value = "要统计的副实体字段名称列表")
    private List<String> field;

    @ApiModelProperty(value = "要执行的统计操作")
    private String action;

    @ApiModelProperty(value = "条件对象数组（条件对象之间关系为“或”）")
    private List<ConditionList> conditions;
}
