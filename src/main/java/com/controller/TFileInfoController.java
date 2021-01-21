package com.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.excepetion.ServiceException;
import com.pojo.TFileInfo;
import com.pojo.query.GeneralJsonStatisticsViewQuery;
import com.pojo.query.GeneralJsonStatisticsViewQueryProcessor;
import com.service.SupplierFileService;
import com.service.TFileInfoService;
import com.util.CephPropertiesUtil;
import com.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.UUID;

/**
 * @author Administrator
 * @Description: //文件controller
 * @Auther: logo丶西亚卡姆
 * @Date: 2021/1/6 16:45
 */

@RestController
@RequestMapping("/fileName")
@Api(tags = "文件管理-file")
public class TFileInfoController {

    /**
     * 注入供应商service
     */
    @Resource
    private SupplierFileService supplierService;
    /**
     * 注入文件信息
     */
    @Resource
    private TFileInfoService tFileInfoService;


    /**
     * 文件实体统计接口设计
     */
    @ApiOperation(value = "文件实体统计接口", notes = "应用本体")
    @PostMapping("/statistics")
    public GeneralJsonStatisticsViewVo statisAlarm(@RequestBody GeneralJsonStatisticsViewQuery query) {
        GeneralJsonStatisticsViewQueryProcessor<TFileInfo> processor = new GeneralJsonStatisticsViewQueryProcessor<>(tFileInfoService, TFileInfo.class);
        GeneralJsonStatisticsViewVo viewVo = null;
        try {
            viewVo = processor.doStatistics(query, true);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return viewVo;
    }

    /**
     * 树状目录
     *
     * @return
     */
    @ApiOperation(value = "树状目录", notes = "应用本体")
    @GetMapping("/treeAll")
    public Result getChapterList() {
        List<OneChapter> list = tFileInfoService.queryChapterAndVideoList();
        ResultData resultData = new ResultData();
        resultData.setResults(list);

        return Result.okData(resultData);
    }

    /**
     * 多条件查询
     *
     * @param
     * @return
     */
    @ApiOperation(value = "多条件查询", notes = "应用本体")
    @PostMapping("/condition")
    public Result queryAlarm(@RequestBody PageRequest pageRequest) {
        Result result = tFileInfoService.queryAlarm(pageRequest);
        return result;
    }

    /**
     * 文件详情查询
     *
     * @param uid
     * @return
     */
    @ApiOperation(value = "文件详情查询", notes = "应用本体")
    @GetMapping("/detail/{id}")
    public Result findAll(@PathVariable("id") String uid) {
        try {
            TFileInfo byId = tFileInfoService.getById(uid);
            return Result.okData(byId);
        } catch (Exception e) {
            e.printStackTrace();
            //返回数据信息
            return Result.error();
        }
    }

    /**
     * 文件创建,
     *
     * @param fileName
     * @return
     */
    @ApiOperation(value = "文件创建", notes = "应用本体")
    @PostMapping("/create")
    public Result created(@RequestBody TFileInfo fileName, HttpServletRequest request) {
        try {
            //添加文件信息
            fileName.setUid(UUID.randomUUID().toString().replaceAll("-", ""));
            tFileInfoService.save(fileName);
            System.out.println("uid = " + fileName.getUid());
            return Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error();
        }
    }

    /**
     * 更新文件
     *
     * @param fileName
     * @return
     */
    @ApiOperation(value = "更新文件", notes = "应用本体")
    @PutMapping("/update")
    public Result update(@RequestBody TFileInfo fileName) {
        //更新文件信息并更新
        boolean flag = tFileInfoService.updateById(fileName);

        System.out.println("更新文件信息 = " + fileName);
        //判断 flag为true 成功, 否则失败
        if (flag) {
            return Result.ok();
        }
        return Result.error();
    }

    /**
     * 接收String[] 数组 单个和批量删除
     *
     * @param ids
     * @return
     */
    @ApiOperation(value = "单个和批量删除", notes = "应用本体")
    @DeleteMapping("/delete")
    public Result delMany(@RequestBody String[] ids) {
        try {
            //删除多个文件
            //调用自定义的多个删除方法
            tFileInfoService.delMany(ids);
            return Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error();
        }
    }

    //    @GetMapping("/tree2")
//    public Result findAllCategorys2() {
//        // 1. 查找一级分类
////        List<TFileInfo> list1 = tFileInfoService.findCategoryByParentid0("supplier_id");
//        List<TFileInfo> list1 = tFileInfoService.find1("0");
//        // 2. 遍历一级分类，查找二级分类
//        for (TFileInfo c1 : list1) {
//            //3. c1.id就是二级分类的父id
//            List<TFileInfo> list2 = tFileInfoService.find2(c1.getSupplierId());
//            //4. list2就是当前一级的二级分类,将list2给c1的children
//            c1.setChildren(list2);
//            //5. 遍历二级分类list2，查找三级分类
//            for (TFileInfo c2 : list2) {
//                //6. 通过c2.id 获取三级分类的数据
//                List<TFileInfo> list3 = tFileInfoService.find3(c1.getSupplierId());
//                System.out.println("c2= " + c2.getSupplierId());
//                //7. 将list3给c2的children
//                c2.setChildren(list3);
//            }
//        }
//        // 8. 组装结果，返回
//        Data data = new Data();
//        data.setResults(list1);
//        //返回数据信息
//        Result result = new Result();
//        result.ok();
//        result.setData(data);
//        return result;
//    }

}
