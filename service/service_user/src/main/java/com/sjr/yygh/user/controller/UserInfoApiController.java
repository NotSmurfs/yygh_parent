package com.sjr.yygh.user.controller;

import com.sjr.yygh.common.result.Result;
import com.sjr.yygh.user.service.UserInfoService;
import com.sjr.yygh.vo.user.LoginVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Api(tags = "登录接口")
@RestController
@RequestMapping("/api/user")
public class UserInfoApiController {

    @Autowired
    private UserInfoService userInfoService;

//    用户手机号登录接口
    @ApiOperation(value = "会员登录")
    @PostMapping("login")
    public Result login(@RequestBody LoginVo loginVo, HttpServletRequest request) {
//        loginVo.setIp(IpUtil.getIpAddr(request));
        Map<String, Object> info = userInfoService.loginUser(loginVo);
        return Result.ok(info);
    }

}

