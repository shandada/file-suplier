package com.controller;

import com.excepetion.ServiceException;
import com.pojo.Supplier;
import com.pojo.query.GeneralJsonStatisticsViewQuery;
import com.pojo.query.GeneralJsonStatisticsViewQueryProcessor;
import com.pojo.query.GeneralJsonStatisticsViewVo;
import com.pojo.query.GroupQuery;
import com.service.SupplierFileService;
import com.vo.PageRequest;
import com.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${restTemplate.url}")
    private String URL;
    /**
     * 注入供应商service
     */
    @Resource
    private SupplierFileService supplierService;

    /**
     * 远程调用
     */
    @Autowired
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

    @ApiOperation(value = "id查询", notes = "应用本体")
    @GetMapping("/detail2/{id}")
    public Supplier findAllID2(@ApiParam(value = "组织id", required = true) @PathVariable("id") String gid) {
        Supplier byId = supplierService.getById(gid);
        return byId;
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

            System.out.println("tSupplier = " + tSupplier);
            //添加的组织名不能重复
            if (tSupplier == null) {

                //添加供应商信息
                String gid = UUID.randomUUID().toString().replaceAll("-", "");
                supplier.setGid(gid);
                supplier.setGroup_id(gid);
                supplier.setState("true");
                supplierService.save(supplier);

                /**
                 *    远程调用 organization_group表 添加供应商id
                 *   获取前端传的token
                 */
                String token = request.getHeader("Token");
                //訪問url + token
                //String url = "http://192.168.1.205:8081/kube/v1/group/add?token=" + token;
                System.out.println("远程调用URL = " + URL);
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
                //远程请求
                System.out.println("group = " + group);
                ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(URL, group, String.class);
                System.out.println("远程添加数据 = " + stringResponseEntity);
                return Result.ok();
            }
            return Result.exist().message("组织名已存在!");
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
            System.out.println("supplier = " + supplier);
            System.out.println("gid = " + supplier.getGid());
            System.out.println("name = " + supplier.getName());
            boolean flag = supplierService.EditName(supplier.getName());
            if (!flag) {
                return Result.exist().message("修改失败，组织名已存在!");
            }
            supplierService.updateById(supplier);
            return Result.ok();

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
            //关联删除,远程组织
            return Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error();
        }
    }

}
