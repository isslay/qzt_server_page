package com.qzt.ump.generator;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.DbType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.xiaoleilu.hutool.io.FileUtil;
import com.xiaoleilu.hutool.setting.dialect.Props;
import com.xiaoleilu.hutool.util.CharsetUtil;
import com.xiaoleilu.hutool.util.StrUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MybatisPlus代码生成工具
 *
 * @author cgw
 * @date 2017-12-11
 **/
public class UmpGeneratorUtil {

    /**
     * 根据配置文件执行生成
     *
     * @param propertiesFilePath properties配置文件绝对路径
     * @author cgw
     * @date 2017-12-11 20:04
     */
    public void generator(String propertiesFilePath) {
        Props props = new Props(FileUtil.touch(propertiesFilePath), CharsetUtil.UTF_8);
        AutoGenerator mpg = new AutoGenerator();
        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(props.getStr("global.outputdir"));
        gc.setFileOverride(true);
        // 不需要ActiveRecord特性的请改为false
        gc.setActiveRecord(false);
        // XML 二级缓存
        gc.setEnableCache(false);
        // XML ResultMap
        gc.setBaseResultMap(true);
        // XML columList
        gc.setBaseColumnList(false);
        // .setKotlin(true) 是否生成 kotlin 代码
        gc.setAuthor(props.getStr("global.author"));
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL);
        dsc.setTypeConvert(new MySqlTypeConvert() {
            // 自定义数据库表字段类型转换【可选】
            @Override
            public DbColumnType processTypeConvert(String fieldType) {
                System.out.println("转换类型：" + fieldType);
                // 注意！！processTypeConvert 存在默认类型转换，如果不是你要的效果请自定义返回、非如下直接返回。
                return super.processTypeConvert(fieldType);
            }
        });
        dsc.setDriverName(props.getStr("datasource.drivername"));
        dsc.setUsername(props.getStr("datasource.username"));
        dsc.setPassword(props.getStr("datasource.password"));
        dsc.setUrl(props.getStr("datasource.url"));
        mpg.setDataSource(dsc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        // 此处可以修改为您的表前缀
        strategy.setTablePrefix(props.getStr("strategy.tableprefix"));
        // 表名生成策略
        strategy.setNaming(NamingStrategy.underline_to_camel);
        // 需要生成的表
        strategy.setInclude(StrUtil.split(props.getStr("strategy.include"), ","));
        // 排除生成的表
        strategy.setExclude(StrUtil.split(props.getStr("strategy.exclude"), ","));
        // 自定义实体父类
        strategy.setSuperEntityClass(props.getStr("strategy.superentityclass"));
        // 自定义实体，公共字段
        strategy.setSuperEntityColumns(StrUtil.split(props.getStr("strategy.superentitycolumns"), ","));
        // 自定义 mapper 父类
        strategy.setSuperMapperClass(props.getStr("strategy.supermapperclass"));
        // 自定义 service 父类
        strategy.setSuperServiceClass(props.getStr("strategy.superserviceclass"));
        // 自定义 service 实现类父类
        strategy.setSuperServiceImplClass(props.getStr("strategy.superserviceimplclass"));
        // 自定义 controller 父类
        strategy.setSuperControllerClass(props.getStr("strategy.superCcontrollerclass"));
        mpg.setStrategy(strategy);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent(props.getStr("package.parent"));
        pc.setModuleName(props.getStr("package.modulename"));
        pc.setMapper("dao.mapper");
        pc.setEntity("model");
        mpg.setPackageInfo(pc);

        // 注入自定义配置，可以在 VM 中使用 cfg.abc 【可无】
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<>(1);
                map.put("abc", this.getConfig().getGlobalConfig().getAuthor() + "-mp");
                this.setMap(map);
            }
        };

        // 自定义模版生成
        List<FileOutConfig> focList = new ArrayList<>();

        //生成webcontroller
        FileOutConfig controllerEdirectory = new FileOutConfig("/templates/webcontroller.java.vm") {
            @Override
            public String outputFile(com.baomidou.mybatisplus.generator.config.po.TableInfo tableInfo) {
                return props.getStr("fileout.controlleredirectory") + "Web" + tableInfo.getEntityName() + "Controller.java";
            }
        };

        //生成service
        FileOutConfig serviceEdirectory = new FileOutConfig("/templates/service.java.vm") {
            @Override
            public String outputFile(com.baomidou.mybatisplus.generator.config.po.TableInfo tableInfo) {
                return props.getStr("fileout.serviceedirectory") + tableInfo.getEntityName() + "Service.java";
            }
        };

        //生成service实现类
        FileOutConfig impEdirectory = new FileOutConfig("/templates/serviceImpl.java.vm") {
            @Override
            public String outputFile(com.baomidou.mybatisplus.generator.config.po.TableInfo tableInfo) {
                return props.getStr("fileout.serviceimpledirectory") + tableInfo.getEntityName() + "ServiceImpl.java";
            }
        };

        //生成dao层
        FileOutConfig mapperEdirectory = new FileOutConfig("/templates/mapper.java.vm") {
            @Override
            public String outputFile(com.baomidou.mybatisplus.generator.config.po.TableInfo tableInfo) {
                return props.getStr("fileout.pagedirectory") + tableInfo.getEntityName() + "Mapper.java";
            }
        };

        // 调整 xml 生成目录
        FileOutConfig mapperXmlEdirectory = new FileOutConfig("/templates/mapper.xml.vm") {
            @Override
            public String outputFile(com.baomidou.mybatisplus.generator.config.po.TableInfo tableInfo) {
                return props.getStr("fileout.mapperxmldirectory") + tableInfo.getEntityName() + "Mapper.xml";
            }
        };

        // 调整 htmlList 生成目录
        FileOutConfig htmlList = new FileOutConfig("/templates/list.html.vm") {
            @Override
            public String outputFile(com.baomidou.mybatisplus.generator.config.po.TableInfo tableInfo) {
                return props.getStr("fileout.pagedirectory") + strDispose(tableInfo.getEntityName()) + "List.html";
            }
        };
        // 调整 htmlList 生成目录
        FileOutConfig jsList = new FileOutConfig("/templates/list.js.vm") {
            @Override
            public String outputFile(com.baomidou.mybatisplus.generator.config.po.TableInfo tableInfo) {
                return props.getStr("fileout.pagedirectory") + strDispose(tableInfo.getEntityName()) + "List.js";
            }
        };

        FileOutConfig modelhtmlList = new FileOutConfig("/templates/model.html.vm") {
            @Override
            public String outputFile(com.baomidou.mybatisplus.generator.config.po.TableInfo tableInfo) {
                return props.getStr("fileout.pagedirectory") + strDispose(tableInfo.getEntityName()) + "model.html";
            }
        };

        FileOutConfig modeljsList = new FileOutConfig("/templates/model.js.vm") {
            @Override
            public String outputFile(com.baomidou.mybatisplus.generator.config.po.TableInfo tableInfo) {
                return props.getStr("fileout.pagedirectory") + strDispose(tableInfo.getEntityName()) + "model.js";
            }
        };
        focList.add(controllerEdirectory);
//        focList.add(serviceEdirectory);
        focList.add(impEdirectory);
        focList.add(mapperXmlEdirectory);

        focList.add(htmlList);
        focList.add(jsList);
        focList.add(modelhtmlList);
        focList.add(modeljsList);
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        // 关闭默认 xml 生成，调整生成 至 根目录
        TemplateConfig tc = new TemplateConfig();
        tc.setXml(null);
        mpg.setTemplate(tc);

        // 执行生成
        mpg.execute();
    }

    private String strDispose(String str) {
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }
}
