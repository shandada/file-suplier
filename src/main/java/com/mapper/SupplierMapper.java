package com.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pojo.TSupplier;
import org.apache.ibatis.annotations.Mapper;


/**
 * @Description:
 * @Auther: logo丶西亚卡姆
 * @Date: 2020/12/22 14:07
 */
//mapper接口
@Mapper
// 继承BaseMapper  使用mybatis-pubs完成crud操作
public interface SupplierMapper extends BaseMapper<TSupplier> {

}