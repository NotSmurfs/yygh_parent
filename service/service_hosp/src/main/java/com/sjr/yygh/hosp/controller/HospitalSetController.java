package com.sjr.yygh.hosp.controller;

import com.sjr.yygh.hosp.service.HospitalSetService;
import com.sjr.yygh.model.hosp.HospitalSet;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Api(tags = "医院设置管理")
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
public class HospitalSetController {
//    注入hospitalSetService
    @Autowired
    private HospitalSetService hospitalSetService;

//    1.查询医院设置表所有信息
    @GetMapping("/findAllx")
    @ApiOperation("查询医院设置表所有信息")
    public List<HospitalSet> findAllHospitalSet(){
        return hospitalSetService.list();
    }

//    2.根据Id逻辑删除医院设置表的信息
    @DeleteMapping("/{id}")
    @ApiOperation("根据Id逻辑删除医院设置表的信息")
    public boolean removeHospSet(@PathVariable Long id){
        return hospitalSetService.removeById(id);
    }



}
