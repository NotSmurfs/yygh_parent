package com.sjr.yygh.cmn.service.impl;


import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sjr.yygh.cmn.mapper.DictMapper;
import com.sjr.yygh.cmn.service.DictService;
import com.sjr.yygh.model.cmn.Dict;
import com.sjr.yygh.model.hosp.HospitalSet;
import com.sjr.yygh.vo.cmn.DictEeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

//    根据id查询子数据
    @Override
    public List<Dict> findChildData(Long id) {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id",id);
        List<Dict> list = baseMapper.selectList(queryWrapper);
//        向list集合每个dict对象中设置hasChildren
        for (Dict dict : list) {
            dict.setHasChildren(this.isChildren(dict.getId()));
        }
        return list;
    }
    //    导出数据字典的接口
    @Override
    public void exportData(HttpServletResponse response) {
//        设置下载信息
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
// 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = "dict";
        response.setHeader("Content-disposition", "attachment;filename="+ fileName + ".xlsx");
//        查询数据库
        List<Dict> dictList = baseMapper.selectList(null);
//        Dict --DictEeVo
//        调用方法进行写操作
        EasyExcel.write(response.getOutputStream(), DictEeVo.class).sheet("dict")
                .doWrite();
    }

    //    判断Id下面是否有子数据
    private Boolean isChildren(Long id){
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id",id);
        Integer count = baseMapper.selectCount(queryWrapper);
        return count>0;
    }
}