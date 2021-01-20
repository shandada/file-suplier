package com.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pojo.ContainerConfig;
import com.pojo.TFileInfo;
import com.pojo.TSupplier;
import org.apache.ibatis.annotations.*;

import java.util.List;


/**
 * @Description:
 * @author pengkun shan
 * @date 2021/1/19 10:32
 */
@Mapper
public interface ContainerConfigMapper extends BaseMapper<ContainerConfig> {

////    @Select("SELECT * FROM t_file_info WHERE supplier_id IN(SELECT gid FROM t_supplier WHERE gid= #{gid, jdbcType=VARCHAR})")
//    @Select("SELECT *FROM container_config  WHERE fileId =(SELECT UID FROM t_file_info WHERE uid= #{uid, jdbcType=VARCHAR})")
//    @Results({
//    })
//    public List<ContainerConfig> listFile(@Param("uid") String uid);
}