package com.qzt.ump.server.message;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 导出配置类
 *
 * @author Xiaofei
 * @date 2019-07-11
 */
@Component
@ConfigurationProperties(prefix = "export")
public class ExportConfig {

    /**
     * 模版目录
     */
    private String catalogue;

    public String getCatalogue() {
        return catalogue;
    }

    public void setCatalogue(String catalogue) {
        this.catalogue = catalogue;
    }
}
