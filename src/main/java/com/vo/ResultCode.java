package com.vo;


//状态码返回类
public interface ResultCode {
    //接口中只能定义常量    public static final可省略,且常量字母大写
    public static final int OK = 200;//成功
    public static final int ERROR = 500;//失败
    //数据未删除，可被显示
    public static final Integer NOT_DELETE=0;
    //数据已删除，不能被显示
    public static final Integer DELETED=1;
}
