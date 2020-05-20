package com.sht.eurasiaring.controller;

import com.sht.eurasiaring.entity.Reply;
import com.sht.eurasiaring.service.ReplyService;
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
@RequestMapping({"ring/reply"})
public class ReplyController {

    @Autowired
    private ReplyService replyService;

    @PostMapping()
    public ResponseEntity<JsonData> save(@RequestBody Reply reply) throws Exception {
        return ResponseEntity.ok(this.replyService.save(reply));
    }

    @DeleteMapping()
    public ResponseEntity<JsonData> delete(@RequestParam(name = "id") Long id) {
        return ResponseEntity.ok(this.replyService.delete(id));
    }


    /*@GetMapping({"findAllByUser"})
    public ResponseEntity findAllByUser(@RequestParam(name = "nameId") Integer nameId) {
        return ResponseEntity.ok(this.replyService.findAllByUser(nameId));
    }*/
}
