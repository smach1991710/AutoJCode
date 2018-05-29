package com.haosong.auto.template;

/**
 * 模板实体文件
 * @author songhao
 * @since 2018-05-28
 */
public class Template {

    private Integer id;
    private String code; // 账户编号
    private String name; // 账户名称

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
