package com.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.excepetion.ServiceException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mapper.ContainerConfigMapper;
import com.mapper.SupplierMapper;
import com.mapper.TFileInfoMapper;
import com.pojo.ContainerConfig;
import com.pojo.TFileInfo;
import com.pojo.TSupplier;
import com.pojo.query.GeneralJsonEntityQuery;
import com.pojo.query.GeneralJsonQueryWrapperBuilder;
import com.vo.Data;
import com.vo.PageRequest;
import com.vo.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author pengkun shan
 * @Description:
 * @date 2021/1/19 10:38
 */
@Service
@Transactional
public class ContainerConfigService extends ServiceImpl<ContainerConfigMapper, ContainerConfig> {
    @Resource
    private TFileInfoService tFileInfoService;

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
            build = new GeneralJsonQueryWrapperBuilder<ContainerConfig>(ContainerConfig.class).build(query);
            //使用pageHelper插件进行非分页
//            if(pageRequest)
            PageHelper.startPage(pageRequest.getCurrent(), pageRequest.getPageSize());
            List<ContainerConfig> selectList = baseMapper.selectList(build);

            //查询对应文件信息
            for (ContainerConfig thiConfig : selectList) {
                thiConfig.setTFileInfo(tFileInfoService.findID(thiConfig.getFileId()));
            }
            //封装返回对象
            Result result = new Result();
            result.ok();
            PageInfo<ContainerConfig> pageInfo = new PageInfo<>(selectList);
            Data<ContainerConfig> containerConfigData = new Data<ContainerConfig>(null, selectList, pageInfo.getTotal(), pageInfo.getPageNum());
            result.setData(containerConfigData);
            //返回response
            return result;
        } catch (ServiceException e) {
            e.printStackTrace();
            Result result = new Result();
            result.error();
            return result;
        }
    }
}
