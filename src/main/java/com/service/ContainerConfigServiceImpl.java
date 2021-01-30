package com.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mapper.ContainerConfigMapper;
import com.mapper.ContainerStationMapper;
import com.pojo.ContainerConfig;
import com.vo.GeneralJsonEntityQuery;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author yangshiling
 * @since 2021-01-18
 */
@Service
public class ContainerConfigServiceImpl extends ServiceImpl<ContainerConfigMapper, ContainerConfig>  {
  @Resource
  private ContainerStationMapper containerStationMapper;


  public Map<String, Object> page(GeneralJsonEntityQuery query) {

    QueryWrapper<ContainerConfig> queryWrapper = new QueryWrapper<>();
    Page<ContainerConfig> page = new Page<>(query.getCurrent(), query.getPageSize());
    queryWrapper.orderByAsc("CreateTime");
    IPage<ContainerConfig> iPage = baseMapper.selectPage(page, queryWrapper);
    long size = count(queryWrapper);
    Map<String, Object> map = new HashMap<>();
    map.put("count", size);
    map.put("list", iPage.getRecords());
    return map;
  }


  /**
   * 详情
   *
   * @param id
   * @return
   */
  public ContainerConfig info(String id) {

    return getById(id);
  }

  /**
   * 新增
   *
   * @param param 根据需要进行传值
   * @return
   */
  public boolean add(ContainerConfig param) {

    return save(param);
  }

  /**
   * 修改
   *
   * @param param 根据需要进行传值
   * @return
   */
  public boolean modify(ContainerConfig param) {

    return updateById(param);
  }

  /**
   * 删除(单个条目)
   *
   * @param id
   * @return
   */
  public boolean remove(String id) {
    return removeById(id);
  }

  /**
   * 删除(多个条目)
   *
   * @param ids
   * @return
   */
  public boolean removes(List<String> ids) {

    return removeByIds(ids);
  }


}
