package com.vo;

import com.pojo.query.ConditionList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 工具类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRequest {
    // 页码
    private Integer current;
    // 每页条数
    private Integer pageSize;
    //查询条件集合
    private List<ConditionList> conditions;
}
