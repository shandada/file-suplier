package com.pojo.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.excepetion.ServiceException;
import com.vo.GeneralJsonStatisticsViewVo;
import lombok.extern.slf4j.Slf4j;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Create on 2020/11/17 16:12
 * <p>
 * 用于处理实体统计请求的工具类，
 * 具体统计详情见文档《通用HTTP接口设计》中的【二、实体统计接口设计】
 * <p>
 *
 * <p>
 * 调用方法：
 * 1.使用实体类作为关系泛型参数实例化对象，传入相应实体的（MBP）Service层对象到构造方法。
 * 2.使用 {@link GeneralJsonStatisticsViewQuery} 作为参数传入 {@link #doStatistics(GeneralJsonStatisticsViewQuery)}
 * 方法中，得到统计结果。
 *
 * @param <EntityType> 实体类
 * @author Yang Shuolin
 */
@Slf4j
public class GeneralJsonStatisticsViewQueryProcessor<EntityType> {

    private IService<EntityType> service;
    private Class<EntityType> clazz;

    public GeneralJsonStatisticsViewQueryProcessor(IService<EntityType> service, Class<EntityType> clazz) {
        this.service = service;
        this.clazz = clazz;
    }

    public GeneralJsonStatisticsViewVo doStatistics(GeneralJsonStatisticsViewQuery query) throws ServiceException {

        // 查询目标实体
        GeneralJsonQueryWrapperBuilder<EntityType> wrapperBuilder = new GeneralJsonQueryWrapperBuilder<>(clazz);
        GeneralJsonEntityQuery newQuery = new GeneralJsonEntityQuery();
        newQuery.setConditions(query.getConditions());
        List<String> methods = Arrays.stream(clazz.getMethods()).map(Method::getName).collect(Collectors.toList());
        QueryWrapper<EntityType> queryWrapper = wrapperBuilder.build(newQuery);

        GeneralJsonStatisticsViewVo vo = new GeneralJsonStatisticsViewVo();
        HashMap<String, Object> resultMap = new HashMap<>();

        // 若无查询条件，表明对所有实体应用统计
        if (queryWrapper == null) {
            queryWrapper = new QueryWrapper<>();
        }

        // 个数统计
        if ("count".equals(query.getAction())) {
            Map<String, Object> map = service.getMap(queryWrapper.select("count(*) as total"));
            resultMap.put("count", map.get("total"));
        } else {
            // 其他统计，分开是因为个数统计不需要field
            for (String field : query.getField()) {
                // 检查field是否存在
                if (!methods.contains("get" + getUpperCaseFirstLetter(field))) {
                    throw new ServiceException("字段：" + field + "不存在");
                }
                Object result;
                // 执行统计操作
                switch (query.getAction()) {
                    case "sum":
                        // 统计某字段总和
                        try {
                            result = service
                                    .getMap(queryWrapper.select("sum(`" + getColumnNameFromFieldName(field) + "`) as target"))
                                    .get("target");
                        } catch (NullPointerException e) {
                            log.info("Statistics result equals NULL, return 0");
                            result = 0;
                        }
                        break;
                    case "average":
                        // 统计某字段平均数
                        try {
                            result = service
                                    .getMap(queryWrapper.select("avg(`" + getColumnNameFromFieldName(field) + "`) as target"))
                                    .get("target");
                        } catch (NullPointerException e) {
                            log.info("Statistics result equals NULL, return 0");
                            result = 0;
                        }
                        break;
                    case "keyedReduce":
                        // 归并某字段
                        result = new HashMap<String, Long>();
                        List<Map<String, Object>> maps = service
                                .listMaps((queryWrapper.select(getColumnNameFromFieldName(field) + " as col, count(*) as target")
                                        .groupBy(getColumnNameFromFieldName(field))));
                        for (Map<String, Object> map : maps) {
                            ((Map<String, Long>) result).put(String.valueOf(map.get("col")), (Long) map.get("target"));
                        }
                        break;
                    default:
                        result = null;
                        break;
                }

                resultMap.put(field, result);
            }
        }

        vo.setResult(resultMap);
        return vo;
    }

    private String getColumnNameFromFieldName(String fieldName) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < fieldName.length(); i++) {
            if (!Character.isUpperCase(fieldName.charAt(i))) {
                stringBuilder.append(fieldName.charAt(i));
            } else {
                stringBuilder.append("_").append(Character.toLowerCase(fieldName.charAt(i)));
            }
        }
        return stringBuilder.toString();
    }

    private String getUpperCaseFirstLetter(String fieldName) {
        return fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }
}
