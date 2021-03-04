package com.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pojo.ContainerConfig;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yangshiling
 * @since 2021-01-18
 */
@Mapper
public interface ContainerConfigMapper extends BaseMapper<ContainerConfig> {

    void selectFileId(String id);
}
