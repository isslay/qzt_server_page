package com.qzt.common.tools;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

import java.util.HashMap;
import java.util.Map;

public class QiniuUtil {

    private static final String ACCESSKEY = "uMyIQBn0VrbfyeU0EpHs0hE-BNUEsoGg_1L2xAdZ";
    private static final String SECRETKEY = "v7GfLhkMQ4UtglSiYpPgYDfwDF-hLlPplDeJAMeN";
    private static final String BUCKET = "qztpic";
    private static final String URLS = "https://img.jlqizutang.com/";


    /**
     * 七牛上传文件
     *
     * @param bytes    字节流文件
     * @param fileName 不指定fileName的情况下，以文件内容的hash值作为文件名
     * @return
     */
    public static Map qiniuObjectStorage(byte[] bytes, String fileName) throws Exception {
        Map map = new HashMap();
        Configuration cfg = new Configuration(Zone.zone1()); //华北	Zone.zone1()
        UploadManager uploadManager = new UploadManager(cfg);
        Auth auth = Auth.create(ACCESSKEY, SECRETKEY);
        String upToken = auth.uploadToken(BUCKET);
        Response response = uploadManager.put(bytes, fileName, upToken);
        DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
        map.put("picUrl", URLS + putRet.key);
        return map;
    }
}
