package com.sjr.yygh.hosp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sjr.yygh.hosp.mapper.HospitalSetMapper;
import com.sjr.yygh.hosp.service.HospitalSetService;
import com.sjr.yygh.model.hosp.HospitalSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HospitalSetServiceImpl extends ServiceImpl<HospitalSetMapper,HospitalSet> implements HospitalSetService {

    @Autowired
    private HospitalSetMapper hospitalSetMapper;

}
