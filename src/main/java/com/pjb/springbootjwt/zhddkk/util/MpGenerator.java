package com.pjb.springbootjwt.zhddkk.util;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.DbType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

public class MpGenerator {

    public static void main(String[] args) {

        AutoGenerator gen = new AutoGenerator();

        // 数据库配置
        String driverName = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/zhdd?serverTimezone=GMT&2b8&useSSL=false";
        String username = "root";
        String password = "123456";
        gen.setDataSource(new DataSourceConfig()
                .setDbType(DbType.MYSQL)
                .setDriverName(driverName)
                .setUrl(url)
                .setUsername(username)
                .setPassword(password));

        // 全局配置
        String projectPath = "E:\\cache";

        String authorName ="hch";
        gen.setGlobalConfig(new GlobalConfig()
                .setOutputDir(projectPath + "/src/main/java") // 输出目录
                .setFileOverride(true) // 是否覆盖文件
                .setBaseResultMap(true) // XML ResultMap
                .setBaseColumnList(true) // XML columnList
                .setOpen(true) //生成后打开文件夹
                .setAuthor(authorName)
                .setMapperName("%sDao")
                .setXmlName("%sMapper")
                .setServiceName("%sService")
                .setServiceImplName("%sServiceImpl")
                .setControllerName("%sController")
        );

        // 策略配置
        String prefix = "t_";
        // 要生成的表名, 为空时默认指定库的所有表
        String[] tables = {"sp_goods"};
        gen.setStrategy(new StrategyConfig()
                .setTablePrefix(prefix) // 表前缀
                .setNaming(NamingStrategy.underline_to_camel) // 表名生成策略
                .setInclude(tables) // 需要生成的表
                .setRestControllerStyle(true)
                .setEntityLombokModel(true) // lombok 模型
                .setLogicDeleteFieldName("is_deleted") // 逻辑删除字段名
                .setEntityBooleanColumnRemoveIsPrefix(true) // 去掉布尔值的is_前缀
        );

        // 包配置
        gen.setPackageInfo(new PackageConfig()
                .setParent("com.pjb.springboot.shop")
                .setController("controller")
                .setEntity("domain")
                .setMapper("dao")
                .setService("service")
                .setServiceImpl("service.impl")
                .setXml("mapper")
        );

        // 执行生成
        gen.execute();
    }
}