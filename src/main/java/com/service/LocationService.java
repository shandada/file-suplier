package com.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mapper.LocationMapper;
import com.pojo.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

}
