package com.sjr.yygh.hosp.controller.api;


import com.sjr.yygh.common.exception.YyghException;
import com.sjr.yygh.common.helper.HttpRequestHelper;
import com.sjr.yygh.common.result.Result;
import com.sjr.yygh.common.result.ResultCodeEnum;
import com.sjr.yygh.common.utils.MD5;
import com.sjr.yygh.hosp.service.HospitalService;
import com.sjr.yygh.hosp.service.HospitalSetService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Api(tags = "医院管理")
@RestController
@RequestMapping("/api/hosp")
public class ApiController {
    @Autowired
    private HospitalService hospitalService;
    @Autowired
    private HospitalSetService hospitalSetService;


    @ApiOperation(value = "获取医院信息")
    @PostMapping("hospital/show")
    public Result hospital(HttpServletRequest request) {
        Map<String, String[]> requsetMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requsetMap);
        //1 获取医院系统传递过来的签名
        String hospSign = paramMap.get("sign").toString();
//        2 根据传递过来的医院编号，查询签名
        String hoscode = paramMap.get("hoscode").toString();
        String signKey = hospitalSetService.getSignKey(hoscode);
//        3 把数据库查询签名进行MD5加密
        String signKeyMd5 = MD5.encrypt(signKey);
//        判断签名是否一致
        if(!hospSign.equals(signKeyMd5)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        return Result.ok(hospitalService.getByHoscode((String)paramMap.get("hoscode")));
    }


    //    上传医院接口
    @ApiOperation("上传医院")
    @PostMapping("/saveHospital")
    public Result saveHospital(HttpServletRequest request){
//        获取传递过来医院信息
        Map<String, String[]> requsetMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requsetMap);
        //1 获取医院系统传递过来的签名
        String hospSign = paramMap.get("sign").toString();
//        2 根据传递过来的医院编号，查询签名
        String hoscode = paramMap.get("hoscode").toString();
        String signKey = hospitalSetService.getSignKey(hoscode);
//        3 把数据库查询签名进行MD5加密
        String signKeyMd5 = MD5.encrypt(signKey);
//        判断签名是否一致
        if(!hospSign.equals(signKeyMd5)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        //传输过程中“+”转换为了“ ”，因此我们要转换回来
        String logoDataString = (String)paramMap.get("logoData");
        String logoData = logoDataString.replaceAll(" ", "+");
        paramMap.put("logoData", logoData);

        hospitalService.save(paramMap);
        return Result.ok();
    }
}
