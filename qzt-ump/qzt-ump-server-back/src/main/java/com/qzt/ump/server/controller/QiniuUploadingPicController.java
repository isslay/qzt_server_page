package com.qzt.ump.server.controller;

import com.qiniu.util.Auth;
import com.qzt.common.core.constant.Constant;
import com.qzt.common.tools.QiniuUtil;
import com.qzt.common.web.BaseController;
import com.qzt.common.web.util.ReturnUtilServer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@Api(value = "QiniuUploadingPicController", description = "后台七牛图片上传管理")
@RequestMapping("/back/qiniuUploading")
public class QiniuUploadingPicController extends BaseController {

    @Autowired
    private ReturnUtilServer returnUtil;

    @ApiOperation(value = "获取后台token", notes = "获取后台token", httpMethod = "GET")
    @GetMapping(value = "/gettoken")
    public String tfAttachmentToken() {
        //正式地址
        String accessKey = "97coXFXjyZPlduGwn_klr3akDAW-SVYshgdR_ktR";
        String secretKey = "RXLOHT-2V1Z9QRCCcYCXEOmfouP3ns---pKUqD8a";
        String bucket = "dgdyf";
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        return upToken;
    }

    @ApiOperation(value = "上传图片", notes = "上传图片")
    @PostMapping(value = "/uploadingPic")
    public Map<String, Object> saveTfAttachment(@RequestParam("file") MultipartFile attachmentFile) {
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
            return returnUtil.returnMessMap(map);
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }
}
