package com.sjr.yygh.hosp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sjr.yygh.common.result.Result;
import com.sjr.yygh.common.utils.MD5;
import com.sjr.yygh.hosp.service.HospitalSetService;
import com.sjr.yygh.model.hosp.Hospital;
import com.sjr.yygh.model.hosp.HospitalSet;
import com.sjr.yygh.vo.hosp.HospitalSetQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.Console;
import java.util.List;
import java.util.Random;

@Api(tags = "医院设置管理")
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
//@CrossOrigin
public class HospitalSetController {
//    注入hospitalSetService
    @Autowired
    private HospitalSetService hospitalSetService;

//    1.查询医院设置表所有信息
    @GetMapping("/findAllx")
    @ApiOperation("查询医院设置表所有信息")
    public Result findAllHospitalSet(){
        return Result.ok(hospitalSetService.list());
    }

//    2.根据Id逻辑删除医院设置表的信息
    @DeleteMapping("/{id}")
    @ApiOperation("根据Id逻辑删除医院设置表的信息")
    public Result removeHospSet(@PathVariable Long id){
        if(hospitalSetService.removeById(id))
            return Result.ok();
        else
            return Result.fail();
    }

    @PostMapping("/findPageHospSet/{current}/{limit}")
    @ApiOperation("条件查询带分页")
    public Result findPageHospSet(@PathVariable long current,
                                  @PathVariable long limit,
                                  @RequestBody(required = false) HospitalSetQueryVo hospitalSetQueryVo){
        Page<HospitalSet> page = new Page<>(current, limit);
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
//        import org.springframework.util.StringUtils;
        if(!StringUtils.isEmpty(hospitalSetQueryVo.getHosname()))
            wrapper.like("hosname",hospitalSetQueryVo.getHosname());
        if(!StringUtils.isEmpty(hospitalSetQueryVo.getHoscode()))
            wrapper.eq("hoscode",hospitalSetQueryVo.getHoscode());
        Page<HospitalSet> hospitalSetPage = hospitalSetService.page(page, wrapper);
        return Result.ok(hospitalSetPage);
    }

    @PostMapping("/saveHospitalSet")
    @ApiOperation("添加医院设置")
    public Result saveHospitalSet(@RequestBody HospitalSet hospitalSet){
        hospitalSet.setStatus(1);
        Random random = new Random();
//        生成签名密钥并且保存
        hospitalSet.setSignKey(MD5.encrypt(System.currentTimeMillis()+""+random.nextInt(1000)));
        if(hospitalSetService.save(hospitalSet))
            return Result.ok();
        else
            return Result.fail();
    }

    @ApiOperation("根据Id获取医院信息")
    @GetMapping("/getHospSet/{id}")
    public Result c(@PathVariable Long id){
        return Result.ok(hospitalSetService.getById(id));
    }

    @ApiOperation("修改医院设置")
    @PutMapping("/updateHospSet")
    public Result updateHosptalSet(@RequestBody HospitalSet hospitalSet){
        return Result.ok(hospitalSetService.updateById(hospitalSet));
    }

    @ApiOperation("根据Id批量删除医院设置")
    @DeleteMapping("/batchRemoveHospSet")
    public Result batchRemoveHosptalSet(@RequestBody List<Long> list){
        return Result.ok(hospitalSetService.removeByIds(list));
    }

    @ApiOperation("医院设置锁定和解锁")
    @PutMapping("/lockHospitalSet/{id}/{status}")
    public Result lockHospitalSet(@PathVariable Long id,
                                  @PathVariable Integer status){
//       先根据Id获取实体类信息，再修改其状态
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        if (StringUtils.isEmpty(hospitalSet)){
            return Result.fail();
        }
        hospitalSet.setStatus(status);
        hospitalSetService.updateById(hospitalSet);
        return Result.ok();
    }

    @ApiOperation("发送签名密钥")
    @GetMapping("/sendKey/{id}")
    public Result lockHospitalSet(@PathVariable Long id){
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        String signKey = hospitalSet.getSignKey();
        String hoscode = hospitalSet.getHoscode();
        return Result.ok();
    }

}
