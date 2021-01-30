package com.controller;

import com.excepetion.ServiceException;
import com.pojo.Supplier;
import com.pojo.query.GeneralJsonStatisticsViewQuery;
import com.pojo.query.GeneralJsonStatisticsViewQueryProcessor;
import com.pojo.query.GroupQuery;
import com.service.SupplierFileService;
import com.vo.GeneralJsonStatisticsViewVo;
import com.vo.PageRequest;
import com.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

;

/**
 * Description:  组织controller
 * date: 2020/12/28 11:22
 *
 * @author pengkun shan
 * @since JDK 1.8
 */
@RestController
@RequestMapping("/supplier")

@Api(tags = "组织管理-supplier")
public class SupplierController {

    /**
     * 注入供应商service
     */
    @Resource
    private SupplierFileService supplierService;

    /**
     * 远程调用
     */
    @Resource
    private RestTemplate restTemplate;


    @ApiOperation(value = "实体统计接口", notes = "应用本体")
    @PostMapping("/statistics")
    public GeneralJsonStatisticsViewVo statisAlarm(@RequestBody GeneralJsonStatisticsViewQuery query) {
        GeneralJsonStatisticsViewQueryProcessor<Supplier> processor = new GeneralJsonStatisticsViewQueryProcessor<>(supplierService, Supplier.class);
        GeneralJsonStatisticsViewVo viewVo = null;
        try {
            viewVo = processor.doStatistics(query, false);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return viewVo;
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
        return supplierService.queryAlarm(pageRequest);
    }

    /**
     * 2.详情查询
     *
     * @param gid
     * @return
     */
    @ApiOperation(value = "详情查询", notes = "应用本体")
    @GetMapping("/detail/{id}")
    public Result findAll(@ApiParam(value = "组织id", required = true) @PathVariable("id") String gid) {
        System.out.println("gid: " + gid);
        try {
            Supplier byId = supplierService.getById(gid);
            return Result.okData(byId);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error();
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
    public Result created(@RequestBody Supplier supplier, HttpServletRequest request) {
        try {
            Supplier tSupplier = supplierService.ReuseName(supplier.getName());
            if (tSupplier == null) {
                //添加供应商信息
                String gid = UUID.randomUUID().toString().replaceAll("-", "");
                supplier.setGid(gid);
                supplier.setGroup_id(gid);
                supplierService.save(supplier);

                /**
                 *    远程调用 organization_group表 添加供应商id
                 *   获取前端传的token
                 */
                String token = request.getHeader("Token");
                System.out.println("token = " + token);
                //訪問url + token
                String url = "http://192.168.1.205:8081/kube/v1/group/add?token=" + token;
                System.out.println("前端传的的数据token = " + token);

                /**
                 * 插入的数据  uuid 组织名称 角色id 描述 创建时间
                 */
                GroupQuery group = new GroupQuery();
                //uuid=uid
                group.setUuid(gid);
                //组织名称
                group.setGroupName(supplier.getName());
                //登录角色
                group.setRoleId(supplier.getRoleId());
                //描述
                group.setOverview(supplier.getType());
                group.setCreateTime(supplier.getCreateTime());
                //远程请求
                ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(url, group, String.class);
                System.out.println("前端传的的数据角色id: " + supplier.getRoleId());
                System.out.println("远程添加数据 = " + stringResponseEntity);
                return Result.okData(supplier.getGid());
            }

            return Result.exist().message("供应商名称已存在");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error();
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
    public Result update(@RequestBody Supplier supplier) {

        try {
            Supplier tSupplier = supplierService.ReuseName(supplier.getName());
            System.out.println("tSupplier = " + tSupplier);
            if (tSupplier == null) {
                supplierService.updateById(supplier);
                return Result.ok();
            }
            return Result.exist().message("供应商名称已存在");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error();
        }
    }

    /**
     * 5.接收String[] 数组 单个和批量删除
     *
     * @param ids
     * @return
     */
    @ApiOperation(value = "单个和批量删除", notes = "应用本体")
    @DeleteMapping("/delete")
    public Result delMany(@ApiParam(value = "传递一个数组,可放多个id", required = true) @RequestBody String[] ids) {
        try {
            supplierService.delMany(ids);
            return Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error();
        }
    }

}
