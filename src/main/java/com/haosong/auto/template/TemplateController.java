package com.haosong.auto.template;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 模板Controller文件
 * @author songhao
 * @since 2018-05-28
 */
@Controller
@RequestMapping("/template")
public class TemplateController {

    static Logger logger = Logger.getLogger(TemplateController.class);

    @Autowired
    private TemplateService templateService;

    /**
     * 跳转列表页面
     * @param model
     * @return
     */
    @RequestMapping(value = "/toList")
    public String toList(ModelMap model) {
        return "template/templateList";
    }

    /**
     * 列表
     *
     * @param template
     * @param page
     * @param size
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/templateList", method = RequestMethod.POST)
    public Object templateList(Template template, HttpServletRequest request,
                               @RequestParam(value = "page", defaultValue = "1") int page,
                               @RequestParam(value = "rows", defaultValue = "20") int size,
                               @RequestParam(value = "sort", defaultValue = "id") String sort,
                               @RequestParam(value = "order", defaultValue = "desc") String order) throws Exception {
        // 查询条件
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("template", template);
        // 查询总数
        long total = templateService.findTemplateCount(param);
        int from = (page - 1) * size; // 计算开始条数
        // 查询数据
        param.put("from", from);
        param.put("size", size);
        param.put("sort", sort);
        param.put("order", order);
        List<Template> list = templateService.findTemplate(param);
        // 返回信息
        Map<String, Object> rev = new HashMap<String, Object>();
        rev.put("total", total);
        rev.put("rows", list);
        return rev;
    }

    /**
     * 跳转详细页面
     *
     * @author dingxingang
     * @param model
     * @param id
     * @return
     */
    @RequestMapping(value = "/toEdit")
    public String toEdit(ModelMap model, Long id) {
        if (id != null) {
            Template template = templateService.getTemplateById(id);
            model.put("template", template);
        }
        return "template/templateEdit";
    }

    /**
     * 修改
     *
     * @author dingxingang
     * @param template
     * @param modelMap
     * @return
     */
    @RequestMapping("updateTemplate")
    public String updateTemplate(Template template, ModelMap modelMap) {
        if (template != null) {
            if (template.getId() != null) {
                templateService.updateTemplate(template);// 修改
            } else {
                templateService.addTemplate(template);// 添加
            }
        }
        modelMap.put("message", "操作成功");
        modelMap.put("template", template);
        return "template/templateEdit";
    }

}
