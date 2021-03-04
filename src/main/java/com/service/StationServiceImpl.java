package com.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.excepetion.ServiceException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mapper.ConStaViewMapper;
import com.mapper.ContainerConfigMapper;
import com.mapper.ContainerStationMapper;
import com.mapper.StationMapper;
import com.pojo.ContainerStationView;
import com.pojo.Station;
import com.pojo.query.GeneralJsonEntityQuery;
import com.pojo.query.GeneralJsonQueryWrapperBuilder;
import com.vo.RetJson;
import lombok.extern.slf4j.Slf4j;
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
 * @since 2021-01-08
 */
@Service
@Slf4j
public class StationServiceImpl extends ServiceImpl<StationMapper, Station> {

    @Resource
    private ContainerStationMapper containerStationMapper;
    @Resource
    private ContainerConfigMapper containerConfigMapper;
    @Resource
    private ConStaViewMapper conStaViewMapper;

    public Map<String, Object> page(GeneralJsonEntityQuery query) throws ServiceException {
        GeneralJsonQueryWrapperBuilder<Station> generalJsonQueryWrapperBuilder = new GeneralJsonQueryWrapperBuilder<>(
                Station.class);
        QueryWrapper<Station> queryWrapper = generalJsonQueryWrapperBuilder.build(query, false);
        queryWrapper.orderByAsc("CreateTime");

        Page<Station> page = new Page<>(query.getCurrent(), query.getPageSize());
        IPage<Station> iPage = baseMapper.selectPage(page, queryWrapper);
        System.out.println(queryWrapper.getSqlSelect());
        Integer size = count(queryWrapper);
        Map<String, Object> map = new HashMap<>();
        log.info(size + "");
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

    public Station info(String id) {

        return getById(id);
    }

    /**
     * 新增
     *
     * @param param 根据需要进行传值
     * @return
     */


    /**
     * 修改
     *
     * @param param 根据需要进行传值
     * @return
     */

    public void modify(Station param) {

        updateById(param);
    }

    /**
     * 删除(单个条目)
     *
     * @param id
     * @return
     */

    public void remove(String id) {
        removeById(id);
    }

    /**
     * 删除(多个条目)
     *
     * @param ids
     * @return
     */

    public void removes(List<String> ids) {

        removeByIds(ids);
    }

    /**
     * 根据镜像查询所属站点
     *
     * @param
     * @param current
     * @param pageSize
     * @return
     */

    public Object queryByContainerId(String imageName, String imageTag, Integer current, Integer pageSize) {
        PageHelper.startPage(current, pageSize);
        QueryWrapper<ContainerStationView> wrapper = new QueryWrapper<>();
        wrapper.eq("imageName", imageName);
        wrapper.eq("imageTag", imageTag);
        List<ContainerStationView> stationByContainerId = conStaViewMapper.selectList(wrapper);
        PageInfo<ContainerStationView> pageInfo = new PageInfo<ContainerStationView>(stationByContainerId);
        Integer total = new Integer((int) pageInfo.getTotal());
        return RetJson.ok(stationByContainerId, total, current);
    }

    public Station findStationId(String stationId) {
        Station station = baseMapper.selectById(stationId);
        return station;
    }
}
