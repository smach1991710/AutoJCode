package com.haosong.auto;

import com.haosong.auto.utils.PropertiesHelper;
import com.haosong.auto.utils.SqlGenerator;
import com.haosong.auto.utils.StringUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * 自动生成mvc代码
 * @author songhao
 * @since 2018-05-28
 */
public class CodeGenerator {

    static Logger logger = Logger.getLogger(CodeGenerator.class);

    //获取项目的根路径
    public static String PROJECT_PATH = PropertiesHelper.getValueByKey("projectPath");
    //实体的包名称
    public static String entityPackage = "com.haosong.auto.entity";
    //模板包的名称
    public static String templatePackage = "\\com\\haosong\\auto\\template\\";
    //表名称
    public static final String dataTitle = "t_";

    public static void main(String[] args) {
        String[] entityNames = {"User"};
        for(String name : entityNames){
            autoGenerator(name);
        }
    }

    /**
     * 开始执行自动生成代码
     * @param className
     */
    private static void autoGenerator(String className) {
        try{
            //生成实体对象
            Class.forName(entityPackage + "." + className);

            //生成实体文件
            writeEntity(className);

            //生成dao
            writeDao(className);

            //生成service
            writeService(className);

            //生成service的实现类
            writeServiceImpl(className);

            //生成controller类
            writeController(className);

            //生成xml文件
            writeXml(className);

            System.out.println("\n======生成的建表语句如下:======");
            System.out.println(SqlGenerator.generateSql(entityPackage + "." + className,"id"));
        }catch (Exception e){
            logger.error(e.getStackTrace(),e);
        }
    }

