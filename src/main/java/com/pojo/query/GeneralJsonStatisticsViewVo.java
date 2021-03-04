package com.pojo.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Create on 2020/11/19 18:21
 *
 * @author Yang Shuolin
 */
@Data
@NoArgsConstructor
@ApiModel(value = "通用JSON实体统计结果对象", description = "详情见《通用HTTP接口设计》文档")
public class GeneralJsonStatisticsViewVo {

    @ApiModelProperty(value = "统计结果")
    private Map<String, Object> result;
}
