package com.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.ceph.CephClient;
import com.excepetion.ServiceException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mapper.TFileInfoMapper;
import com.pojo.TSupplier;
import com.pojo.query.GeneralJsonEntityQuery;
import com.pojo.query.GeneralJsonQueryWrapperBuilder;
import com.util.CephPropertiesUtil;
import com.vo.Data;
import com.vo.PageRequest;
import com.vo.Result;
import org.springframework.stereotype.Service;

import com.mapper.SupplierMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * @Description:
 * @Auther: logo丶西亚卡姆
 * @Date: 2020/12/22 14:07
 */
//供应商service类

@Service
@Transactional
public class SupplierFileService extends ServiceImpl<SupplierMapper, TSupplier> {
    @Resource
    private TFileInfoMapper tFileInfoMapper;

    /**
     * 上传文件。
     *
     * @param file 上传的文件
     * @return 文件实体对象
     */
    public void upload(MultipartFile file, String gid, String writeFile1) throws IOException {
        byte[] files = file.getBytes();
        System.out.println("files:" + files);
        CephClient cephClient = new CephClient();
        cephClient.getConnect("admin", CephPropertiesUtil.CEPH_IP,CephPropertiesUtil.KEY);
        //拼接 文件 key
        cephClient.writeFile(writeFile1, files);
    }

    /**
     * 分页
     *
     * @param pageParam
     * @param
     */
    public void pageQuery(Page<TSupplier> pageParam) {
        //封装条件QueryWrapper
        QueryWrapper<TSupplier> queryWrapper = new QueryWrapper<>();
        List<TSupplier> supplierList = baseMapper.selectList(queryWrapper);
        //供应商查询所属文件信息
        for (TSupplier thisSupper : supplierList) {
            thisSupper.setTFileInfo(tFileInfoMapper.supplierId(thisSupper.getGid()));
        }
        //执行搜索
        baseMapper.selectPage(pageParam, queryWrapper);
    }

    /**
     * 多个文件删除
     *
     * @param ids
     */
    public void delMany(String[] ids) {
        //遍历字符串,
        for (String id : ids) {
            //遍历删除
            baseMapper.deleteById(id);
        }
    }


    //条件查询
    public Result queryAlarm(PageRequest pageRequest) {
        //封装query
        GeneralJsonEntityQuery query = new GeneralJsonEntityQuery();
        query.setConditions(pageRequest.getConditions());
        QueryWrapper<TSupplier> build = null;
        try {
            //构建wrapperQuery搜索条件
            build = new GeneralJsonQueryWrapperBuilder<TSupplier>(TSupplier.class).build(query);
            //使用pageHelper插件进行非分页
//            if(pageRequest)
            PageHelper.startPage(pageRequest.getCurrent(), pageRequest.getPageSize());
            List<TSupplier> selectList = baseMapper.selectList(build);

            //供应商查询所属文件信息
            for (TSupplier thisSupper : selectList) {
                thisSupper.setTFileInfo(tFileInfoMapper.supplierId(thisSupper.getGid()));
            }
            //封装返回对象
            Result result = new Result();
            result.ok();
            PageInfo<TSupplier> pageInfo = new PageInfo<>(selectList);
            Data<TSupplier> tAlarmData = new Data<TSupplier>(null, selectList, pageInfo.getTotal(), pageInfo.getPageNum());
            result.setData(tAlarmData);
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
