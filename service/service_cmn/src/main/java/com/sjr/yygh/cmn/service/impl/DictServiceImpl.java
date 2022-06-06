package com.sjr.yygh.cmn.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sjr.yygh.cmn.mapper.DictMapper;
import com.sjr.yygh.cmn.service.DictService;
import com.sjr.yygh.model.cmn.Dict;
import com.sjr.yygh.model.hosp.HospitalSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    @Autowired
    private DictMapper dictMapper;

}
