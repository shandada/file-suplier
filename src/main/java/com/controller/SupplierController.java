package com.controller;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pojo.TFileInfo;
import com.pojo.TSupplier;
import com.pojo.query.FileQuery;
import com.pojo.query.SuppQuery;
import com.service.SupplierFileService;
import com.vo.Data;
import com.vo.PageRequest;
import com.vo.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/supplier")
public class SupplierController {

    //注入供应商service

    @Resource
    private SupplierFileService supplierService;
    @GetMapping("/all")
    public List<TSupplier> find(){
        List<TSupplier> list = supplierService.list(null);
        return list;
    }
    /**
     * 1.分页查询所有
     * 供应商列表
     *
     * @return
     */
    @GetMapping("/query")
    public Result findAll(PageRequest pageRequest) {
        //获取页码
        Integer pageNo = pageRequest.getPageNo();
        //每页条数
        Integer pageSize = pageRequest.getPageSize();
        try {
            //获取所有数据
//            分页使用map的page对象
            Page<TSupplier> pageParam = new Page<>(pageNo, pageSize);
//            2.条件分页查询
            supplierService.pageQuery(pageParam);
            //返回结果
            List<TSupplier> list = pageParam.getRecords();
            long total1 = pageParam.getTotal();
            System.out.println("pageNo:" + pageNo+"\n" +"pageSize: "+pageSize);

//            成功,返回数据
            Data data = new Data();
            //返回数据信息
            Result result = new Result();
            data.setResults(list);
            data.setTotal(total1);
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
     *    多条件查询
     * @param pageRequest
     * @return
     */
    @PostMapping("/condition")
    public Result queryAlarm(@RequestBody PageRequest pageRequest){
        System.out.println(pageRequest);
        Result result=supplierService.queryAlarm(pageRequest);
        return result;
    }
    /**
     * 2.详情查询
     *
     * @param gid
     * @return
     */
    @GetMapping("/detail/{id}")
    public Result findAll(@PathVariable("id") String gid) {
        System.out.println("gid: "+gid);
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

    @PostMapping("/create")
    public Result created(@RequestBody TSupplier supplier) {
        try {
            //添加供应商信息
            supplier.setGid(UUID.randomUUID().toString().replaceAll("-", ""));
            supplierService.save(supplier);
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
     * 4.更新供应商
     *
     * @param supplier
     * @return
     */
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
