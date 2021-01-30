package com.vo;


import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.Date;


/**
 * @author strator
 */
@Data
public class RetJson implements Serializable {

  /**
   * 状态码
   */
  private int code = 200;
  /**
   * 消息
   */
  private String msg;
  /**
   * 数据
   */
  private RetDataJsonVo data;
  private Boolean success = true;
  private Long timestamp = System.currentTimeMillis();


  public RetJson(int code, String msg, Object data, Boolean success, Date timestamp,
      Integer total) {
    this.code = code;
    this.msg = msg;
    this.data = RetDataJsonVo.builder().results(data).build();
    this.success = success;
    this.timestamp = System.currentTimeMillis();
  }

  /**
   * 执行成功
   */
  public static Object ok() {
    return new RetJson();
  }

  public static Object ok(Integer code, String msg) {
    return new RetJson(code, msg);
  }

  public static Object ok(Object data, Integer total, Integer current) {
    return new RetJson(data, total, current);
  }

  /**
   * 执行成功
   *
   * @param data 返回数据
   */
  public static Object ok(Object data) {
    return new RetJson(data);
  }

  /**
   * 执行失败
   *
   * @param code 返回码
   * @param msg  消息
   */
  public static RetJson err(int code, String msg) {
    return new RetJson(code, msg, false);
  }

  public static RetJson err(String msg) {
    return new RetJson(HttpStatus.METHOD_NOT_ALLOWED.value(), msg, false);
  }
  /**
   * 执行失败
   *
   * @param RetCode 返回码（枚举）
   */

  /**
   * 执行失败
   *
   * @param code 返回码
   * @param msg  消息
   * @param data 返回数据
   */
  public static Object err(int code, String msg, Object data) {
    return new RetJson(code, msg, data, false);
  }

  public RetJson() {
    this.code = HttpStatus.OK.value();
    this.msg = HttpStatus.OK.getReasonPhrase();
    this.success = Boolean.TRUE;
    this.timestamp = System.currentTimeMillis();
  }

  public RetJson(Object data) {
    this.code = HttpStatus.OK.value();
    this.msg = HttpStatus.OK.getReasonPhrase();
    this.data = RetDataJsonVo.builder().results(data).build();
    this.success = true;

  }

  public RetJson(int code, String msg) {
    this.code = code;
    this.msg = msg;
  }


  public RetJson(Object data, Integer total, Integer current) {
    this.code = HttpStatus.OK.value();
    this.msg = HttpStatus.OK.getReasonPhrase();

    this.data = RetDataJsonVo.builder().total(total).current(current).results(data).build();
    this.timestamp = System.currentTimeMillis();
    this.success = Boolean.TRUE;

  }


  public RetJson(Object data, Integer total) {
    this.code = HttpStatus.OK.value();
    this.msg = HttpStatus.OK.getReasonPhrase();

    this.data = RetDataJsonVo.builder().total(total).results(data).build();
    this.timestamp = System.currentTimeMillis();
    this.success = Boolean.TRUE;

  }

  public RetJson(int code, String msg, RetDataJsonVo data) {
    this.code = code;
    this.msg = msg;
    this.data = data;
  }

  public RetJson(int code, String msg, Object data, Boolean flag) {
    this.code = code;
    this.msg = msg;
    this.data = RetDataJsonVo.builder().results(data).build();
    this.success = flag;

  }

  public RetJson(int code, String msg, Boolean flag) {
    this.code = code;
    this.msg = msg;
    this.success = flag;
    this.timestamp = System.currentTimeMillis();
  }


}