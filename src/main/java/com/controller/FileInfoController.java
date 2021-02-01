package com.controller;

import com.excepetion.ServiceException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.pojo.ContainerConfig;
import com.pojo.FileInfo;
import com.pojo.query.GeneralJsonEntityQuery;
import com.pojo.query.GeneralJsonStatisticsViewQuery;
import com.pojo.query.GeneralJsonStatisticsViewQueryProcessor;
import com.service.FileInfoService;
import com.service.SupplierFileService;
import com.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Administrator
 * @author pengkun shan
 * @Description: 文件库 controller
 * @Date: 2021/1/6 16:45
 */

@RestController
@RequestMapping("/fileName")
@Api(tags = "文件库管理-file")
public class FileInfoController {

    /**
     * 注入供应商service
     */
    @Resource
    private SupplierFileService supplierService;
    /**
     * 注入文件service
     */
    @Resource
    private FileInfoService fileInfoService;


    /**
     * 文件实体统计接口设计
     */
    @ApiOperation(value = "文件实体统计接口", notes = "应用本体")
    @PostMapping("/statistics")
    public GeneralJsonStatisticsViewVo statisticsAlarm(@RequestBody GeneralJsonStatisticsViewQuery query) {
        GeneralJsonStatisticsViewQueryProcessor<FileInfo> processor = new GeneralJsonStatisticsViewQueryProcessor<>(fileInfoService, FileInfo.class);
        GeneralJsonStatisticsViewVo viewVo = null;
        try {
            viewVo = processor.doStatistics(query, true);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return viewVo;
    }


    @ApiOperation(value = "分页列表", notes = "应用本体")
    @PostMapping(value = "/query")
    public Object list(@RequestBody GeneralJsonEntityQuery jsonbody) throws JsonProcessingException {
        Map map = fileInfoService.page(jsonbody);
        List<FileInfo> data = (List<FileInfo>) map.get("list");
        return RetJson.ok(data, fileInfoService.count(), jsonbody.getCurrent());
    }

    /**
     * 多条件查询
     *
     * @param
     * @return
     */
    @ApiOperation(value = "多条件查询+分页", notes = "应用本体")
    @PostMapping("/condition")
    public Result queryAlarm(@RequestBody PageRequest pageRequest) {
        System.out.println("file- pageRequest = " + pageRequest);
        return fileInfoService.queryAlarm(pageRequest);
    }

    /**
     * 文件详情查询
     *
     * @param uid
     * @return
     */
    @ApiOperation(value = "文件详情查询", notes = "应用本体")
    @GetMapping("/detail/{id}")
    public Result findAll(
            @ApiParam(value = "文件id", required = true) @PathVariable("id") String uid) {
        try {
            FileInfo byId = fileInfoService.getById(uid);
            return Result.okData(byId);
        } catch (Exception e) {
            e.printStackTrace();
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
    public Result created(@RequestBody FileInfo fileName) {
        try {
            /**
             * 随机生成uuid
             */
            fileName.setStatus(false);
            fileName.setUid(UUID.randomUUID().toString().replaceAll("-", ""));
            fileInfoService.save(fileName);
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
    public Result update(@RequestBody FileInfo fileName) {
        String fileName1 = fileName.getFileName();
        try {
//            FileInfo fileInfo = fileInfoService.ReuseName(fileName1);
//            System.out.println("tFileInfos2 = " + fileInfo);
//            //修改的文件名不能重复
//            if (fileInfo == null) {
//                fileInfoService.updateById(fileName);
//                return Result.ok();
//            }
//            return Result.exist().message("文件名已经存在");

            fileInfoService.updateById(fileName);
            return Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error();
        }
    }

    /**
     * 接收String[] 数组 单个和批量删除
     *
     * @param ids
     * @return
     */
    @ApiOperation(value = "单个和批量删除", notes = "应用本体")
    @DeleteMapping("/delete")
    public Result delMany(@ApiParam(value = "传递一个数组,可放多个id", required = true) @RequestBody String[] ids) {
        try {
            fileInfoService.delMany(ids);
            return Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error();
        }
    }

    /**
     * 树状目录
     *
     * @return
     */
    @ApiOperation(value = "树状目录", notes = "应用本体")
    @GetMapping("/treeAll")
    public Result getChapterList() {
        List<OneChapter> list = fileInfoService.queryChapterAndVideoList();
        System.out.println("123 = " + list);
        ResultData resultData = new ResultData();
        resultData.setResults(list);
        return Result.okData(resultData);
    }
}
