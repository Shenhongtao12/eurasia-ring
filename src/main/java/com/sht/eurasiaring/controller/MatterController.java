package com.sht.eurasiaring.controller;

import com.sht.eurasiaring.entity.Matter;
import com.sht.eurasiaring.service.MatterService;
import com.sht.eurasiaring.utils.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Hongtao Shen
 * @date 2020/5/17 - 19:07
 **/
@RestController
@RequestMapping("/ring/matter")
public class MatterController {

    @Autowired
    private MatterService matterService;

    @PostMapping()
    public ResponseEntity<JsonData> save(@RequestBody Matter matter){
        return ResponseEntity.status(HttpStatus.OK).body(matterService.save(matter));
    }

    //@PutMapping()
    public ResponseEntity<JsonData> update(@RequestBody Matter matter) {
        return ResponseEntity.status(HttpStatus.OK).body(matterService.update(matter));
    }

    @DeleteMapping
    public ResponseEntity<JsonData> delete(@RequestParam(name = "id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(matterService.delete(id));
    }

    @GetMapping("{matterId}")
    public ResponseEntity<JsonData> findById(@PathVariable(name = "matterId") Long matterId){
        Matter result = matterService.findById(matterId);
        if (result == null) {
            return ResponseEntity.ok(JsonData.buildError("不存在的matterId：" + matterId));
        }
        return ResponseEntity.ok(JsonData.buildSuccess(result, ""));
    }
}
