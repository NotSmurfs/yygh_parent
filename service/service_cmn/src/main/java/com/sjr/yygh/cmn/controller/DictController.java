package com.sjr.yygh.cmn.controller;

import com.sjr.yygh.cmn.service.DictService;
import com.sjr.yygh.common.result.Result;
import com.sjr.yygh.model.cmn.Dict;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

//    导出数据字典的接口
    @ApiOperation(value="导出")
    @GetMapping(value = "/exportData")
    public Result exportData(HttpServletResponse response) {
        dictService.exportData(response);
        return Result.ok();
    }


    @ApiOperation("根据数据Id查询子数据列表")
    @GetMapping("/findChildData/{id}")
    public Result findChildData(@PathVariable Long id){
        List<Dict> list = dictService.findChildData(id);
        return Result.ok(list);
    }


}
