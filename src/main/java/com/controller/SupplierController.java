package com.controller;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.excepetion.ServiceException;
import com.pojo.TSupplier;
import com.pojo.query.GeneralJsonStatisticsViewQuery;
import com.pojo.query.GeneralJsonStatisticsViewQueryProcessor;
import com.pojo.query.Group;
import com.service.SupplierFileService;
import com.vo.Data;
import com.vo.GeneralJsonStatisticsViewVo;
import com.vo.PageRequest;
import com.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

;
import net.minidev.json.JSONUtil;
import com.alibaba.fastjson.*;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import springfox.documentation.spring.web.json.Json;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

/**
 * Description:
 * date: 2020/12/28 11:22
 *
 * @author pengxu
 * @since JDK 1.8
 */
@RestController
@RequestMapping("/supplier")
@Api(tags = "供应商管理-supplier")
public class SupplierController {

    //注入供应商service

    @Resource
    private SupplierFileService supplierService;
    //远程调用

    @Resource
    private RestTemplate restTemplate;


    @ApiOperation(value = "实体统计接口", notes = "应用本体")
    @PostMapping("/statistics")
    public GeneralJsonStatisticsViewVo statisAlarm(@RequestBody GeneralJsonStatisticsViewQuery query) {
        GeneralJsonStatisticsViewQueryProcessor<TSupplier> processor = new GeneralJsonStatisticsViewQueryProcessor<>(supplierService, TSupplier.class);
        GeneralJsonStatisticsViewVo viewVo = null;
        try {
            viewVo = processor.doStatistics(query);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return viewVo;
    }

    /**
     * 1.分页查询所有
     * 供应商列表
     *
     * @return
     */
//    @ApiOperation(value = "分页查询所有", notes = "应用本体")
    @GetMapping("/query")
    public Result findAll(PageRequest pageRequest) {
        //获取页码
        Integer current = pageRequest.getCurrent();
        //每页条数
        Integer pageSize = pageRequest.getPageSize();
        try {
            //获取所有数据
//            分页使用map的page对象
            Page<TSupplier> pageParam = new Page<>(current, pageSize);
//            2.条件分页查询
            supplierService.pageQuery(pageParam);
            //返回结果
            List<TSupplier> list = pageParam.getRecords();
            long total1 = pageParam.getTotal();
            long current1 = pageParam.getCurrent();
            System.out.println("query:getCurrent:" + current + "\n" + "pageSize: " + pageSize);

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
     * @param pageRequest
     * @return
     */
    @ApiOperation(value = "多条件查询", notes = "应用本体")
    @PostMapping("/condition")
    public Result queryAlarm(@RequestBody PageRequest pageRequest) {
        System.out.println(pageRequest);
        Result result = supplierService.queryAlarm(pageRequest);
        return result;
    }

    /**
     * 2.详情查询
     *
     * @param gid
     * @return
     */
    @ApiOperation(value = "详情查询", notes = "应用本体")
    @GetMapping("/detail/{id}")
    public Result findAll(@PathVariable("id") String gid) {
        System.out.println("gid: " + gid);
        try {
            TSupplier byId = supplierService.getById(gid);
            //成功,返回数据
            Data data = new Data();
            //返回数据信息
            Result result = new Result();
            data.setResult(byId);
            result.ok();
            result.setData(data);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            //失败,返回错误信息
            Result result = new Result();
            result.error();
            return result;
        }
    }


    /**
     * 3.供应商创建,
     *
     * @param supplier
     * @return
     */
    @ApiOperation(value = "供应商创建", notes = "应用本体")
    @PostMapping("/create")
    public Result created(@RequestBody TSupplier supplier) {
        try {
            //获取前端传的token
            String token = supplier.getToken();
            //添加供应商信息
            String gid = UUID.randomUUID().toString().replaceAll("-", "");
            supplier.setGid(gid);
            supplier.setGroup_id(gid);
            supplierService.save(supplier);
            //访问organization_group 添加供应商id
            String url = "http://192.168.1.205:8089/aip/v1/supplier/create/?token=" + token;
            System.out.println("token = " + token);
            Group group = new Group();
            group.setUuid(gid);
            group.setGroupName("restTemplate");
            ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(url, group, String.class);
            System.out.println("stringResponseEntity = " + stringResponseEntity);

            Data data = new Data<>();
            data.setResult("gid: " + supplier.getGid());
            Result result = new Result();
            result.setData(data);
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
     * 4.更新供应商
     *
     * @param supplier
     * @return
     */
    @ApiOperation(value = "更新供应商", notes = "应用本体")
    @PutMapping("/update")
    public Result update(@RequestBody TSupplier supplier) {
        //更新供应商信息并更新
        boolean flag = supplierService.updateById(supplier);
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
     * 5.接收String[] 数组 单个和批量删除
     *
     * @param ids
     * @return
     */
    @ApiOperation(value = "单个和批量删除", notes = "应用本体")
    @DeleteMapping("/delete")
    public Result delMany(@RequestBody String[] ids) {
        try {
            supplierService.delMany(ids);
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
