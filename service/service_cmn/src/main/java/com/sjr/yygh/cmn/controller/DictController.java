package com.sjr.yygh.cmn.controller;

import com.sjr.yygh.cmn.service.DictService;
import com.sjr.yygh.common.result.Result;
import com.sjr.yygh.model.cmn.Dict;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Service;
import java.util.List;

@Api("数据字典的接口")
@RestController
@RequestMapping("/admin/cmn/dict")
@CrossOrigin
public class DictController {

    @Autowired
    private DictService dictService;

//    导入数据字典的接口
    @ApiOperation(value = "导入")
    @PostMapping("/importData")
    public Result importData(MultipartFile file) {
        dictService.importData(file);
        return Result.ok();
    }


    //    导出数据字典的接口
    @ApiOperation(value="导出")
    @GetMapping(value = "/exportData")
    public void exportData(HttpServletResponse response) {
        dictService.exportData(response);
    }


    @ApiOperation("根据数据Id查询子数据列表")
    @GetMapping("/findChildData/{id}")
    public Result findChildData(@PathVariable Long id){
        List<Dict> list = dictService.findChildData(id);
        return Result.ok(list);
    }

    @ApiOperation("根据dictCode和value查询")
    @GetMapping("/getName/{dictCode}/{value}")
    public String getName(@PathVariable String dictCode,
                          @PathVariable String value){
        return dictService.getDictName(dictCode,value);
    }

    @ApiOperation("根据value查询")
    @GetMapping("/getName/{value}")
    public String getName(@PathVariable String value){
        return dictService.getDictName("",value);
    }
}
