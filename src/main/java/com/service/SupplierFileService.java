package com.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ceph.CephUtils;
import com.excepetion.ServiceException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mapper.FileInfoMapper;
import com.mapper.SupplierMapper;
import com.pojo.FileInfo;
import com.pojo.Supplier;
import com.pojo.query.GeneralJsonEntityQuery;
import com.pojo.query.GeneralJsonQueryWrapperBuilder;
import com.util.CephPropertiesUtil;
import com.util.FileSizeUtil;
import com.vo.PageRequest;
import com.vo.Result;
import com.vo.ResultData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author Administrator
 * @Description: //供应商service类
 * @Auther: logo丶西亚卡姆
 * @Date: 2020/12/22 14:07
 */

@Service
@Transactional(rollbackFor = Exception.class)

public class SupplierFileService extends ServiceImpl<SupplierMapper, Supplier> {
    @Resource
    private FileInfoMapper fileInfoMapper;


    /**
     * 2.0
     *
     * @param file 上传的文件
     * @return 文件实体对象  fileKey 上传到CEph的文件名
     */
    public void upload(String bucketName, String fileKey, MultipartFile file) throws IOException {
        System.out.println("file:" + file + "--------" + "桶名: " + bucketName+"-----"+ "fileKey"+"--"+fileKey);
        //连接CEph
        CephUtils.connectCpeh(CephPropertiesUtil.ADMIN, CephPropertiesUtil.KEY, CephPropertiesUtil.CEPH_IP);
        CephUtils.mulutiUpload(bucketName, fileKey, file);
    }

    /**
     * 添加的文件信息
     *
     * @param file
     * @param gid
     * @throws UnsupportedEncodingException
     */
    public FileInfo addFileINfo(MultipartFile file, String gid) throws UnsupportedEncodingException {
        //文件大小
        FileSizeUtil sizeUtil = new FileSizeUtil();
        long size = file.getSize();
        String sizes = sizeUtil.getPrintSize(size);
        //文件全名
        String filenameAll = file.getOriginalFilename();
        // gbk转utf-8，需要确定原编码格式，不知道的话就试一下
        byte[] s = filenameAll.getBytes("utf-8");
        String filename = new String(s, "UTF-8");
        System.out.println("上传文件大小sizes===filename 文件名: " + filename+"-"+sizes);
        //接受供应商gid 获取供应商信息
        Supplier supplier1 = baseMapper.selectById(gid);
        //获取供应商name
        String name = supplier1.getName();
        //供应商类型
        String type = supplier1.getType();
        //版本号 暂定当前时间 //设置日期格式
        SimpleDateFormat df = new SimpleDateFormat("HH.mm.ss");
        //版本号
        String version = df.format(new Date());
        //向数据库文件表 添加上传文件信息
        String uid = UUID.randomUUID().toString().replaceAll("-", "");
        // 传给ceph的信息   uid+文件名
        String file_key = uid + "-" + filename;
        System.out.println("写入ceph的 file_key :  " + file_key);

        FileInfo fileInfo = new FileInfo(
                //文件id  //文件名   //版本号 供应商id 名称     描述       创建时间       更新时间    类型   大小  ceph_key    标签          //上传人    //历史版本      //状态                //逻辑删除,0 显示
                uid, filename, version, gid, name, null, null, null, type, sizes, file_key, null, null, true, true, 0);
        return fileInfo;
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


    /**
     * 供应商分页+条件查询
     *
     * @param pageRequest
     * @return
     */
    public Result queryAlarm(PageRequest pageRequest) {
        //封装query
        GeneralJsonEntityQuery query = new GeneralJsonEntityQuery();
        query.setConditions(pageRequest.getConditions());
        QueryWrapper<Supplier> build = null;

        try {
            //构建wrapperQuery搜索条件
            build = new GeneralJsonQueryWrapperBuilder<Supplier>(Supplier.class).build(query, true);
            //使用pageHelper插件进行非分页
            PageHelper.startPage(pageRequest.getCurrent(), pageRequest.getPageSize());
            List<Supplier> selectList = baseMapper.selectList(build.orderByDesc("create_time"));

            //关联查询文件库
            for (Supplier thisSupper : selectList) {
                thisSupper.setFileInfo(fileInfoMapper.supplierId(thisSupper.getGid()));
            }
            //封装返回对象
            PageInfo<Supplier> pageInfo = new PageInfo<>(selectList);
            ResultData tAlarmData = new ResultData(null, selectList, pageInfo.getTotal(), pageInfo.getPageNum());

            //返回response
            return Result.okDataes(tAlarmData);
        } catch (ServiceException e) {
            return Result.error();
        }
    }

    /**
     * 添加修改文件名查重
     *
     * @param name
     * @return
     */
    public Supplier ReuseName(String name) {
        QueryWrapper<Supplier> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", name);
        List<Supplier> suppliers = baseMapper.selectList(queryWrapper);
        return suppliers.size() > 0 ? suppliers.get(0) : null;
    }
}
