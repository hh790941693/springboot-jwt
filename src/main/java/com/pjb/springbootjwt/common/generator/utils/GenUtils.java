package com.pjb.springbootjwt.common.generator.utils;

import com.pjb.springbootjwt.common.exception.ApplicationException;
import com.pjb.springbootjwt.common.generator.domain.ColumnDO;
import com.pjb.springbootjwt.common.generator.domain.TableDO;
import com.pjb.springbootjwt.common.generator.type.EnumGen;
import com.pjb.springbootjwt.common.utils.SpringContextHolder;
import com.pjb.springbootjwt.sys.domain.ConfigDO;
import com.pjb.springbootjwt.sys.service.impl.ConfigService;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 代码生成器 工具类.
 */
public class GenUtils {

    public static String STR_DELIMITER = "_";

    private static final Logger log = LoggerFactory.getLogger(GenUtils.class);

    /**
     * 生成代码.
     */
    public static void generatorCode(TableDO tableDO, List<ColumnDO> columnList, ZipOutputStream zip) {
        // 配置信息 读取表sys_config
        Map<String, String> config = getConfig();
        // 表名转换成Java类名 ws_users  --> WsUsers
        String className = tableToJava(tableDO.getTableName(), config.get("tablePrefix"), config.get("autoRemovePre"));
        //首字母大写类名
        tableDO.setClassName(className);
        //首字母小写类名
        tableDO.setClassname(StringUtils.uncapitalize(className));

        //各列类型列表
        Set<String> dataTypeList = new HashSet<>();
        //列名列表
        Set<String> columnNameList = new HashSet<>();
        List<String> baseColumnNames = Arrays.asList("deleted", "version", "createAt", "createBy", "updateAt", "updateBy");
        for (ColumnDO columnDO : columnList) {
            String columnName = columnDO.getColumnName();
            columnNameList.add(columnName);
            dataTypeList.add(columnDO.getDataType());
            if (baseColumnNames.contains(columnName)) {
                continue;
            }

            // 列名转换成Java属性名
            String attrName = columnToJava(columnName);
            columnDO.setAttrname(StringUtils.uncapitalize(attrName));   //字段名 首字母小写

            // 列的数据类型，转换成Java类型
            String attrType = config.get(columnDO.getDataType());     //字段类型 int、varchar
            columnDO.setAttrType(attrType);

            // 是否主键
            if ("PRI".equalsIgnoreCase(columnDO.getColumnKey()) && tableDO.getPk() == null) {
                tableDO.setPk(columnDO);
            }
        }
        tableDO.setColumnList(columnList);

        // 没主键，则第一个字段为主键
        if (tableDO.getPk() == null) {
            tableDO.setPk(tableDO.getColumnList().get(0));
        }

        // 设置velocity资源加载器
        Properties prop = new Properties();
        prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(prop);

        // 封装模板数据
        Map<String, Object> map = new HashMap<>(16);
        map.put("tableName", tableDO.getTableName());
        map.put("comments", tableDO.getComments());
        map.put("pk", tableDO.getPk());
        map.put("className", tableDO.getClassName());
        map.put("classname", tableDO.getClassname());
        String wholePackagePath = config.get("package");
        String basePackagePath = wholePackagePath.substring(wholePackagePath.lastIndexOf(".") + 1);
        map.put("package", wholePackagePath); // com.aaa.bbb.ccc
        map.put("pathName", basePackagePath); // ccc
        map.put("columnList", tableDO.getColumnList());
        map.put("author", config.get("author"));
        map.put("email", config.get("email"));
        map.put("datetime", DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN_19));
        // 字段特性
        map.put("hasBigDecimal", dataTypeList.contains("decimal"));
        map.put("hasDeleted", columnNameList.contains("deleted"));
        map.put("hasVersion", columnNameList.contains("version"));
        map.put("hasCreateAt", columnNameList.contains("createAt"));
        map.put("hasCreateBy", columnNameList.contains("createBy"));
        map.put("hasUpdateAt", columnNameList.contains("updateAt"));
        map.put("hasUpdateBy", columnNameList.contains("updateBy"));
        map.put("hasCreateTime", columnNameList.contains("create_time"));
        map.put("hasUpdateTime", columnNameList.contains("update_time"));
        map.put("hasDeleteTime", columnNameList.contains("delete_time"));
        VelocityContext context = new VelocityContext(map);

