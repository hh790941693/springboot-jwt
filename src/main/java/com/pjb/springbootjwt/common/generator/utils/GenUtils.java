package com.pjb.springbootjwt.common.generator.utils;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.pjb.springbootjwt.common.exception.ApplicationException;
import com.pjb.springbootjwt.common.generator.domain.ColumnDO;
import com.pjb.springbootjwt.common.generator.domain.TableDO;
import com.pjb.springbootjwt.common.generator.type.EnumGen;
import com.pjb.springbootjwt.common.utils.SpringContextHolder;
import com.pjb.springbootjwt.sys.domain.ConfigDO;
import com.pjb.springbootjwt.sys.service.impl.ConfigService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 代码生成器 工具类
 */
public class GenUtils {

    public static String STR_DELIMITER = "_";

    private static Logger log = LoggerFactory.getLogger(GenUtils.class);

    /**
     * 生成代码
     */
    public static void generatorCode(TableDO tableDO, List<ColumnDO> columnList, ZipOutputStream zip) {
        // 配置信息 读取表sys_config
        Map<String, String> config = getConfig();
        // 表名转换成Java类名
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
            if(baseColumnNames.contains(columnName)) {
                continue;
            }

            // 列名转换成Java属性名
            String attrName = columnToJava(columnName);
            columnDO.setAttrName(attrName);
            columnDO.setAttrname(StringUtils.uncapitalize(attrName));

            // 列的数据类型，转换成Java类型
            String attrType = config.get(columnDO.getDataType());
            columnDO.setAttrType(attrType);

            // 是否主键
            if ("PRI".equalsIgnoreCase(columnDO.getColumnKey()) && tableDO.getPk() == null) {
                tableDO.setPk(columnDO);
            }
        }
        tableDO.setColumns(columnList);

