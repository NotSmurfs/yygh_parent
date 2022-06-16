package com.sjr.yygh.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sjr.yygh.common.exception.YyghException;
import com.sjr.yygh.common.helper.JwtHelper;
import com.sjr.yygh.common.result.ResultCodeEnum;
import com.sjr.yygh.model.user.UserInfo;
import com.sjr.yygh.user.mapper.UserInfoMapper;
import com.sjr.yygh.user.service.UserInfoService;
import com.sjr.yygh.vo.user.LoginVo;
import io.jsonwebtoken.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserInfoServiceImpl extends
        ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {
//    用户手机号登录接口
    @Override
    public Map<String, Object> loginUser(LoginVo loginVo) {
//        从loginVo中获取手机号和验证码
        String phone = loginVo.getPhone();
        String code = loginVo.getCode();
        //校验参数(判断手机号和验证码是否为空)
        if(StringUtils.isEmpty(phone) ||
                StringUtils.isEmpty(code)) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }

        //TODO 校验校验验证码(判断手机验证码和输入是否一致)

        //判断是否第一次登录，根据手机号查询数据库，如果不存在相同手机号，就是第一次登录
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", phone);
        //获取会员
        UserInfo userInfo = baseMapper.selectOne(queryWrapper);
        if(null == userInfo) {
            userInfo = new UserInfo();
            userInfo.setName("");
            userInfo.setPhone(phone);
            userInfo.setStatus(1);
            this.save(userInfo);
        }
//        不是第一次登录就直接登录
        //校验是否被禁用
        if(userInfo.getStatus() == 0) {
            throw new YyghException(ResultCodeEnum.LOGIN_DISABLED_ERROR);
        }

        //TODO 记录登录(token生成)

        //返回页面显示名称
//        返回登录信息
//        返回登录用户名
//        返回token信息
        Map<String, Object> map = new HashMap<>();
        String name = userInfo.getName();
        if(StringUtils.isEmpty(name)) {
            name = userInfo.getNickName();
        }
        if(StringUtils.isEmpty(name)) {
            name = userInfo.getPhone();
        }
        map.put("name", name);
//        jwt生成token生成
        String token = JwtHelper.createToken(userInfo.getId(), name);
        map.put("token", token);
        return map;
    }


}

