package com.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.excepetion.ServiceException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pojo.TFileInfo;
import com.pojo.TSupplier;
import com.pojo.query.Condition;
import com.pojo.query.ConditionList;
import com.pojo.query.FileQuery;
import com.pojo.query.GeneralJsonEntityQuery;
import com.util.FileUtil;
import com.util.FileUtil2;
import com.util.GeneralJsonQueryWrapperBuilder;
import com.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Results;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mapper.TFileInfoMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description:
 * @Auther: logo丶西亚卡姆
 * @Date: 2021/1/6 15:31
 */
@Service
@Transactional
public class TFileInfoService extends ServiceImpl<TFileInfoMapper, TFileInfo> implements FileImpl {

    @Resource
    private TFileInfoMapper tFileInfoMapper;


    //树状方法
    public List<OneChapter> queryChapterAndVideoList() {

        //定义一个目录集合
        List<OneChapter> oneChapterList = new ArrayList<>();
        //先查询目录列表集合
        QueryWrapper<TFileInfo> queryWrapper = new QueryWrapper<>();
        //根据供应商id+name 去重 结果作为父节点
        queryWrapper.select("distinct supplier_id,supplier_name");
        List<TFileInfo> fileNames = baseMapper.selectList(queryWrapper);
        for (TFileInfo fileName : fileNames) {
            String supplierId = fileName.getSupplierId();
            //去重目录
            System.out.println("supplierName:supplierId " + fileName.getSupplierName() + supplierId);
            queryWrapper
                    .eq("supplier_id", supplierId);
        }
        //再遍历章节集合，获取每个节点ID(版本号)
        for (TFileInfo eduChapter : fileNames) {
            OneChapter oneChapter = new OneChapter();
            BeanUtils.copyProperties(eduChapter, oneChapter);
            //再根据每个目录的ID查询节点的列表
            QueryWrapper<TFileInfo> videoWrapper = new QueryWrapper<>();
            videoWrapper.eq("supplier_id", oneChapter.getSupplierId());
            List<TFileInfo> eduVideoList = tFileInfoMapper.selectList(videoWrapper);
            //把小节的列表添加目录中
            for (TFileInfo thisFile : eduVideoList) {
                TwoFile twoFile = new TwoFile();
                BeanUtils.copyProperties(thisFile, twoFile);
                //数据添加到二级目录
                oneChapter.getChildren().add(twoFile);
            }
            //所有数据添加自定义list集合中,返回
            oneChapterList.add(oneChapter);
        }
        return oneChapterList;
    }

    /**
     * 分页
     *
     * @param pageParam
     * @param pageParam
     */
    public void pageQuery(Page<TFileInfo> pageParam) {
        //封装条件QueryWrapper
        QueryWrapper<TFileInfo> queryWrapper = new QueryWrapper<>();

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
            baseMapper.deleteById(id);
        }
    }


    /**
     * 条件查询
     *
     * @param
     */
    public Result queryAlarm(PageRequest pageRequest) {
        //封装query
        GeneralJsonEntityQuery query = new GeneralJsonEntityQuery();
        query.setConditions(pageRequest.getConditions());
        QueryWrapper<TFileInfo> build = null;
        try {
            //构建wrapperQuery搜索条件
            build = new GeneralJsonQueryWrapperBuilder<TFileInfo>(TFileInfo.class).build(query);
            //使用pageHelper插件进行非分页
//            if(pageRequest)
            PageHelper.startPage(pageRequest.getCurrent(), pageRequest.getPageSize());
            List<TFileInfo> tAlarms = baseMapper.selectList(build);
            //封装图片数据
            //封装返回对象
            Result result = new Result();
            result.ok();
            PageInfo<TFileInfo> pageInfo = new PageInfo<>(tAlarms);
            Data<TFileInfo> tAlarmData = new Data<TFileInfo>(null, tAlarms, pageInfo.getTotal(), pageInfo.getPageNum());
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


    /**
     * 文件下载
     *
     * @param uid
     * @param
     * @throws IOException
     */
    public void download(String uid) throws IOException {

        //根据id获取服务器文件url
        TFileInfo tFileInfo = baseMapper.selectById(uid);
        String fileName = tFileInfo.getFileName();
        //拼接的文件名
        String fileKey = tFileInfo.getFileKey();
        System.out.println("详细数据: " + tFileInfo);
        System.out.println("下载的文件名 && fileKey: " + fileName + "=====" + fileKey);

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = requestAttributes.getResponse();
//        String type = new MimetypesFileTypeMap().getContentType(fileName);
        // 设置contenttype，即告诉客户端所发送的数据属于什么类型
//        response.setHeader("Content-type", type);
        // 设置编码
        String hehe = new String(fileName.getBytes("utf-8"), "iso-8859-1");
        // 设置扩展头，当Content-Type 的类型为要下载的类型时 , 这个信息头会告诉浏览器这个文件的名字和类型。
        response.setHeader("Content-Disposition", "attachment;filename=" + hehe);
        FileUtil.download(fileName, response, fileKey);
//        FileUtil2.download2(fileName,response);
    }

    @Override
    public List<TFileInfo> all() {
        return baseMapper.selectList(null);
    }
}

