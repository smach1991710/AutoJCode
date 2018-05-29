package com.haosong.auto.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 规则的英文
 * @author songhao
 * @since 2018-05-27
 */
public class RuleUtils {

    //mysql类型映射
    static Map<String,String> mySqlTypeMap = new HashMap<String,String>();
    //表名称前缀
    static String tableTitle = "t_";

    static{
        mySqlTypeMap.put("INTEGER","java.lang.Integer");
        mySqlTypeMap.put("TINYINT","java.lang.Integer");
        mySqlTypeMap.put("MALLINT","java.lang.Integer");
        mySqlTypeMap.put("MEDIUMINT","java.lang.Integer");
        mySqlTypeMap.put("BIGINT","java.lang.Long");
        mySqlTypeMap.put("DOUBLE","java.lang.Double");
        mySqlTypeMap.put("FLOAT","java.lang.Float");
        mySqlTypeMap.put("CHAR","java.lang.String");
        mySqlTypeMap.put("VARCHAR","java.lang.String");
        mySqlTypeMap.put("DATE","java.util.Date");
        mySqlTypeMap.put("DATETIME","java.util.Date");
        mySqlTypeMap.put("TIMESTAMP","java.util.Date");
    }

    public static void main(String[] args) {
        System.out.println(getBeanNameByTable("t_user"));
    }


    /**
     * 根据一条完整的包路径，取对应的类名称
     * @return
     */
    public static String getClassNameByType(String packagepath){
        String clazz = packagepath;
        int index = packagepath.lastIndexOf(".");
        if(index >= 0){
            clazz = packagepath.substring(index + 1,packagepath.length());
        }
        return clazz;
    }

    /**
     * 根据Mysql类型获取java类型
     * @param mysqlType
     * @return
     */
    public static String getJavaTypeByMysql(String mysqlType) {
        return mySqlTypeMap.get(mysqlType);
    }

    /**
     * 通过bean名称转换成表名称
     * @param beanName
     * @return
     */
    public static String getTableNameByBean(String beanName){
        String tableName = tableTitle + StringUtils.toLower(beanName);
        return tableName;
    }

    /**
     * 通过表名称转换成类名称
     * @param tableName 表名称，类似t_user
     * @return
     */
    public static String getBeanNameByTable(String tableName){
        if(StringUtils.isNull(tableName)){
            return tableName;
        }
        int index = tableName.indexOf("_");
        int length = tableName.length();
        tableName = tableName.substring(index + 1,length);
        tableName = StringUtils.upperFirst(tableName);
        return tableName;
    }
}
