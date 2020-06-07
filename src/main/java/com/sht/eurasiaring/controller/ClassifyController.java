package com.sht.eurasiaring.controller;

import com.sht.eurasiaring.entity.Classify;
import com.sht.eurasiaring.service.ClassifyService;
import com.sht.eurasiaring.utils.JsonData;
import io.swagger.annotations.Api;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Hongtao Shen
 * @date 2020/5/16 - 17:30
 **/
@RestController
@RequestMapping("ring/classify")
@Api(tags = "分类服务")
public class ClassifyController {

    @Autowired
    private ClassifyService classifyService;

    @PostMapping("save")
    public ResponseEntity<JsonData> save(@RequestBody Classify classify) {
        Classify save = classifyService.save(classify);
        JsonData jsonData = new JsonData();
        jsonData.setCode(0);
        if (save.getId() == null){
            jsonData.setCode(-1);
        }
        return ResponseEntity.status(HttpStatus.SC_OK).body(jsonData);
    }

    @PutMapping("update")
    public ResponseEntity<JsonData> update(@RequestBody Classify classify) {
        return ResponseEntity.status(HttpStatus.SC_OK).body(classifyService.update(classify));
    }
    @GetMapping()
    public ResponseEntity<JsonData> findAll(){
        return ResponseEntity.ok(JsonData.buildSuccess(classifyService.findAll(), ""));
    }
}
