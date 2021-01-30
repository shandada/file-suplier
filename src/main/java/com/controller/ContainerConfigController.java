package com.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.PageInfo;
import com.pojo.ContainerConfig;
import com.pojo.query.GeneralJsonEntityQuery;
import com.service.ContainerConfigService;
import com.vo.PageRequest;
import com.vo.Result;
import com.vo.RetJson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author pengkun shan
 * @Description:
 * @date 2021/1/19 10:36
 */
@Api(tags = "算法库管理-config-file")
@RestController
@RequestMapping("/config-file")
public class ContainerConfigController {

    /**
     * 注入
     */
    @Resource
    private ContainerConfigService configService;

    /**
     * 分页+多条件查询
     *
     * @param pageRequest
     * @return
     */

    @ApiOperation(value = "分页+算法库文件库关联查询", notes = "应用本体")
    @PostMapping("/condition")
    public Result queryAlarm(@RequestBody PageRequest pageRequest) {
        System.out.println(pageRequest);
        return configService.queryAlarm(pageRequest);
    }

    @ApiOperation(value = "分支master状态修改", notes = "应用本体")
    @PostMapping("/master-update/{uid}")
    public Result versions(@PathVariable("uid") String uid) {
        ContainerConfig byId = configService.getById(uid);
        System.out.println("byId = " + byId);
        String imageName = byId.getImageName();
        System.out.println("imageName = " + imageName);
        Boolean master = byId.getMaster();
        if (master == true) {
            System.out.println("master = " + master);
            configService.updateMaster(imageName);
            byId.setMaster(true);
            configService.updateById(byId);
        }
        if (master == false) {
            System.out.println("master = " + master);
            configService.updateMaster(imageName);
            byId.setMaster(true);
            configService.updateById(byId);
        }
        return Result.okData(uid);
    }





    @ApiOperation(value = "分页列表", response = ContainerConfig.class)
    @PostMapping(value = "/page")
    public Object list(@RequestBody GeneralJsonEntityQuery jsonbody) throws JsonProcessingException {
        Map map = configService
                .page(jsonbody);
        List<ContainerConfig> data = (List<ContainerConfig>) map.get("list");
        return RetJson.ok(data, configService.count(), jsonbody.getCurrent());
    }

    @ApiOperation(value = "详情", response = ContainerConfig.class)
    @GetMapping(value = "/info")
    public Object info(@RequestParam String id) {

        Object data = configService.info(id);
        return RetJson.ok(data);
    }

    @ApiOperation(value = "新增")
    @PostMapping(value = "/add")
    public Object add(@RequestBody ContainerConfig param) {

        configService.add(param);
        return RetJson.ok();
    }

    @ApiOperation(value = "修改")
    @PostMapping(value = "/modify")
    public Object modify(@RequestBody ContainerConfig param) {

        configService.modify(param);
        return RetJson.ok();
    }

    @ApiOperation(value = "删除(单个条目)")
    @GetMapping(value = "/remove")
    public Object remove(@RequestParam String id) {

        configService.remove(id);
        return RetJson.ok();
    }

    @ApiOperation(value = "删除(多个条目)")
    @PostMapping(value = "/removes")
    public Object removes(@RequestBody List<String> ids) {

        configService.removes(ids);
        return RetJson.ok();
    }


    @ApiOperation(value = "根据站点查询所属镜像")
    @GetMapping("/queryBystationId")
    public Object findContainsersByStationId(@RequestParam("stationId")@ApiParam(value = "站点id",required = true) String stationId, @ApiParam("当前页数")@RequestParam("current")Integer current, @ApiParam("每页数据条数")@RequestParam("pageSize")Integer pageSize){
        return configService.findContainsersByStationId(stationId, current, pageSize);
    }
}
