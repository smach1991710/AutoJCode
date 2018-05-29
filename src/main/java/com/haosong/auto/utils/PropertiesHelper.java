package com.haosong.auto.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * PropertiesHelper
 * @since 2018-05-27
 * @author songhao
 */
public class PropertiesHelper {

    private static final Map<String, String> properties = new HashMap<String, String>();
    static {
        try {
            Properties pps = new Properties();
            pps.load(PropertiesHelper.class.getClassLoader().getResourceAsStream("prop/auto.properties"));
            //处理重复的值.
            for (Map.Entry<Object, Object> entry : pps.entrySet()) {
                properties.put(entry.getKey().toString().trim(), entry.getValue().toString().trim());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *通过key值去获取值.
     */
    public static String getValueByKey(String name) {
        return properties.get(name);
    }
}
