package com.jscxrz.zhddkk.util;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.DbType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

public class MpGenerator {

    /**
     * @param args
     */
    public static void main(String[] args) {
    	
    	//要生成的表名称  all表示生成所有的表
    	String generateTableName = "ws_operation_log";
    	
        AutoGenerator mpg = new AutoGenerator();
        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir("D://cache");//生成存放地址
        gc.setFileOverride(true);
        gc.setActiveRecord(true);
        gc.setEnableCache(false);// XML 二级缓存
        gc.setBaseResultMap(true);// XML ResultMap
        gc.setBaseColumnList(false);// XML columList
        gc.setAuthor("huangchaohui");

        // 自定义文件命名，注意 %s 会自动填充表实体属性！
        gc.setMapperName("%sDao");
        gc.setXmlName("%sMapper");
        gc.setServiceName("%sService");
        gc.setServiceImplName("%sServiceImpl");
        gc.setControllerName("%sController");
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL);
        dsc.setTypeConvert(new MySqlTypeConvert(){
            // 自定义数据库表字段类型转换【可选】
            @Override
            public DbColumnType processTypeConvert(String fieldType) {
                System.out.println("转换类型：" + fieldType);
                return super.processTypeConvert(fieldType);
            }
        });

        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("123456");
        dsc.setUrl("jdbc:mysql://127.0.0.1:3306/zhdd?characterEncoding=utf8");
        mpg.setDataSource(dsc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setTablePrefix(new String[] {""});// 此处可以修改为您的表前缀
        strategy.setNaming(NamingStrategy.underline_to_camel);// 表名生成策略
        //strategy.setInclude(new String[]{"sys_order"});//生成指定表
        strategy.setTablePrefix("t_");//去除表名前缀t_
        if (!generateTableName.equalsIgnoreCase("all")) {
        	strategy.setInclude(generateTableName);//指定要生成的表
        }
        mpg.setStrategy(strategy);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent("com.jscxrz");
        pc.setModuleName("zhddkk");
        pc.setEntity("entity");
        pc.setMapper("dao");
        pc.setXml("mapping");
        pc.setService("service");
        pc.setServiceImpl("service.impl");
        pc.setController("controller");
        mpg.setPackageInfo(pc);
        // 执行生成
        mpg.execute();
    }
}