        // 获取模板列表
        List<String> templates = getTemplateList();
        for (String template : templates) {
            // 渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, "UTF-8");
            tpl.merge(context, sw);

            try {
                // 添加到zip
                String outputFilePath = getFileName(template, tableDO.getClassname(), tableDO.getClassName(), wholePackagePath, basePackagePath);
                if (StringUtils.isNotBlank(outputFilePath)) {
                    zip.putNextEntry(new ZipEntry(outputFilePath));
                    IOUtils.write(sw.toString(), zip, "UTF-8");
                    IOUtils.closeQuietly(sw);
                    zip.closeEntry();
                }
            } catch (IOException e) {
                log.warn(e.getMessage());
                log.info("渲染模板失败，表名：" + tableDO.getTableName());
                throw new ApplicationException("999", "生成代码失败");
            }
        }
    }

    /**
     * 去掉表名中的前缀(比如t_).
     */
    public static String tableToJava(String tableName, String tablePrefix, String autoRemovePre) {
        if ("true".equals(autoRemovePre) && (StringUtils.isNotBlank(tablePrefix))) {
            //去除表名前缀
            String pattern = "(^" + tablePrefix + ")(.*)";
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(tableName);
            boolean isFind = m.find();
            if (isFind) {
                tableName = m.group(2);
            }
        }
        return columnToJava(tableName);
    }

    /**
     * 列名转换成Java属性名.
     * 1.删除下划线_
     * 2.除第一个单词外,后面每个单词首字母大写
     */
    public static String columnToJava(String columnName) {
        if (columnName.contains(STR_DELIMITER)) {
            return WordUtils.capitalize(columnName, new char[] {'_'}).replace(STR_DELIMITER, "");
        } else {
            return WordUtils.capitalize(columnName);
        }
    }

    /**
     * 获取配置信息.
     */
    public static Map<String, String> getConfig() {
        ConfigService configService = SpringContextHolder.getBean(ConfigService.class);
        List<ConfigDO> list = configService.findListByKvType(EnumGen.KvType.base.getValue());
        list.addAll(configService.findListByKvType(EnumGen.KvType.mapping.getValue()));
        Map<String, String> config = new HashMap<>();
        list.stream().forEach(kv -> config.put(kv.getK(), kv.getV()));
        return config;
    }

    /**
     * 获取模板文件路径.
     * @return 模板文件路径列表
     */
    public static List<String> getTemplateList() {
        List<String> templates = new ArrayList<>();
        templates.add("templates/common/generator/domain.java.vm");
        templates.add("templates/common/generator/Dao.java.vm");
        templates.add("templates/common/generator/Mapper.xml.vm");
        templates.add("templates/common/generator/Service.java.vm");
        templates.add("templates/common/generator/ServiceImpl.java.vm");
        templates.add("templates/common/generator/Controller.java.vm");
        templates.add("templates/common/generator/list.html.vm");
        templates.add("templates/common/generator/add.html.vm");
        templates.add("templates/common/generator/edit.html.vm");
        templates.add("templates/common/generator/list.js.vm");
        templates.add("templates/common/generator/add.js.vm");
        templates.add("templates/common/generator/edit.js.vm");
        templates.add("templates/common/generator/menu.sql.vm");
        templates.add("templates/common/generator/form.html.vm");
        templates.add("templates/common/generator/form.js.vm");
        return templates;
    }

    /**
     * 获取文件名.
     */
    public static String getFileName(String template, String classname, String className, String wholePackageName, String basePackageName) {
        String javaPackagePath = "main" + File.separator + "java" + File.separator;
        if (StringUtils.isNotBlank(wholePackageName)) {
            javaPackagePath += wholePackageName.replace(".", File.separator) + File.separator;
        }
        String resourcePackagePath = "main" + File.separator + "resources" + File.separator;
        String mapperPackagePath = resourcePackagePath + "mapper" + File.separator + basePackageName + File.separator;
        String htmlPackagePath = resourcePackagePath + "templates" + File.separator + basePackageName + File.separator + classname + File.separator;
        String jsPackagePath = resourcePackagePath + "static" + File.separator + "js" + File.separator + "appjs" + File.separator + basePackageName + File.separator + classname + File.separator;

        // 后台java文件
        if (template.contains("domain.java.vm")) {
            return javaPackagePath + "domain" + File.separator + className + "DO.java";
        } else if (template.contains("Dao.java.vm")) {
            return javaPackagePath + "dao" + File.separator + className + "Dao.java";
        } else if (template.contains("Service.java.vm")) {
            return javaPackagePath + "service" + File.separator + className + "Service.java";
        } else if (template.contains("ServiceImpl.java.vm")) {
            return javaPackagePath + "service" + File.separator + "impl" + File.separator + className + "ServiceImpl.java";
        } else if (template.contains("Controller.java.vm")) {
            return javaPackagePath + "controller" + File.separator + className + "Controller.java";
        }

        // 前端html、js、文件
        if (template.contains("Mapper.xml.vm")) {
            return mapperPackagePath + className + "Mapper.xml";
        } else if (template.contains("list.html.vm")) {
            return htmlPackagePath + classname + ".html";
        } else if (template.contains("add.html.vm")) {
            return htmlPackagePath + classname + "Add.html";
        } else if (template.contains("edit.html.vm")) {
            return htmlPackagePath + classname + "Edit.html";
        } else if (template.contains("form.html.vm")) {
            return htmlPackagePath + classname + "Form.html";
        } else if (template.contains("list.js.vm")) {
            return jsPackagePath + classname + ".js";
        } else if (template.contains("add.js.vm")) {
            return jsPackagePath + classname + "Add.js";
        } else if (template.contains("edit.js.vm")) {
            return jsPackagePath + classname + "Edit.js";
        } else if (template.contains("form.js.vm")) {
            return jsPackagePath + classname + "Form.js";
        } else if (template.contains("menu.sql.vm")) {
            return className.toLowerCase() + "_menu.sql";
        }
        return null;
    }
}
