package com.controller;

import com.excepetion.ServiceException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.PageInfo;
import com.pojo.Location;
import com.pojo.Station;
import com.service.LocationService;
import com.service.StationServiceImpl;
import com.vo.GeneralJsonEntityQuery;
import com.vo.Result;
import com.vo.ResultData;
import com.vo.RetJson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author yangshiling
 * @since 2021-01-08
 */
@Api(tags = "站点管理-station")
@RestController
@RequestMapping("/station/station")
@Slf4j
public class StationController {

    @Autowired
    private StationServiceImpl stationService;

    @Autowired
    private LocationService locationService;

    @ApiOperation(value = "分页列表", response = Station.class)
    @PostMapping(value = "page")
    public Object list(@RequestBody GeneralJsonEntityQuery jsonbody) throws JsonProcessingException {

        Map map = null;
        try {
            map = stationService
                    .page(jsonbody);
        } catch (ServiceException e) {
            RetJson.err(e.getMessage());
        }
        List<Station> data = (List<Station>) map.get("list");
        Integer total = Integer.parseInt(map.get("count").toString());
        log.info(total + "");
        return RetJson.ok(data, total, jsonbody.getCurrent());
    }

    @ApiOperation(value = "详情")
    @GetMapping(value = "info")
    public Object info(@RequestParam String id) {

        Object data = stationService.info(id);
        return RetJson.ok(data);
    }


    @ApiOperation(value = "修改")
    @PostMapping(value = "modify")
    public Object modify(@RequestBody Station param) {

        stationService.modify(param);
        return RetJson.ok();
    }


    @ApiOperation(value = "删除(多个条目)")
    @PostMapping(value = "removes")
    public Object removes(@RequestBody List<String> ids) {

        stationService.removes(ids);

        return RetJson.ok();
    }


    @ApiOperation(value = "根据镜像查询站点信息")
    @GetMapping(value = "queryByContainerId")
    public Object queryByContainerId(@RequestParam("imageName")@ApiParam(value = "镜像name",required = true) String imageName,@RequestParam("imageTag")@ApiParam(value = "版本号") String imageTag,@ApiParam("当前页数") @RequestParam("current")Integer current, @RequestParam("pageSize")@ApiParam("每页数据条数") Integer pageSize){
        return stationService.queryByContainerId(imageName,imageTag, current, pageSize);
    }


    @ApiOperation(value = "站点目录")
    @GetMapping("/tree")
    public Object tree() {
        List<Location> list = locationService.findAll();
        System.out.println("站点目录 = " + "tree");
        return RetJson.ok(list);
    }
}
