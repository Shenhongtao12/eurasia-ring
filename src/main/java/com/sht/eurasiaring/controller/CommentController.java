package com.sht.eurasiaring.controller;

import com.sht.eurasiaring.entity.Comment;
import com.sht.eurasiaring.service.CommentService;
import com.sht.eurasiaring.utils.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"ring/comment"})
public class CommentController extends BaseController{

    @Autowired
    private CommentService commentService;

    @PostMapping()
    public ResponseEntity<JsonData> save(@RequestBody Comment comment){
        comment.setUserId(userId);
        return ResponseEntity.ok(this.commentService.save(comment));
    }

    @DeleteMapping()
    public ResponseEntity<JsonData> delete(@RequestParam(name = "id") Integer id) {
        return ResponseEntity.ok(this.commentService.delete(id));
    }

    @GetMapping()
    public ResponseEntity<JsonData> findOneComment(@RequestParam(name = "id") Integer id){
        return ResponseEntity.ok(JsonData.buildSuccess(commentService.findOneComment(id, userId), ""));
    }
}
