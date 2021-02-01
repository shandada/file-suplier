package com.pojo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.pojo.query.TAlarm;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * @author pengkun Shan
 * @Description:
 * @date 2021/1/30 13:55
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Location {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    private String name;
    /**
     * province,city,station
     */
    private String type;
    /**
     * 如果类型为prov为
     */
    private String fatherId;
    /**
     * 类型station  就是stationid
     */
    private String stationId;

    @TableField(exist = false)
    private TAlarm tAlarm;
}