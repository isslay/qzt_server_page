package com.qzt.ump.generator;

/**
 * 代码生成器
 *
 * @author
 * @date
 */
public class CodeGenerator {

    public static void main(String[] args) {
        UmpGeneratorUtil mybatisPlusGeneratorUtil = new UmpGeneratorUtil();
        String propertiesFilePath = "generator.properties";
        mybatisPlusGeneratorUtil.generator(propertiesFilePath);
    }

}
