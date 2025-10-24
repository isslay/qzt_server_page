package com.qzt.tools.zk;

import org.apache.curator.test.TestingServer;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class EmbeddedZkRunner {
    public static void main(String[] args) throws Exception {
        int port = 2181; // match Dubbo config (127.0.0.1:2181)
        if (args != null && args.length > 0) {
            try { port = Integer.parseInt(args[0]); } catch (NumberFormatException ignored) {}
        }
        File dataDir = new File("./target/zk-data");
        if (!dataDir.exists() && !dataDir.mkdirs()) {
            System.err.println("Failed to create data dir: " + dataDir.getAbsolutePath());
        }
        System.out.println("Starting embedded ZooKeeper on port " + port + "...");
        TestingServer server = startServer(port, dataDir);
        System.out.println("Embedded ZooKeeper started at 127.0.0.1:" + port + ", dataDir=" + dataDir.getAbsolutePath());
        // Block forever until process is terminated
        new CountDownLatch(1).await();
        // On shutdown
        if (server != null) {
            try { server.close(); } catch (IOException ignored) {}
        }
    }

    private static TestingServer startServer(int port, File dataDir) throws Exception {
        // Curator 2.x TestingServer has a (int, File) constructor
        return new TestingServer(port, dataDir);
    }
}

