package com.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ceph.CephUtils;
import com.excepetion.ServiceException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mapper.FileInfoMapper;
import com.pojo.ContainerConfig;
import com.pojo.FileInfo;
import com.pojo.query.GeneralJsonEntityQuery;
import com.pojo.query.GeneralJsonQueryWrapperBuilder;
import com.util.CephPropertiesUtil;
import com.vo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @Description:
 * @Auther: logo丶西亚卡姆
 * @Date: 2021/1/6 15:31
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class FileInfoService extends ServiceImpl<FileInfoMapper, FileInfo> implements IService<FileInfo> {

    @Resource
    private FileInfoMapper fileInfoMapper;

    /**
     * 目录树状
     */
    public List<OneChapter> queryChapterAndVideoList() {
        long start = System.currentTimeMillis();
        //定义一个目录集合
        List<OneChapter> oneChapterList = new ArrayList<>();
        //先查询目录列表集合
        QueryWrapper<FileInfo> queryWrapper = new QueryWrapper<>();
        //根据供应商id+name 去重 结果作为父节点
        queryWrapper.select("distinct supplier_id,supplier_name");
        List<FileInfo> fileNames = baseMapper.selectList(queryWrapper);
        for (FileInfo fileName : fileNames) {
            String supplierId = fileName.getSupplierId();
            //去重目录
            queryWrapper
                    .eq("supplier_id", supplierId);
            System.out.println("supplierName:supplierId " + fileName.getSupplierName() + supplierId);
        }
        //再遍历章节集合，获取每个节点ID(版本号)
        for (FileInfo eduChapter : fileNames) {
            OneChapter oneChapter = new OneChapter();
            BeanUtils.copyProperties(eduChapter, oneChapter);
            //再根据每个目录的ID查询节点的列表
            QueryWrapper<FileInfo> videoWrapper = new QueryWrapper<>();
            videoWrapper.eq("supplier_id", oneChapter.getSupplierId());
            List<FileInfo> eduVideoList = fileInfoMapper.selectList(videoWrapper);
            System.out.println("eduVideoList = " + eduVideoList);
            //把小节的列表添加目录中
            for (FileInfo thisFile : eduVideoList) {
                TwoFile twoFile = new TwoFile();
                BeanUtils.copyProperties(thisFile, twoFile);
                //数据添加到二级目录
                oneChapter.getChildren().add(twoFile);
            }
            //所有数据添加自定义list集合中,返回
            oneChapterList.add(oneChapter);
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        return oneChapterList;
    }

    /**
     * 多个文件删除
     *
     * @param ids
     */
    public void delMany(String[] ids) {
        //遍历字符串,
        for (String id : ids) {
            baseMapper.deleteById(id);
        }
    }


    /**
     * 分页列表
     *
     * @param query
     * @return
     */
    public Map<String, Object> page(GeneralJsonEntityQuery query) {

        QueryWrapper<FileInfo> queryWrapper = new QueryWrapper<>();
        Page<FileInfo> page = new Page<>(query.getCurrent(), query.getPageSize());
        queryWrapper.orderByAsc("create_time");
        IPage<FileInfo> iPage = baseMapper.selectPage(page, queryWrapper);
        long size = count(queryWrapper);
        Map<String, Object> map = new HashMap<>();
        map.put("count", size);
        map.put("list", iPage.getRecords());
        return map;
    }

    /**
     * 分页+条件查询
     *
     * @param
     */
    public Result queryAlarm(PageRequest pageRequest) {
        //封装query
        GeneralJsonEntityQuery query = new GeneralJsonEntityQuery();
        query.setConditions(pageRequest.getConditions());
        QueryWrapper<FileInfo> build = null;

        try {
            //构建wrapperQuery搜索条件
            build = new GeneralJsonQueryWrapperBuilder<>(FileInfo.class).build(query, true);
            //使用pageHelper插件进行非分页
            PageHelper.startPage(pageRequest.getCurrent(), pageRequest.getPageSize());
            List<FileInfo> tAlarms = baseMapper.selectList(build.orderByDesc("create_time"));
            PageInfo<FileInfo> pageInfo = new PageInfo<>(tAlarms);
            ResultData tAlarmData = new ResultData(null, tAlarms, pageInfo.getTotal(), pageInfo.getPageNum());
            return Result.okDataes(tAlarmData);

        } catch (ServiceException e) {
            e.printStackTrace();
            return Result.error();
        }
    }

    /**
     * 2.0
     * 文件下载
     *
     * @param
     * @param uid
     * @return
     * @throws IOException
     */
    public void download(String uid, HttpServletResponse response) throws IOException {
        System.out.println("下载文件的id = " + uid);
        String bucketName = "nanjing";
        FileInfo fileInfo = baseMapper.selectById(uid);
        String fileKey = fileInfo.getFileKey();
        String fileName = fileInfo.getFileName();
        System.out.println("下载文件信息: bucketName == uid == fileKey " + bucketName + "==" + uid + "===" + fileKey);
        CephUtils.connectCpeh(CephPropertiesUtil.ADMIN, CephPropertiesUtil.KEY, CephPropertiesUtil.CEPH_IP);
        InputStream inputStream = CephUtils.readStreamObject(bucketName, fileKey);
        String hehe = new String(fileName.getBytes("utf-8"), "iso-8859-1");
        response.setHeader("Content-Disposition", "attachment;filename=" + hehe);
        System.out.println("CEph流: inputStream = 文件下载中... " + inputStream);
        byte[] b = new byte[1024];
        int len;
        while ((len = inputStream.read(b)) > 0) {
            response.getOutputStream().write(b, 0, len);
        }
        inputStream.close();
        System.out.println("文件下载成功---  文件名:" + fileName);
    }

    /**
     * 关联id查询
     *
     * @param uid
     * @return
     */
    public FileInfo findID(String uid) {
        FileInfo fileInfo = baseMapper.selectById(uid);
        return fileInfo;
    }

    /**
     * 添加修改文件名查重
     *
     * @param fileName1
     * @return
     */
    public FileInfo ReuseName(String fileName1) {
        QueryWrapper<FileInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("file_name", fileName1);
        List<FileInfo> fileInfos = baseMapper.selectList(queryWrapper);
        return fileInfos.size() > 0 ? fileInfos.get(0) : null;
    }
}

