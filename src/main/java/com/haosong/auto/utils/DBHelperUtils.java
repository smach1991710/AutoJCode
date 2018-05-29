package com.haosong.auto.utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DBHelperUtils
 * @author songhao
 * @since 2018-05-27
 */
public class DBHelperUtils {

    private static final Connection conn;
    private static final String driverClass = PropertiesHelper.getValueByKey("jdbc.driver");
    private static final String connectionUrl = PropertiesHelper.getValueByKey("jdbc.url");
    private static final String username = PropertiesHelper.getValueByKey("jdbc.username");
    private static final String password = PropertiesHelper.getValueByKey("jdbc.password");


    public static final String NAME = "NAME";
    public static final String REMARKS = "REMARKS";//表注释
    public static final String TYPE = "TYPE";//表的类型
    public static final String SIZE = "SIZE";//大小
    public static final String CLASS = "CLASS";//类别

    private static DBHelperUtils instance = null;
    /**
     * 定义代码块.
     */
    static {
        try {
            Class.forName(driverClass);
            conn = DriverManager.getConnection(connectionUrl, username, password);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**建立单例模式
     * Single
     * @return
     */
    public static DBHelperUtils getInstance() {
        if (instance == null) {
            synchronized (DBHelperUtils.class) {
                instance = new DBHelperUtils();
            }
        }
        return instance;
    }


    /**
     * 查询数据
     * @param sql
     * @param params
     * @return
     */
    public static ResultSet query(String sql, List<Object> params) {
        //System.out.println("sql: " + sql);
        try {
            PreparedStatement psmt = conn.prepareStatement(sql);
            if(params != null) {
                for (int i = 0; i < params.size(); i++) {
                    psmt.setObject(i+1, params.get(i));
                }
            }
            return psmt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /***
     * 更新
     * @param sql
     * @param params
     */
    public static void update(String sql, List<Object> params) {
        System.out.println("sql: " + sql);
        //System.out.println("params: " + params);
        try {
            PreparedStatement psmt = conn.prepareStatement(sql);
            if(params != null) {
                for (int i = 0; i < params.size(); i++) {
                    psmt.setObject(i+1, params.get(i));
                }
            }
            psmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取链接
     * @return
     */
    public static Connection getConnection(){
        try {
            Class.forName(driverClass);
            return DriverManager.getConnection(connectionUrl, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /***
     * 列名 类型 => 说明
     * TABLE_CAT String => 表 catalog
     * TABLE_SCHEM String => 表 schema
     * TABLE_NAME String => 表名
     * TABLE_TYPE String => 表类型
     * REMARKS String => 表注释
     * 获取表的列
     * @param table
     * @return
     * @throws Exception
     */
    public static List<Map<String, Object>> getCols(String table) throws Exception {
        List<Map<String, Object>> cols = new ArrayList<Map<String,Object>>();
        ResultSetMetaData md = query("select * from " + table + " where 1 = 2", null).getMetaData();

        for (int i = 1; i <= md.getColumnCount(); i++) {
            Map<String, Object> col = new HashMap<String, Object>();
            cols.add(col);
            col.put(NAME, md.getColumnName(i));
            col.put(CLASS, md.getColumnClassName(i));
            col.put(SIZE, md.getColumnDisplaySize(i));
            col.put(REMARKS, md.getColumnName(i));
            String _type = null;
            String type = md.getColumnTypeName(i);
            if(type.equals("INT")) {
                _type = "INTEGER";
            } else if(type.equals("DATETIME")) {
                _type = "TIMESTAMP";
            } else {
                _type = type;
            }
            col.put(TYPE, _type);
        }
        return cols;
    }


}
