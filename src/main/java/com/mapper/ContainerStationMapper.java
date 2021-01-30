package com.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pojo.ContainerConfig;
import com.pojo.ContainerStation;
import com.pojo.Station;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Description:
 * date: 2021/1/19 11:19
 *
 * @author pengxu
 * @since JDK 1.8
 */
@Mapper
public interface ContainerStationMapper extends BaseMapper<ContainerStation> {
    @Select("select container_config.* FROM container_config left  join container_station  on container_config.uid=container_station.container_uid\n" +
            "where container_station.station_uid=#{stationId}")
    List<ContainerConfig>findContainsersByStationId(@Param("stationId") String stationId);
    @Select("SELECT station.* FROM station left  JOIN container_station ON station.UID=container_station.station_uid WHERE\n" +
            "container_station.container_uid=#{containerId}")
    List<Station>findStationByContainerId(@Param("containerId") String containerId);

    @Select("select * from container_station where container_station.container_uid= #{containerId}")
    List<ContainerStation> selectByContainerId(@Param("containerId") String containerId);
    @Select("select container_station.* FROM container_config,container_station where container_config.uid=container_station.container_uid "+
            " and container_station.station_uid=#{stationId}")
    List<ContainerStation> findByStationId(String stationId);
}
