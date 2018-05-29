package com.haosong.auto.base;

/**
 * 一些常量配置信息
 * @author songhao
 * @since 2018-05-27
 */
public class CodeConsts {

    //访问权限类型
    public static final String ACCESS_TYPE_PRIVATE = "private";
    public static final String ACCESS_TYPE_PROTECTED = "protected";
    public static final String ACCESS_TYPE_PUBLIC = "public";

    //返回值类型为void
    public static final String RESULT_TYPE_VOID = "void";
    public static final String CLASS_TYPE = "class";
    public static final String STATIC_TYPE = "static";
    public static final String FINAL_TYPE = "final";
    public static final String PACKAGE_TYPE = "package";

    //代码部分格式处理
    public static final String ENTER = "\n";//换行符
    public static final String TAB = "    ";//tab键

    //定义各个部分的包名称
    public static final String ENTITY = "entity";
    public static final String CONTROLLER = "controller";
    public static final String SERVICE = "service";
    public static final String SERVICEIMPL = "service.impl";
    public static final String DAO = "dao";
    public static final String mapper = "entity.mapper";
}
