package com.controller;

import com.pojo.ContainerConfig;
import com.pojo.TFileInfo;
import com.pojo.TSupplier;
import com.service.ContainerConfigService;
import com.vo.Data;
import com.vo.PageRequest;
import com.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.Resources;
import java.util.List;
import java.util.UUID;

/**
 * @author pengkun shan
 * @Description:
 * @date 2021/1/19 10:36
 */
@Api(tags = "镜像管理-config-file")
@RestController
@RequestMapping("/config-file")
public class ContainerConfigController {
    @Resource
    private ContainerConfigService configService;
    /**
     * 分页+多条件查询
     *
     * @param pageRequest
     * @return
     */

    @ApiOperation(value = "分页+镜像文件关联查询", notes = "应用本体")
    @PostMapping("/condition")
    public Result queryAlarm(@RequestBody PageRequest pageRequest) {
        System.out.println(pageRequest);
        Result result = configService.queryAlarm(pageRequest);
        return result;
    }
}
