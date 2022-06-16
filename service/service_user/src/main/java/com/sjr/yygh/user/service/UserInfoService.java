package com.sjr.yygh.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sjr.yygh.model.user.UserInfo;
import com.sjr.yygh.vo.user.LoginVo;

import java.util.Map;

public interface UserInfoService extends IService<UserInfo> {
//    用户手机号登录接口
    Map<String, Object> loginUser(LoginVo loginVo);
}
