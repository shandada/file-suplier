package com.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pojo.TFileInfo;
import com.pojo.query.FileQuery;
import com.service.SupplierFileService;
import com.service.TFileInfoService;
import com.vo.Data;
import com.vo.OneChapter;
import com.vo.PageRequest;
import com.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.UUID;

/**
 * @Description:
 * @Auther: logo丶西亚卡姆
 * @Date: 2021/1/6 16:45
 */
//文件controller
@RestController
@RequestMapping("/fileName")
@Api(description = "文件管理")
public class TFileInfoController {
    /**
     * 客户端,ip,秘钥
     */
    //TODO  ceph
//    public MyCeph cephUtils = new CephUtils("admin", "192.168.1.13:6789", "198.162.1.1.2");
    //返回数据类型

    //注入session
    @Resource
    private HttpSession session;
    //注入供应商service
    @Resource
    private SupplierFileService supplierService;
    //注入文件信息
    @Resource
    private TFileInfoService tFileInfoService;

    /**
     * 树状目录
     *
     * @return
     */
    @GetMapping("/treeAll")
    public Result getChapterList() {
        List<OneChapter> list = tFileInfoService.queryChapterAndVideoList();
//        if(list.size()>=0){
        //
        Data data = new Data();
        //返回数据信息
        Result result = new Result();
        data.setResults(list);
        result.ok();
        result.setData(data);
        return result;
    }

    /**
     * 分页查询所有
     * 文件列表
     *
     * @return
     */
    @GetMapping("/query")
    public Result findAll(PageRequest pageRequest) {
        //获取页码
        Integer current = pageRequest.getCurrent();
        //每页条数
        Integer pageSize = pageRequest.getPageSize();
        System.out.println("current" + current+"\n"+"pageSize" + pageSize);
        System.out.println();
        try {
            //获取所有数据
//            分页使用map的page对象
            Page<TFileInfo> pageParam = new Page<>(current, pageSize);
//            2.条件分页查询
            tFileInfoService.pageQuery(pageParam);
            //返回结果
            List<TFileInfo> list = pageParam.getRecords();
            long total1 = pageParam.getTotal();
            long current1 = pageParam.getCurrent();
//            成功,返回数据
            Data data = new Data();
            //返回数据信息
            Result result = new Result();
            data.setResults(list);
            data.setTotal(total1);
            data.setCurrent(current1);
            result.ok();
            result.setData(data);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            //失败,返回错误信息
            //返回数据信息
            Result result = new Result();
            result.error();
            return result;
        }
    }

    /**
     * 多条件查询
     *
     * @param
     * @return
     */
    @PostMapping("/condition")
    public Result queryAlarm(@RequestBody PageRequest pageRequest){
        Result result=tFileInfoService.queryAlarm(pageRequest);
        return result;
    }

    /**
     * 文件详情查询
     *
     * @param uid
     * @return
     */
    @GetMapping("/detail/{id}")
    public Result findAll(@PathVariable("id") String uid) {
        try {
            TFileInfo byId = tFileInfoService.getById(uid);
            Data data = new Data();
            //返回数据信息
            Result result = new Result();
            data.setResult(byId);
            result.ok();
            result.setData(data);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            //返回数据信息
            Result result = new Result();
            //失败,返回错误信息
            result.error();
            return result;
        }
    }

    /**
     * 文件创建,
     *
     * @param fileName
     * @return
     */

    @PostMapping("/create")
    public Result created(@RequestBody TFileInfo fileName, HttpServletRequest request) {
        try {
            //添加文件信息
            fileName.setUid(UUID.randomUUID().toString().replaceAll("-", ""));
            tFileInfoService.save(fileName);
            String uid = fileName.getUid();
            System.out.println(uid+"uid");


            Result result = new Result();
            result.ok();
            return result;
        } catch (
                Exception e) {
            e.printStackTrace();
            Result result = new Result();
            result.error();
            return result;
        }
    }

    /**
     * 更新文件
     *
     * @param fileName
     * @return
     */
    @PutMapping("/update")
    public Result update(@RequestBody TFileInfo fileName) {
        //更新文件信息并更新
        boolean flag = tFileInfoService.updateById(fileName);
        //判断 flag为true 成功, 否则失败
        if (flag) {
            Result result = new Result();
            result.ok();
            return result;
        }
        Result result = new Result();
        result.error();
        return result;
    }

    /**
     * 接收String[] 数组 单个和批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping("/delete")
    public Result delMany(@RequestBody String[] ids) {
        try {
            //删除多个文件
            //调用自定义的多个删除方法
            tFileInfoService.delMany(ids);
            Result result = new Result();
            result.ok();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            Result result = new Result();
            result.error();
            return result;
        }
    }
}
