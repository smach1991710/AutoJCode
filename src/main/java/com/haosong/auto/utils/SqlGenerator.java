package com.haosong.auto.utils;

import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * SqlGenerator
 * @author songhao
 * @since 2018-05-29
 */
public class SqlGenerator {

    static Logger logger = Logger.getLogger(SqlGenerator.class);

    public static void main(String[] args) {
        String className = "com.haosong.auto.entity.User";
        System.out.println(generateSql(className,"id"));
    }

    /**
     * 根据实体类生成建表语句
     * @author
     * @param className 全类名
     */
    public static String generateSql(String className,String indexKey){
        try {
            Class<?> clz = Class.forName(className);
            className = clz.getSimpleName();
            Field[] fields = clz.getDeclaredFields();
            StringBuffer column = new StringBuffer();
            String varchar = " varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,";
            for (Field f : fields) {
                boolean isStatic = Modifier.isStatic(f.getModifiers());
                if(!isStatic) {
                    if (indexKey.equals(f.getName())) {
                        column.append("\n\t`" + f.getName() + "` int(11) NOT NULL AUTO_INCREMENT,");
                    } else {
                        Class type = f.getType();
                        if (type == String.class) {
                            column.append("\n\t`" + f.getName() + "`" + varchar);
                        }
                    }
                }
            }
            StringBuffer sql = new StringBuffer();
            sql.append("DROP TABLE IF EXISTS `"+ RuleUtils.getTableNameByBean(className) + "`; ")
                    .append("\nCREATE TABLE `"+ RuleUtils.getTableNameByBean(className) + "`(")
                    .append(column)
                    .append(" \n\t PRIMARY KEY (`" + indexKey + "`) USING BTREE")
                    .append("\n) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci;");
            return sql.toString();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            logger.debug("该类未找到！");
            return null;
        }
    }
}
