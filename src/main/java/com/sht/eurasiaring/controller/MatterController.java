package com.sht.eurasiaring.controller;

import com.sht.eurasiaring.entity.Matter;
import com.sht.eurasiaring.service.MatterService;
import com.sht.eurasiaring.utils.JsonData;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Hongtao Shen
 * @date 2020/5/23 - 13:33
 **/
@RestController
@RequestMapping("ring/matter")
@Api(tags = "热门事件")
public class MatterController {

    @Autowired
    private MatterService matterService;

    @PostMapping()
    public ResponseEntity<JsonData> save(@RequestBody Matter matter){
        return ResponseEntity.status(HttpStatus.OK).body(matterService.save(matter));
    }

    //设置热门话题
    @PutMapping("hotMatter")
    public ResponseEntity<JsonData> hotMatter(@RequestParam(name = "hotMatter") String hot){
        return ResponseEntity.status(HttpStatus.OK).body(matterService.hotMatter(hot));
    }
}
