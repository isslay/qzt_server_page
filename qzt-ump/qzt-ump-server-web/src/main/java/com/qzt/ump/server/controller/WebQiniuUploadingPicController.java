package com.qzt.ump.server.controller;

import com.qiniu.util.Auth;
import com.qzt.common.core.constant.Constant;
import com.qzt.common.tools.QiniuUtil;
import com.qzt.common.web.BaseController;
import com.qzt.common.web.util.ReturnUtilServer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@Api(value = "WebQiniuUploadingPicController", description = "Web七牛图片上传管理")
public class WebQiniuUploadingPicController extends BaseController {

    @Autowired
    private ReturnUtilServer returnUtil;

    @ApiOperation(value = "上传图片", notes = "文件最大限制500K")
    @PostMapping(value = "/pubapi/qiniuUploading/uploadingPic")
    public Map saveTfAttachment(@RequestParam("file") MultipartFile attachmentFile) {
        String attachmentName = "";
        try {
            long size = attachmentFile.getSize();
            if (size > 550000) {
                return this.returnUtil.returnMess(Constant.FILE_UPLOAD_FAILED);
            }
            String fileName = System.currentTimeMillis() + "";
            String attachmentOriginalName = attachmentFile.getOriginalFilename();//文件源名
            String fileFormat = attachmentOriginalName.substring(attachmentOriginalName.lastIndexOf(".") + 1, attachmentOriginalName.length());
            attachmentName = fileName + "." + fileFormat;//新文件名
            Map map = QiniuUtil.qiniuObjectStorage(attachmentFile.getBytes(), attachmentName);
            return this.returnUtil.returnMessMap(map);
        } catch (Exception e) {
            e.printStackTrace();
            return this.returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

    @ApiOperation(value = "获取前端token", notes = "获取前端token", httpMethod = "GET")
    @GetMapping(value = "/pubapi/klygqiniukey/gettoken")
    public String getToken() {
        //正式地址
        String accessKey = "uMyIQBn0VrbfyeU0EpHs0hE-BNUEsoGg_1L2xAdZ";
        String secretKey = "v7GfLhkMQ4UtglSiYpPgYDfwDF-hLlPplDeJAMeN";
        String bucket = "qztpic";
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        return upToken;
    }
}