        // 没主键，则第一个字段为主键
        if (tableDO.getPk() == null) {
            tableDO.setPk(tableDO.getColumns().get(0));
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
        String pack = config.get("package");
        map.put("pathName", pack.substring(pack.lastIndexOf(".") + 1));
        map.put("columns", tableDO.getColumns());
        map.put("package", pack);
        map.put("author", config.get("author"));
        map.put("email", config.get("email"));
        map.put("datetime", DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN_19));
        // 字段特性
        map.put("hasBigDecimal", dataTypeList.contains("decimal"));
        //时间类型的包含3种，date、datetime、timestamp
        if (dataTypeList.contains("date") || dataTypeList.contains("datetime") || dataTypeList.contains("timestamp")) {
            map.put("hasDatetime", 1);
        }
        map.put("hasDeleted", columnNameList.contains("deleted"));
        map.put("hasVersion", columnNameList.contains("version"));
        map.put("hasCreateAt", columnNameList.contains("createAt"));
        map.put("hasCreateBy", columnNameList.contains("createBy"));
        map.put("hasUpdateAt", columnNameList.contains("updateAt"));
        map.put("hasUpdateBy", columnNameList.contains("updateBy"));
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
                String outputFilePath =getFileName(template, tableDO.getClassname(), tableDO.getClassName(),
                        pack.substring(pack.lastIndexOf(".") + 1));
                zip.putNextEntry(new ZipEntry(outputFilePath));
                IOUtils.write(sw.toString(), zip, "UTF-8");
                IOUtils.closeQuietly(sw);
                zip.closeEntry();
            } catch (IOException e) {
                log.warn(e.getMessage());
                log.info("渲染模板失败，表名：" + tableDO.getTableName());
                throw new ApplicationException("999","生成代码失败");
            }
        }
    }

    /**
     * 表名转换成Java类名
     */
    public static String tableToJava(String tableName, String tablePrefix, String autoRemovePre) {

        if ("true".equals(autoRemovePre)) {
            //tableName = tableName.substring(tableName.indexOf(STR_DELIMITER) + 1);
            if (StringUtils.isNotBlank(tablePrefix)) {
                //去除表名前缀
                String pattern = "(^" + tablePrefix + ")(.*)";
                Pattern r = Pattern.compile(pattern);
                Matcher m = r.matcher(tableName);
                boolean isFind = m.find();
                if (isFind) {
                    tableName = m.group(2);
                }
            }
        }
        return columnToJava(tableName);
    }

    /**
     * 列名转换成Java属性名
     */
    public static String columnToJava(String columnName) {
        if(columnName.contains(STR_DELIMITER)){
            return WordUtils.capitalize(columnName, new char[] {'_'}).replace(STR_DELIMITER, "");
        }else {
            return WordUtils.capitalize(columnName);
        }
    }

    /**
     * 获取配置信息
     */
    public static Map<String, String> getConfig() {
        ConfigService configService = SpringContextHolder.getBean(ConfigService.class);
        List<ConfigDO> list = configService.findListByKvType(EnumGen.KvType.base.getValue());
        list.addAll(configService.findListByKvType(EnumGen.KvType.mapping.getValue()));
        Map<String, String> config = new HashMap<>();
        list.stream().forEach(kv -> config.put(kv.getK(), kv.getV()));
        return config;
    }

    public static List<String> getTemplateList() {
        List<String> templates = new ArrayList<String>();
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
     * 获取文件名
     */
    public static String getFileName(String template, String classname, String className, String packageName) {
        String packagePath = "main" + File.separator + "java" + File.separator;
        if (StringUtils.isNotBlank(packageName)) {
            packagePath += packageName.replace(".", File.separator) + File.separator;
        }

        if (template.contains("domain.java.vm")) {
            return packagePath + "domain" + File.separator + className + "DO.java";
        }else if (template.contains("Dao.java.vm")) {
            return packagePath + "dao" + File.separator + className + "Dao.java";
        }else if (template.contains("Service.java.vm")) {
            return packagePath + "service" + File.separator + className + "Service.java";
        }else if (template.contains("ServiceImpl.java.vm")) {
            return packagePath + "service" + File.separator + "impl" + File.separator + className + "ServiceImpl.java";
        }else if (template.contains("Controller.java.vm")) {
            return packagePath + "controller" + File.separator + className + "Controller.java";
        }else if (template.contains("Mapper.xml.vm")) {
            return "main" + File.separator + "resources" + File.separator + "mapper" + File.separator + packageName
                    + File.separator + className + "Mapper.xml";
        }else if (template.contains("list.html.vm")) {
            return "main" + File.separator + "resources" + File.separator + "templates" + File.separator + packageName
                    + File.separator + classname + File.separator + classname + ".html";
        }else if (template.contains("add.html.vm")) {
            return "main" + File.separator + "resources" + File.separator + "templates" + File.separator + packageName
                    + File.separator + classname + File.separator + "add.html";
        }else if (template.contains("edit.html.vm")) {
            return "main" + File.separator + "resources" + File.separator + "templates" + File.separator + packageName
                    + File.separator + classname + File.separator + classname + "edit.html";
        }else if (template.contains("form.html.vm")) {
            return "main" + File.separator + "resources" + File.separator + "templates" + File.separator + packageName
                    + File.separator + classname + File.separator + classname + "Form.html";
        }else if (template.contains("list.js.vm")) {
            return "main" + File.separator + "resources" + File.separator + "static" + File.separator + "js"
                    + File.separator + "appjs" + File.separator + packageName + File.separator + classname
                    + File.separator + classname + ".js";
        }else if (template.contains("add.js.vm")) {
            return "main" + File.separator + "resources" + File.separator + "static" + File.separator + "js"
                    + File.separator + "appjs" + File.separator + packageName + File.separator + classname
                    + File.separator + "add.js";
        }else if (template.contains("edit.js.vm")) {
            return "main" + File.separator + "resources" + File.separator + "static" + File.separator + "js"
                    + File.separator + "appjs" + File.separator + packageName + File.separator + classname
                    + File.separator + "edit.js";
        }else if (template.contains("form.js.vm")) {
            return "main" + File.separator + "resources" + File.separator + "static" + File.separator + "js"
                    + File.separator + "appjs" + File.separator + packageName + File.separator + classname
                    + File.separator + classname +"Form.js";
        }else if (template.contains("menu.sql.vm")) {
            return className.toLowerCase() + "_menu.sql";
        }

        return null;
    }
}
