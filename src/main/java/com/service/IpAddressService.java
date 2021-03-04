package com.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mapper.IpAddressMapper;
import com.pojo.IpAddress;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author pengkun Shan
 * @Description:
 * @date 2021/3/2 9:57
 */

@Service
@Transactional(rollbackFor = Exception.class)
public class IpAddressService extends ServiceImpl<IpAddressMapper, IpAddress> {


    public IpAddress getIpAddress() {
        QueryWrapper<IpAddress> queryWrapper = new QueryWrapper<>();
        List<IpAddress> ipAddresses = baseMapper.selectList(queryWrapper);
        return ipAddresses.get(0);
    }
}
