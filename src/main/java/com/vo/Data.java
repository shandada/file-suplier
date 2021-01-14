package com.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
public class Data<T>{
    private T result;
    //返回多条数据
    private List<T> results;
    //总共多少条
    private Long total;
//    //当前页数
    private long current;

}
