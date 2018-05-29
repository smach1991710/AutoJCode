package com.haosong.auto.template;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 模板文件service
 * @author songhao
 * @since 2018-05-28
 */
@Service
public interface TemplateService {

    /**
     * 添加
     * @param template
     */
    public void addTemplate(Template template);

    /**
     * 修改
     * @param template
     */
    public void updateTemplate(Template template);

    /**
     * 获取
     * @param id
     * @return
     */
    public Template getTemplateById(Long id);

    /**
     * 获取总记录数
     * @return
     */
    public long findTemplateCount(Map<String, Object> param);

    /**
     * 查询记录列表
     * @param param
     * @return
     */
    public List<Template> findTemplate(Map<String, Object> param);

}
