package com.sht.eurasiaring.controller;

import com.sht.eurasiaring.service.PraiseService;
import com.sht.eurasiaring.utils.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author Hongtao Shen
 * @date 2020/5/24 - 19:58
 **/
@RequestMapping("ring/praise")
@RestController
public class PraiseController extends BaseController {
    @Autowired
    private PraiseService praiseService;

    @PostMapping()
    public ResponseEntity<JsonData> save(@RequestBody Map<String, Object> map) { // "type":"reply:600:600","state":"1"
        String type = map.get("type") + ":"+ userId;
        praiseService.save(type, map.get("state"));
        return ResponseEntity.status(HttpStatus.OK).body(JsonData.buildSuccess("成功"));
    }
}
