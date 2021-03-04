package com.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.excepetion.ServiceException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mapper.ContainerConfigMapper;
import com.mapper.ContainerStationMapper;
import com.pojo.ContainerConfig;
import com.pojo.ContainerStation;
import com.pojo.FileInfo;
import com.pojo.query.GeneralJsonEntityQuery;
import com.pojo.query.GeneralJsonQueryWrapperBuilder;
import com.vo.PageRequest;
import com.vo.Result;
import com.vo.ResultData;
import com.vo.RetJson;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author pengkun shan
 * @Description:
 * @date 2021/1/19 10:38
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ContainerConfigService extends ServiceImpl<ContainerConfigMapper, ContainerConfig> {
    @Resource
    private FileInfoService fileInfoService;


    /**
     * 镜像分页+条件查询
     */
    public Result queryAlarm(PageRequest pageRequest) {

        //封装query
        GeneralJsonEntityQuery query = new GeneralJsonEntityQuery();
        query.setConditions(pageRequest.getConditions());
        QueryWrapper<ContainerConfig> build = null;
        try {
            //构建wrapperQuery搜索条件
            build = new GeneralJsonQueryWrapperBuilder<ContainerConfig>(ContainerConfig.class).build(query, false);
            //使用pageHelper插件进行非分页
            PageHelper.startPage(pageRequest.getCurrent(), pageRequest.getPageSize());
            List<ContainerConfig> selectList = baseMapper.selectList(build.orderByDesc("createTime"));
            //关联查询文件库
            for (ContainerConfig thiConfig : selectList) {
                FileInfo fileInfo = fileInfoService.findID(thiConfig.getFileId());
                System.out.println("fileInfo = " + fileInfo);
                if (fileInfo != null) {
                    thiConfig.setFileInfo(fileInfo);
                } else {
                    thiConfig.setFileInfo(null);
                }
            }
            //封装返回对象
            PageInfo<ContainerConfig> pageInfo = new PageInfo<>(selectList);
            ResultData containerConfigData = new ResultData(null, selectList, pageInfo.getTotal(), pageInfo.getPageNum());
            return Result.okDataes(containerConfigData);
        } catch (ServiceException e) {
            e.printStackTrace();
            return Result.error();
        }
    }

    /**
     * 修改算法库master
     *
     * @param imageName
     */
    public void updateMaster(String imageName) {
        QueryWrapper<ContainerConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("imageName", imageName);
        List<ContainerConfig> containerConfigs = baseMapper.selectList(queryWrapper);
        for (ContainerConfig thisContainerConfigs : containerConfigs) {
            thisContainerConfigs.setMaster(false);
            baseMapper.updateById(thisContainerConfigs);
        }
    }

    @Resource
    private ContainerStationMapper containerStationMapper;


    /**
     * 分页
     *
     * @param query
     * @return
     */
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

    /**
     * 根据变电站查询 镜像s
     * @param stationId
     * @param current
     * @param pageSize
     * @return
     */
    public Object findContainsersByStationId(String stationId, Integer current, Integer pageSize) {
        PageHelper.startPage(current, pageSize);
        PageHelper.startPage(current, pageSize);
        List<ContainerConfig> containsers = containerStationMapper.findContainsersByStationId(stationId);
        List<ContainerStation> containerStations = containerStationMapper.findByStationId(stationId);
        PageInfo<ContainerConfig> pageInfo = new PageInfo<>(containsers);
        for (int i = 0; i < containsers.size(); i++) {
            ContainerConfig containerConfig = containsers.get(i);
            containerConfig.setContainerStatus(containerStations.get(i).getContainerStatus());
            containerConfig.setUpCreateTime(containerStations.get(i).getUpCreateTime());
        }
//        for (ContainerConfig containser : containsers) {
////        List<ContainerStation> containerStations = containerStationMapper.selectByContainerId(containser.getUid());
////            containser.setCreateTime(containerStation.getCreateTime());
////            containser.setContainerStatus(containerStation.getStatus());
//        }
        Integer total = new Integer((int) pageInfo.getTotal());
        return RetJson.ok(containsers, total, current);
    }

    /**
     * 删除后 主线设置
     *
     * @param ids
     */
    public void IdsMaster(List<String> ids) {
        List<ContainerConfig> configList = baseMapper.selectBatchIds(ids);
        QueryWrapper<ContainerConfig> queryWrapper = new QueryWrapper<>();
        for (ContainerConfig containerConfig : configList) {
            String thisImageName = containerConfig.getImageName();
            queryWrapper.eq("imageName", thisImageName).orderByDesc("createTime");
        }
        List<ContainerConfig> containerConfigs = baseMapper.selectList(queryWrapper);
        ContainerConfig containerConfig1 = containerConfigs.get(0);
        containerConfig1.setMaster(true);
        baseMapper.updateById(containerConfig1);
        System.out.println("containerConfig1 = " + containerConfig1);
    }

//    public List<ContainerConfig> selectFileId(String id) {
//        QueryWrapper<ContainerConfig> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("fileId",id);
//        return baseMapper.selectList(queryWrapper);
//    }

    public boolean selectFileId(String id) {
        QueryWrapper<ContainerConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("fileId",id);
        System.out.println("integer = " + baseMapper.selectCount(queryWrapper));
        if (baseMapper.selectCount(queryWrapper)>0){
            return false;
        }
        return true;
    }

}

