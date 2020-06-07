package com.sht.eurasiaring.controller;

import com.sht.eurasiaring.entity.Carousel;
import com.sht.eurasiaring.service.CarouselService;
import com.sht.eurasiaring.utils.JsonData;
import io.swagger.annotations.Api;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Hongtao Shen
 * @date 2020/5/16 - 18:14
 **/
@RestController
@RequestMapping("ring/carousel")
@Api(tags = "轮播图")
public class CarouselController {

    @Autowired
    private CarouselService carouselService;

    @PostMapping("save")
    public ResponseEntity<JsonData> save(@RequestBody Carousel carousel) {
        carouselService.save(carousel);
        return ResponseEntity.status(HttpStatus.SC_OK).body(JsonData.buildSuccess("成功"));
    }

    @PutMapping("update")
    public ResponseEntity<JsonData> update(@RequestBody Carousel carousel){
        return ResponseEntity.status(HttpStatus.SC_OK).body(carouselService.update(carousel));
    }

}
