package com.qzt.ump.server.dic;

import com.qzt.ump.rpc.api.SysAreaService;
import com.qzt.ump.rpc.api.SysDicService;
import com.qzt.ump.rpc.api.SysParamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Created by cgw on 2018/6/1.
 */
@Component
@Slf4j
@Order(2)
public class DicLoad implements CommandLineRunner {

    @Autowired
    private SysDicService sysDicService;

    @Autowired
    private SysParamService sysParamService;

    @Autowired
    private SysAreaService sysAreaService;

    @Override
    public void run(String... strings) throws Exception {
        log.info("======================开始加载字典===========================");
        retryInvoke("initializeDic", () -> { sysDicService.initializeDic(); return null; });

        log.info("======================开始加载系统参数===========================");
        retryInvoke("initializeParam", () -> { sysParamService.initializeParam(); return null; });

//        log.info("======================开始加载地区信息===========================");
//        retryInvoke("initializeArea", () -> { sysAreaService.initializeArea(); return null; });
    }

    /**
     * Simple retry helper to avoid failing startup if RPC providers aren\'t ready yet.
     */
    private void retryInvoke(String name, SupplierWithException<Void> supplier) {
        final int maxAttempts = 20; // ~about up to ~3 minutes with backoff below
        long sleepMs = 3000L;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                supplier.get();
                log.info("{} 成功 (attempt {}/{})", name, attempt, maxAttempts);
                return;
            } catch (Throwable ex) {
                String msg = ex.getClass().getSimpleName() + ": " + (ex.getMessage() == null ? "" : ex.getMessage());
                if (attempt < maxAttempts) {
                    log.warn("{} 失败，稍后重试 (attempt {}/{}): {}", name, attempt, maxAttempts, msg);
                    try { Thread.sleep(sleepMs); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
                    // exponential backoff up to 15s
                    sleepMs = Math.min(sleepMs * 2, 15000L);
                } else {
                    log.error("{} 多次重试失败，跳过初始化。最后错误: {}", name, msg, ex);
                }
            }
        }
    }

    @FunctionalInterface
    private interface SupplierWithException<T> {
        T get() throws Exception;
    }
}
