package com.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pojo.Supplier;
import org.apache.ibatis.annotations.Mapper;


/**
 * @author Administrator
 * @Description: // 继承BaseMapper  使用mybatis-pubs完成crud操作
 * @Auther: logo丶西亚卡姆
 * @Date: 2020/12/22 14:07
 */
@Mapper
public interface SupplierMapper extends BaseMapper<Supplier> {

}