package com.sht.eurasiaring.controller;

import com.sht.eurasiaring.entity.Reply;
import com.sht.eurasiaring.service.ReplyService;
import com.sht.eurasiaring.utils.JsonData;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"ring/reply"})
@Api(tags = "回复服务")
public class ReplyController extends BaseController{

    @Autowired
    private ReplyService replyService;

    @PostMapping()
    public ResponseEntity<JsonData> save(@RequestBody Reply reply) throws Exception {
        reply.setUserId(userId);
        return ResponseEntity.ok(this.replyService.save(reply));
    }

    @DeleteMapping()
    public ResponseEntity<JsonData> delete(@RequestParam(name = "id") Integer id) {
        return ResponseEntity.ok(this.replyService.delete(id));
    }


    @GetMapping({"findAllByUser"})
    public ResponseEntity<JsonData> findAllByUser(@RequestParam(name = "id", required = false) Integer id,
                                                  @RequestParam(name = "type") String type) {

        return ResponseEntity.ok(this.replyService.findAllByUser(id == null ? userId : id, type));
    }
}
