package com.haosong.auto.template;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * 模板文件service的实现类
 * @author songhao
 * @since 2018-05-28
 */
public class TemplateServiceImpl implements TemplateService {

    static Logger logger = Logger.getLogger(TemplateServiceImpl.class);

    @Autowired
    private TemplateDao templateDao;

    /**
     * 添加
     */
    public void addTemplate(Template template) {
        templateDao.addTemplate(template);
    }

    /**
     * 获取
     */
    public Template getTemplateById(Long id) {
        return templateDao.getTemplateById(id);
    }

    /**
     * 查询总记录数
     */
    public long findTemplateCount(Map<String, Object> param) {
        return templateDao.findTemplateCount(param);
    }

    /**
     * 查询列表
     */
    public List<Template> findTemplate(Map<String, Object> param) {
        return templateDao.findTemplate(param);
    }

    /**
     * 修改
     */
    public void updateTemplate(Template template) {
        templateDao.updateTemplate(template);
    }
}

