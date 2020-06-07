package com.sht.eurasiaring.controller;

import com.sht.eurasiaring.exception.AllException;
import com.sht.eurasiaring.service.UploadService;
import com.sht.eurasiaring.utils.JsonData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

//上传图片
@RestController
@RequestMapping({"ring/upload"})
@Api(tags = "图片服务")
public class UploadController {
    @Autowired
    private UploadService uploadService;

    //@PostMapping(value = "/image", headers = "content-type=multipart/form-data")
    @PostMapping({"image"})
    @ApiOperation(value = "上传图片", notes = "上传图片,可选图片路径为/eurasia/classify、/eurasia/carousel")
    public ResponseEntity<JsonData> uploadImage(@RequestParam("file") MultipartFile[] file, @RequestParam(name = "site", defaultValue = "/eurasia/other") String site) {
        JsonData url = this.uploadService.upload(file, site);
        if (StringUtils.isEmpty(url)) {
            throw new AllException(-1, "图片上传失败");
        }

        return ResponseEntity.ok(url);
    }

    @DeleteMapping({"deleteImage"})
    @ApiOperation(value = "删除单张图片", notes = "删除图片,传入图片的完整url")
    public ResponseEntity<JsonData> delFile(@RequestParam(name = "url") String url) {
        String msg = uploadService.deleteImage(url);
        if ("删除成功".equals(msg)) {
            return ResponseEntity.status(HttpStatus.CREATED).body(JsonData.buildSuccess(msg));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(JsonData.buildError(msg));
    }
}
