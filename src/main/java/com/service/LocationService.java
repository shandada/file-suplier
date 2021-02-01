package com.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mapper.FileInfoMapper;
import com.mapper.LocationMapper;
import com.mapper.StationMapper;
import com.pojo.Location;
import com.pojo.Station;
import com.pojo.query.GroupQuery;
import com.pojo.query.TAlarm;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.security.PublicKey;
import java.util.List;

/**
 * @author pengkun Shan
 * @Description:
 * @date 2021/1/30 13:58
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class LocationService extends ServiceImpl<LocationMapper, Location> {

    @Autowired
    private StationServiceImpl stationService;

    /**
     * 远程调用
     */
    @Resource
    private RestTemplate restTemplate;

    public List<Location> findAll() {

//        String url = "http://192.168.1.205:10100/aip/v1/alarm_manage/alarm/detail";
//
//        /**
//         * 插入的数据  uuid 组织名称 角色id 描述 创建时间
//         */
//        TAlarm alarm = new TAlarm();
//        //远程请求
//        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(url, alarm, String.class);
//        System.out.println("stringResponseEntity = " + stringResponseEntity);

        QueryWrapper<Location> queryWrapper = new QueryWrapper<>();
        List<Location> locations = baseMapper.selectList(queryWrapper);


//        for (Location location : locations) {
//            location.setTAlarm(stationService.stringResponseEntity(location.getStationId()));
//        }

        return locations;
    }
}
