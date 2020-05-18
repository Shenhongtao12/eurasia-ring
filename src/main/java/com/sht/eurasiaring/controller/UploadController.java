package com.sht.eurasiaring.controller;

import com.alibaba.fastjson.JSON;
import com.sht.eurasiaring.exception.AllException;
import com.sht.eurasiaring.service.UploadService;
import com.sht.eurasiaring.utils.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

//上传图片
@RestController
@RequestMapping({"ring/upload"})
public class UploadController {
    @Autowired
    private UploadService uploadService;

    //@PostMapping(value = "/image", headers = "content-type=multipart/form-data")
    @PostMapping({"image"})
    public ResponseEntity<JsonData> uploadImage(@RequestParam("file") MultipartFile[] file, @RequestParam(name = "site", defaultValue = "/eurasia/other") String site) {
        JsonData url = this.uploadService.upload(file, site);
        if (StringUtils.isEmpty(url)) {
            throw new AllException(-1, "图片上传失败");
        }

        return ResponseEntity.ok(url);
    }

    @DeleteMapping({"deleteImage"})
    public ResponseEntity<JsonData> delFile(@RequestParam(name = "url") String url) {
        String msg = uploadService.deleteImage(url);
        if ("删除成功".equals(msg)) {
            return ResponseEntity.status(HttpStatus.CREATED).body(JsonData.buildSuccess(msg));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(JsonData.buildError(msg));
    }
}
