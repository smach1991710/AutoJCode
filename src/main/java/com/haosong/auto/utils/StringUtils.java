package com.haosong.auto.utils;

import java.io.File;

/**
 * 字符串的工具类
 * @author songhao
 * @since 2018-05-27
 */
public class StringUtils {

    /**
     * 获取数据库表字段名称
     *
     * @author dingxingang
     * @param fName
     * @return
     */
    public static String getFieldName(String fName) {
        String fieldName = "";
        char[] ns = fName.toCharArray();
        for (char n : ns) {
            if (Character.isLowerCase(n)) {
                fieldName += n;
            } else {
                String s = n + "";
                if(fieldName.length()>0){
                    fieldName += "_" + s.toLowerCase();
                } else {
                    fieldName += s.toLowerCase();
                }
            }
        }
        return fieldName;
    }

    /**
     * 转小写（第一个字符转小写)
     * @param str
     * @return
     */
    public static String toLower(String str) {
        String sNum = "";
        sNum = str.substring(0, 1).toLowerCase();
        sNum = sNum + str.substring(1);
        return sNum;
    }

    /**
     * 获取java项目的路径
     * @return
     */
    public static String getJavaPath(){
        String path = System.getProperty("user.dir") + "//src//main//java//";
        return path;
    }

    /**
     * 获取resource路径
     * @return
     */
    public static String getResourcePath(){
        String path = System.getProperty("user.dir") + "//src//main//resources//";
        return path;
    }

    /**
     * 判断字符串是否为空，为空就返回true，否则就返回false
     * @param value
     * @return
     */
    public static boolean isNull(String value){
        if(value == null || "".equals(value.trim()) || "null".equalsIgnoreCase(value.trim())){
            return true;
        }
        return false;
    }

    /**
     * 判断字符串是否为空，为空就返回false，否则就返回true
     * @param value
     * @return
     */
    public static boolean isNotNull(String value){
        return !isNull(value);
    }

    /**
     * 对字符串进行join处理
     * @param value1
     * @param value2
     * @param join
     * @return
     */
    public static String join(String value1,String value2,String join){
        StringBuffer buffer = new StringBuffer();
        if(StringUtils.isNotNull(value1)){
            buffer.append(value1);
        }
        buffer.append(".");
        if(StringUtils.isNotNull(value2)){
            buffer.append(value2);
        }
        return buffer.toString();
    }

    /**
     * 将字符串的第一个字母改成大写的
     * @param value
     * @return
     */
    public static String upperFirst(String value){
        if(StringUtils.isNull(value)){
            return value;
        }
        int length = value.length();
        StringBuffer buffer = new StringBuffer();
        if(length > 0){
            String first = value.substring(0,1).toUpperCase();
            buffer.append(first);
            buffer.append(value.substring(1,length));
        }
        return buffer.toString();
    }

    public static void main(String[] args) {
        System.out.println(upperFirst("bl"));
    }
}