    /**
     * 拼接xml文件
     * @param className
     */
    private static void writeXml(String className) {
        String mybatisPath = StringUtils.getJavaPath() + templatePackage;
        try {
            String path = PROJECT_PATH + File.separator + PropertiesHelper.getValueByKey("rootPackage").replaceAll("\\.","/") + File.separator + "entity//mapper//";
            File file = new File(path);
            if(!file.exists()){
                file.mkdirs();
            }
            File fileBiz = new File(path + "\\" + className + ".xml");
            if (!fileBiz.exists()) {
                String template = "";
                FileReader fr = new FileReader(mybatisPath + "\\" + "Template.xml");

                BufferedReader br = new BufferedReader(fr);
                String s;
                while ((s = br.readLine()) != null) {
                    if(s.trim().startsWith("<mapper namespace")){
                        s = "<mapper namespace=\"" + PropertiesHelper.getValueByKey("rootPackage") + ".dao." + className + "Dao" + "\" >";
                    }
                    template += s + "\n";
                }
                fr.close();

                // 替换内容
                template = template.replaceAll("Template", className).replaceAll("template", StringUtils.toLower(className));

                // 获取实体属性，拼接数据库
                try {
                    Class clz = Class.forName(entityPackage + "." + className);
                    String fieldXml = ""; // 属性映射
                    String fieldStr = ""; // 字段集合

                    // 类所有属性
                    List<String> fieldList = new ArrayList<String>();
                    // 父类获取
                    fieldList.addAll(getSupperXmlByClass(clz));
                    // 本类获取
                    fieldList.addAll(getXmlByClass(clz));
                    // 拼接映射XML
                    fieldXml = getXmlByFieldList(fieldList);
                    // 替换属性映射XML
                    template = template.replaceFirst("<attribute/>", fieldXml);
                    // 拼接数据库字段集合
                    fieldStr = getDbNameListByFieldList(fieldList);
                    template = template.replaceFirst("<dbNameList/>", fieldStr);
                    // 拼接实体字段集合
                    fieldStr = getFieldNameListByFieldList(fieldList);
                    template = template.replaceFirst("<fieldNameList/>", fieldStr);
                    // 替换表名
                    fieldStr = dataTitle + StringUtils.getFieldName(className);
                    template = template.replaceAll("表名", fieldStr);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                FileWriter fw = new FileWriter(path + "\\" + className + ".xml");
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(template);
                bw.close();
            }
            logger.info("生成配置文件:[" + className + ".xml]成功" );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成Controller
     * @param className
     */
    private static void writeController(String className) {
        String controllerPath = StringUtils.getJavaPath() + templatePackage;
        try {
            String targetController = PROJECT_PATH + File.separator + PropertiesHelper.getValueByKey("rootPackage").replaceAll("\\.","/") + File.separator + "controller//";
            File file = new File(targetController);
            if(!file.exists()){
                file.mkdirs();
            }

            File fileBiz = new File(targetController + className + "Controller.java");
            if (!fileBiz.exists()) {
                String template = "";
                FileReader fr = new FileReader(controllerPath + "TemplateController.java");

                BufferedReader br = new BufferedReader(fr);
                String s;
                boolean started = false;
                while ((s = br.readLine()) != null) {
                    if(s.startsWith("package")){
                        //重新赋值
                        s = "package " + PropertiesHelper.getValueByKey("rootPackage") + ".controller;";
                    }
                    template += s + "\n";
                    if(!started){
                        if(s.startsWith("import")){
                            String entityClass = PropertiesHelper.getValueByKey("rootPackage") + ".entity." + className + ";";
                            template += "import " + entityClass + "\n";

                            String serviceClass = PropertiesHelper.getValueByKey("rootPackage") + ".service." + className + "Service;";
                            template += "import " + serviceClass + "\n";
                            started = true;
                        }
                    }
                }
                fr.close();

                // 替换内容
                template = template.replaceAll("Template", className).replaceAll("template", StringUtils.toLower(className));

                FileWriter fw = new FileWriter(targetController + className + "Controller.java");
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(template);
                bw.close();
            }
            logger.info("生成控制类:[" + className + "Controller]成功" );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成service的实现类
     * @param className
     */
    private static void writeServiceImpl(String className) {
        String serviceImplPath = StringUtils.getJavaPath() + templatePackage;
        try {
            String targetService = PROJECT_PATH + File.separator + PropertiesHelper.getValueByKey("rootPackage").replaceAll("\\.","/") + File.separator + "service//impl//";
            File file = new File(targetService);
            if(!file.exists()){
                file.mkdirs();
            }
            File fileBiz = new File(targetService + className + "ServiceImpl.java");
            if (!fileBiz.exists()) {
                String template = "";
                FileReader fr = new FileReader(serviceImplPath + "TemplateServiceImpl.java");

                BufferedReader br = new BufferedReader(fr);
                String s;
                boolean started = false;
                while ((s = br.readLine()) != null) {
                    if(s.startsWith("package")){
                        //重新赋值
                        s = "package " + PropertiesHelper.getValueByKey("rootPackage") + ".service.impl;";
                    }
                    template += s + "\n";
                    if(!started){
                        if(s.startsWith("import")){
                            String entityClass = PropertiesHelper.getValueByKey("rootPackage") + ".entity." + className + ";";
                            template += "import " + entityClass + "\n";

                            String daoClass = PropertiesHelper.getValueByKey("rootPackage") + ".dao." + className + "Dao;";
                            template += "import " + daoClass + "\n";

                            String serviceClass = PropertiesHelper.getValueByKey("rootPackage") + ".service." + className + "Service;";
                            template += "import " + serviceClass + "\n";
                            started = true;
                        }
                    }
                }
                fr.close();

                // 替换内容
                template = template.replaceAll("Template", className).replaceAll("template", className.toLowerCase());

                FileWriter fw = new FileWriter(targetService + className + "ServiceImpl.java");
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(template);
                bw.close();
            }
            logger.info("生成服务实现类:[" + className + "ServiceImpl]成功" );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成service服务类
     * @param className
     */
    private static void writeService(String className) {
        String servicePath = StringUtils.getJavaPath() + templatePackage;
        try {
            String targetService = PROJECT_PATH + File.separator + PropertiesHelper.getValueByKey("rootPackage").replaceAll("\\.","/") + File.separator + "service//";
            File file = new File(targetService);
            if(!file.exists()){
                file.mkdirs();
            }

            File fileBiz = new File(targetService + className + "Service.java");
            if (!fileBiz.exists()) {
                String template = "";
                FileReader fr = new FileReader(servicePath + "TemplateService.java");

                BufferedReader br = new BufferedReader(fr);
                String s;
                boolean started = false;
                while ((s = br.readLine()) != null) {
                    if(s.startsWith("package")){
                        //重新赋值
                        s = "package " + PropertiesHelper.getValueByKey("rootPackage") + ".service;";
                    }
                    template += s + "\n";
                    if(!started){
                        if(s.startsWith("import")){
                            String entityClass = PropertiesHelper.getValueByKey("rootPackage") + ".entity." + className + ";";
                            template += "import " + entityClass + "\n";
                            started = true;
                        }
                    }
                }
                fr.close();

                // 替换内容
                template = template.replaceAll("Template", className).replaceAll("template", className);

                FileWriter fw = new FileWriter(targetService + className + "Service.java");
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(template);
                bw.close();
            }
            logger.info("生成服务类:[" + className + "Service]成功" );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成实体对象
     * @param className
     */
    private static void writeEntity(String className){
        String entityPath = StringUtils.getJavaPath() + entityPackage.replaceAll("\\.","/") + "//";
        try{
            String targetEntity = PROJECT_PATH + File.separator + PropertiesHelper.getValueByKey("rootPackage").replaceAll("\\.","/") + File.separator + "entity//";
            File file = new File(targetEntity);
            if(!file.exists()){
                file.mkdirs();
            }

            File fileAction = new File(targetEntity + className + ".java");
            if (!fileAction.exists()) {
                String template = "";
                FileReader fr = new FileReader(entityPath + className + ".java");

                BufferedReader br = new BufferedReader(fr);
                String s;
                while ((s = br.readLine()) != null) {
                    if(s.startsWith("package")){
                        //重新赋值
                        s = "package " + PropertiesHelper.getValueByKey("rootPackage") + ".entity;";
                    }
                    template += s + "\n";
                }
                fr.close();

                FileWriter fw = new FileWriter(targetEntity + className + ".java");
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(template);
                bw.close();
            }
            logger.info("生成实体对象:[" + className + "]成功" );
        }catch (Exception e){
            logger.error(e.getStackTrace(),e);
        }
    }

    /**
     * 生成dao代码
     * @param className
     */
    private static void writeDao(String className) {
        String daoPath = StringUtils.getJavaPath() + templatePackage;
        try {
            String targetDao = PROJECT_PATH + File.separator + PropertiesHelper.getValueByKey("rootPackage").replaceAll("\\.","/") + File.separator + "dao//";
            File file = new File(targetDao);
            if(!file.exists()){
                file.mkdirs();
            }
            File fileAction = new File(targetDao + className + "Dao.java");
            if (!fileAction.exists()) {
                String template = "";
                FileReader fr = new FileReader(daoPath + "TemplateDao.java");

                BufferedReader br = new BufferedReader(fr);
                String s;
                boolean started = false;
                while ((s = br.readLine()) != null) {
                    if(s.startsWith("package")){
                        //重新赋值
                        s = "package " + PropertiesHelper.getValueByKey("rootPackage") + ".dao;";
                    }
                    if(!started){
                        if(s.startsWith("import")){
                            String entityClass = PropertiesHelper.getValueByKey("rootPackage") + ".entity." + className + ";";
                            template += "import " + entityClass + "\n";
                            started = true;
                        }
                    }
                    template += s + "\n";
                }
                fr.close();

                // 替换内容
                template = template.replaceAll("Template", className).replaceAll("template", className);

                FileWriter fw = new FileWriter(targetDao + className + "Dao.java");
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(template);
                bw.close();
            }
            logger.info("生成dao类:[" + className + "Dao]成功" );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取父类的属性拼接xml
     * @param clz
     * @return
     */
    private static List<String> getSupperXmlByClass(Class clz) {
        List<String> list = new ArrayList<String>();
        Class supClz = clz.getSuperclass();
        if (supClz != null) {
            list.addAll(getSupperXmlByClass(supClz));
            list.addAll(getXmlByClass(supClz));
        }
        return list;
    }

    /**
     * 获取类的属性拼接xml
     * @param clz
     * @return
     */
    private static List<String> getXmlByClass(Class clz) {
        List<String> list = new ArrayList<String>();
        Field[] fields = clz.getDeclaredFields();
        for (Field field : fields) {// --for() begin
            boolean isStatic = Modifier.isStatic(field.getModifiers());
            if(!isStatic){
                String name = field.getName(); // 字段名
                if (!StringUtils.isNull(name)) {
                    list.add(name);
                }
            }
        }
        return list;
    }

    /**
     * 获取类的属性拼接xml
     * @param fieldList
     * @return
     */
    private static String getXmlByFieldList(List<String> fieldList) {
        String fieldXml = "";
        for (String name : fieldList) {// --for() begin
            if ("serialVersionUID".equals(name)) {
                continue;
            }
            fieldXml += "\t\t<result column=\"" + StringUtils.getFieldName(name) + "\" property=\"" + name + "\" />\n";
        }
        return fieldXml;
    }

    /**
     * 拼接数据字段集合
     * @param fieldList
     * @return
     */
    private static String getDbNameListByFieldList(List<String> fieldList) {
        String fieldStr = "";
        for (String name : fieldList) {// --for() begin
            if ("serialVersionUID".equals(name) || "id".equals(name)) {
                continue;
            }
            fieldStr += "`" + StringUtils.getFieldName(name) + "`,";
        }
        if (fieldStr.length() > 0) {
            fieldStr = fieldStr.substring(0, fieldStr.length() - 1);
        }
        return fieldStr;
    }

    /**
     * 拼接实体字段集合
     * @param fieldList
     * @return
     */
    private static String getFieldNameListByFieldList(List<String> fieldList) {
        String fieldStr = "";
        for (String name : fieldList) {// --for() begin
            if ("serialVersionUID".equals(name) || "id".equals(name)) {
                continue;
            }
            fieldStr += "\t\t\t#{" + name + "},\n";
        }
        if (fieldStr.length() > 0) {
            fieldStr = fieldStr.substring(0, fieldStr.length() - 2);
        }
        return fieldStr;
    }
}
