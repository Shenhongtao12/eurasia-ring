package com.sht.eurasiaring.controller;

import com.sht.eurasiaring.entity.Post;
import com.sht.eurasiaring.service.PostService;
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
@RequestMapping("/ring/post")
public class PostController extends BaseController{

    @Autowired
    private PostService postService;

    @PostMapping()
    public ResponseEntity<JsonData> save(@RequestBody Post post){
        return ResponseEntity.status(HttpStatus.OK).body(postService.save(post));
    }

    //@PutMapping()
    public ResponseEntity<JsonData> update(@RequestBody Post post) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.update(post));
    }

    @DeleteMapping
    public ResponseEntity<JsonData> delete(@RequestParam(name = "id") Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.delete(id));
    }

    @GetMapping("{postId}")
    public ResponseEntity<JsonData> findById(@PathVariable(name = "postId") Integer postId){
        Post result = postService.findById(postId, userId);
        if (result == null) {
            return ResponseEntity.ok(JsonData.buildError("不存在的postId：" + postId));
        }
        return ResponseEntity.ok(JsonData.buildSuccess(result, ""));
    }
}
