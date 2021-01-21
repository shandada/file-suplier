package com.pojo.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.excepetion.ServiceException;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**   数据库字段是小写 下划线_ true  大写 false
 * Create on 2020/11/12 11:06
 * <p>
 * 用于根据《通用HTTP接口设计》文档中的【一、实体CRUD操作的接口设计】， 生成相应实体的 {@link QueryWrapper} 查询对象。
 * <p>
 * 调用方法： 1.以想要查询的实体类为泛型参数，实例化一个此类的对象（Builder对象）； 2.使用本库中已实现的Query类：{@link GeneralJsonEntityQuery}
 * 作为参数，调用Builder对象的 {@link #(GeneralJsonEntityQuery)} 方法，query参数通常是在Controller中作为参数从前端传入的；
 * 3.使用所返回的QueryWrapper对象去查询实体对象即可。
 * <p>
 * 注：代码调用示例见其他基础库中实体Controller中的【JSON通用查询】接口。
 *
 * @param <EntityType> 要查询的目标实体类
 * @author Yang Shuolin
 */
public class GeneralJsonQueryWrapperBuilder<EntityType> {

  private Class<EntityType> clazz;

  public GeneralJsonQueryWrapperBuilder(Class<EntityType> clazz) {
    this.clazz = clazz;
  }

  /**
   * 根据查询对象构建MyBatis的QueryWrapper条件对象。
   *
   * @param query 前端查询对象
   * @return QueryWrapper对象，若返回null则表示无条件需要被应用
   */


  public QueryWrapper<EntityType> build(GeneralJsonEntityQuery query, boolean isUnderline)
      throws ServiceException {
    QueryWrapper<EntityType> queryWrapper = new QueryWrapper<>();
    List<ConditionList> conditionsOr = query.getConditions();
    List<String> methods = Arrays.stream(clazz.getMethods()).map(Method::getName)
        .collect(Collectors.toList());
    if (conditionsOr.size() == 0) {
      // 无条件，表示所有实体
      return queryWrapper;
    }
    for (ConditionList conditionsAnd : conditionsOr) {
      // 组装条件为OR的条件对象数组到QueryWrapper
      queryWrapper = queryWrapper.or();
      for (Condition condition : conditionsAnd.getCondition()) {
        // 检查当前传入的字段名是否存在
        if (!methods.contains("get" + getUpperCaseFirstLetter(condition.getField()))) {
          throw new ServiceException("字段：" + condition.getField() + "不存在");
        }
        // 将Field的驼峰风格字段名转换为下划线形式
        if (isUnderline) {
          condition.setField(getColumnNameFromFieldName(condition.getField()));
        } else {
          condition.setField(condition.getField());
        }
        // 组装条件为AND的条件对象到QueryWrapper
        if ("ids".equals(condition.getAction())) {
          // 首先处理多ID查询，若有多ID查询，则后续条件在这些ID中应用
          queryWrapper = queryWrapper.in("id", condition.getParam());
        }
        // 筛选其他条件
        switch (condition.getAction()) {
          case "equal":
            // 精确匹配
            queryWrapper = queryWrapper.eq(condition.getField(), condition.getParam());
            break;
          case "like":
            // 模糊匹配
            queryWrapper = queryWrapper.like(condition.getField(), condition.getParam());
            break;
          case "gt":
          case "lateThanEx":
            // 大于 / 晚于（不包括）
            queryWrapper = queryWrapper.gt(condition.getField(), condition.getParam());
            break;
          case "ge":
          case "lateThan":
            // 大于等于 / 晚于（包括）
            queryWrapper = queryWrapper.ge(condition.getField(), condition.getParam());
            break;
          case "lt":
          case "earlyThanEx":
            // 小于 / 早于（不包括）
            queryWrapper = queryWrapper.lt(condition.getField(), condition.getParam());
            break;
          case "le":
          case "earlyThan":
            // 小于等于 / 早于（包括）
            queryWrapper = queryWrapper.le(condition.getField(), condition.getParam());
            break;
          default:
            break;
        }
        if ("sort".equals(condition.getAction())) {
          // 处理排序
          if ("+".equals(condition.getParam())) {
            // 顺序（升序）
            queryWrapper = queryWrapper.orderByAsc(condition.getField());
          }
          if ("-".equals(condition.getParam())) {
            // 逆序（降序）
            queryWrapper = queryWrapper.orderByDesc(condition.getField());
          }
        }
      }
    }
    return queryWrapper;
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
