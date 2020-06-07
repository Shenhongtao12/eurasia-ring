package com.sht.eurasiaring.controller;


import com.sht.eurasiaring.entity.Fans;
import com.sht.eurasiaring.service.FansService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/ring/fans"})
@Api(tags = "粉丝关注服务")
public class FansController extends BaseController{

    @Autowired
    private FansService fansService;

    @PostMapping({"save"})
    @ApiOperation(value = "关注或者取消关注用户", notes = "第一次是关注，第二次是取消关注")
    public ResponseEntity save(@RequestBody Fans fans) {
        fans.setFansId(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.fansService.save(fans));
    }

    @DeleteMapping({"delete"})
    @ApiImplicitParam(name = "id", required = true, value = "主键id")
    @ApiOperation(value = "取消关注用户", notes = "根据该条数据id取消关注")
    public ResponseEntity delete(@RequestParam(name = "id") Integer id) {
        return ResponseEntity.ok(this.fansService.delete(id));
    }


    @GetMapping({"findFansToUser"})
    @ApiOperation(value = "根据用户id查找粉丝或者关注", notes = "userId：查找他的粉丝，fansId：查找他的关注")
    public ResponseEntity findFansToUser(
            @ApiParam("查找改userId的粉丝") @RequestParam(name = "userId", required = false) Integer userid,
            @RequestParam(name = "fansId", required = false) Integer fansId,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "rows", defaultValue = "20") Integer rows) {
        return ResponseEntity.ok(this.fansService.findFansToUser(userid, fansId, page, rows));
    }


    @GetMapping({"checkFans"})
    @ApiOperation(value = "判断是否关注用户", notes = "toUserId: 待判断的用户id")
    public ResponseEntity checkFans(@RequestParam(name = "toUserId") Integer toUserId) {
        return ResponseEntity.ok(this.fansService.checkFans(userId, toUserId));
    }
}
