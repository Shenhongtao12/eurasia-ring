package com.sht.eurasiaring.controller;

import com.sht.eurasiaring.service.PraiseService;
import com.sht.eurasiaring.utils.JsonData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "点赞服务")
public class PraiseController extends BaseController {
    @Autowired
    private PraiseService praiseService;

    @PostMapping()
    @ApiOperation(value = "点赞", notes = "数据格式：\"type\":\"reply:600\",\"state\":\"1\" ")
    public ResponseEntity<JsonData> save(@RequestBody Map<String, Object> map) { // "type":"reply:600:600","state":"1"
        String type = map.get("type") + ":"+ userId;
        praiseService.save(type, map.get("state"));
        return ResponseEntity.status(HttpStatus.OK).body(JsonData.buildSuccess("成功"));
    }
}
