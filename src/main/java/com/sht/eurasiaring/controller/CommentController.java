package com.sht.eurasiaring.controller;

import com.sht.eurasiaring.entity.Comment;
import com.sht.eurasiaring.service.CommentService;
import com.sht.eurasiaring.utils.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
