package com.sjr.yygh.cmn.service.impl;


import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sjr.yygh.cmn.listener.DictListener;
import com.sjr.yygh.cmn.mapper.DictMapper;
import com.sjr.yygh.cmn.service.DictService;
import com.sjr.yygh.model.cmn.Dict;
import com.sjr.yygh.model.hosp.HospitalSet;
import com.sjr.yygh.vo.cmn.DictEeVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

//    根据id查询子数据
    @Cacheable(value = "dict",keyGenerator = "keyGenerator")
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
        ArrayList<DictEeVo> dictVoList = new ArrayList<>();
        for (Dict dict : dictList) {
            DictEeVo dictEeVo = new DictEeVo();
            BeanUtils.copyProperties(dict,dictEeVo);
            dictVoList.add(dictEeVo);
        }
//        调用方法进行写操作
        try {
            EasyExcel.write(response.getOutputStream(), DictEeVo.class).sheet("dict")
                    .doWrite(dictVoList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    导入字典数据
    @CacheEvict(value = "dict", allEntries=true)
    @Override
    public void importData(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(),DictEeVo.class,new DictListener(baseMapper)).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//      根据dictCode和value查询医院等级
    @Override
    public String getDictName(String dictCode, String value) {
//        如果dictCode为空则直接根据value查询
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("value",value);
        if(StringUtils.isEmpty(dictCode)){
            Dict dict = baseMapper.selectOne(wrapper);
            return dict.getName();
        }else{
//          根据dict_code查询Id
            Dict dictByDictCode = this.getDictByDictCode(dictCode);
            Long id = dictByDictCode.getId();
//            根据dictCode和value查询医院等级
            wrapper.eq("parent_id",id);
            return baseMapper.selectOne(wrapper).getName();
        }
    }

    @Override
    public List<Dict> findByDictCode(String dictCode) {
//        根据dictCode获取对象id
        Dict dict = this.getDictByDictCode(dictCode);
//        根据Id获取子节点
        List<Dict> childData = this.findChildData(dict.getId());
        return childData;
    }

    //    根据dict_code查询Id
    private Dict getDictByDictCode(String dictCode){
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("dict_code",dictCode);
        return baseMapper.selectOne(wrapper);
    }

    //    判断Id下面是否有子数据
    private Boolean isChildren(Long id){
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id",id);
        Integer count = baseMapper.selectCount(queryWrapper);
        return count>0;
    }
}
