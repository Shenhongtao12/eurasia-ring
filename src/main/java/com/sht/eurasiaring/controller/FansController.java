package com.sht.eurasiaring.controller;


import com.sht.eurasiaring.entity.Fans;
import com.sht.eurasiaring.service.FansService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/ring/fans"})
public class FansController extends BaseController{

    @Autowired
    private FansService fansService;

    @PostMapping({"save"})
    public ResponseEntity save(@RequestBody Fans fans) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.fansService.save(fans));
    }

    @DeleteMapping({"delete"})
    public ResponseEntity delete(@RequestParam(name = "id") Integer id) {
        return ResponseEntity.ok(this.fansService.delete(id));
    }


    @GetMapping({"findFansToUser"})
    public ResponseEntity findFansToUser(
                                         @RequestParam(name = "fansId", required = false) Integer fansId,
                                         @RequestParam(name = "page", defaultValue = "1") Integer page,
                                         @RequestParam(name = "rows", defaultValue = "10") Integer rows) {
        return ResponseEntity.ok(this.fansService.findFansToUser(userId, fansId, page, rows));
    }


    @GetMapping({"checkFans"})
    public ResponseEntity checkFans(@RequestParam(name = "toUserId") Integer toUserId) {
        return ResponseEntity.ok(this.fansService.checkFans(userId, toUserId));
    }
}
