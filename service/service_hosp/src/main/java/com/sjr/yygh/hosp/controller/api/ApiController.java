package com.sjr.yygh.hosp.controller.api;



import com.sjr.yygh.common.exception.YyghException;
import com.sjr.yygh.common.helper.HttpRequestHelper;
import com.sjr.yygh.common.result.Result;
import com.sjr.yygh.common.result.ResultCodeEnum;
import com.sjr.yygh.common.utils.MD5;
import com.sjr.yygh.hosp.service.DepartmentService;
import com.sjr.yygh.hosp.service.HospitalService;
import com.sjr.yygh.hosp.service.HospitalSetService;
import com.sjr.yygh.hosp.service.ScheduleService;
import com.sjr.yygh.model.hosp.Department;
import com.sjr.yygh.model.hosp.Schedule;
import com.sjr.yygh.vo.hosp.DepartmentQueryVo;
import com.sjr.yygh.vo.hosp.ScheduleQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ScheduleService scheduleService;

//    删除排班接口
    @ApiOperation(value = "删除科室")
    @PostMapping("/schedule/remove")
    public Result removeSchedule(HttpServletRequest request) {
        Map<String, Object> paramMap = this.getParamMap(request);
        String hoscode = (String)paramMap.get("hoscode");
//        排班编号
        String hosScheduleId = (String)paramMap.get("hosScheduleId");
        this.MD5isok(paramMap);
        scheduleService.remove(hoscode,hosScheduleId);
        return Result.ok();
    }


//    查询排班接口
    @ApiOperation(value = "获取排班分页列表")
    @PostMapping("schedule/list")
    public Result findSchedule(HttpServletRequest request) {
        Map<String, Object> paramMap = this.getParamMap(request);
        String hoscode = (String)paramMap.get("hoscode");
        String depcode = (String)paramMap.get("depcode");
        int page = StringUtils.isEmpty(paramMap.get("page")) ? 1 : Integer.parseInt((String)paramMap.get("page"));
        int limit = StringUtils.isEmpty(paramMap.get("limit")) ? 10 : Integer.parseInt((String)paramMap.get("limit"));
        this.MD5isok(paramMap);
        ScheduleQueryVo scheduleQueryVo = new ScheduleQueryVo();
        scheduleQueryVo.setHoscode(hoscode);
        scheduleQueryVo.setDepcode(depcode);
        Page<Schedule> pageModel = scheduleService.findPageSchedule(page, limit, scheduleQueryVo);
        return Result.ok(pageModel);
    }


//    上传排班接口
    @ApiOperation(value = "上传排班")
    @PostMapping("/saveSchedule")
    public Result saveSchedule(HttpServletRequest request) {
        Map<String, Object> paramMap = this.getParamMap(request);
        this.MD5isok(paramMap);
        scheduleService.save(paramMap);
        return Result.ok();
    }


//    删除科室接口
    @ApiOperation("删除科室接口")
    @PostMapping("/department/remove")
    public Result removeDepartment(HttpServletRequest request){
        Map<String, Object> paramMap = this.getParamMap(request);
//        医院编号和科室编号
        String hoscode = (String)paramMap.get("hoscode");
        String depcode = (String)paramMap.get("depcode");
        this.MD5isok(paramMap);
        departmentService.remove(hoscode,depcode);
        return Result.ok();
    }
//    查询科室接口
    @ApiOperation(value = "获取分页列表")
    @PostMapping("/department/list")
    public Result department(HttpServletRequest request) {
        Map<String, Object> paramMap = this.getParamMap(request);
    //必须参数校验 略
        String hoscode = (String)paramMap.get("hoscode");
    //非必填
        int page = StringUtils.isEmpty(paramMap.get("page")) ? 1 : Integer.parseInt((String)paramMap.get("page"));
        int limit = StringUtils.isEmpty(paramMap.get("limit")) ? 10 : Integer.parseInt((String)paramMap.get("limit"));

        if(StringUtils.isEmpty(hoscode)) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
    //签名校验
        this.MD5isok(paramMap);

        DepartmentQueryVo departmentQueryVo = new DepartmentQueryVo();
        departmentQueryVo.setHoscode(hoscode);
        Page<Department> pageModel = departmentService.findPageDepartment(page, limit, departmentQueryVo);
        return Result.ok(pageModel);
    }


    //    上传科室接口
    @ApiOperation(value = "上传科室")
    @PostMapping("/saveDepartment")
    public Result saveDepartment(HttpServletRequest request) {
        Map<String, Object> paramMap = this.getParamMap(request);
        this.MD5isok(paramMap);
        departmentService.save(paramMap);
        return Result.ok();
    }



    public Map<String, Object> getParamMap(HttpServletRequest request){
        Map<String, String[]> requsetMap = request.getParameterMap();
        return HttpRequestHelper.switchMap(requsetMap);
    }

    public void MD5isok(Map<String, Object> paramMap){
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
    }

    @ApiOperation(value = "获取医院信息")
    @PostMapping("hospital/show")
    public Result hospital(HttpServletRequest request) {
        Map<String, Object> paramMap = this.getParamMap(request);
        this.MD5isok(paramMap);
        return Result.ok(hospitalService.getByHoscode((String)paramMap.get("hoscode")));
    }


    //    上传医院接口
    @ApiOperation("上传医院")
    @PostMapping("/saveHospital")
    public Result saveHospital(HttpServletRequest request){
//        获取传递过来医院信息
        Map<String, Object> paramMap = this.getParamMap(request);
        this.MD5isok(paramMap);
        //传输过程中“+”转换为了“ ”，因此我们要转换回来
        String logoDataString = (String)paramMap.get("logoData");
        String logoData = logoDataString.replaceAll(" ", "+");
        paramMap.put("logoData", logoData);
        paramMap.put("logoData", logoData);
        hospitalService.save(paramMap);
        return Result.ok();
    }
}
