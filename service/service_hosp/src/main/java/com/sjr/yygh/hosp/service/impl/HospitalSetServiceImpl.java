package com.sjr.yygh.hosp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
    //根据传递过来的医院编号，查询签名
    @Override
    public String getSignKey(String hoscode) {
        QueryWrapper<HospitalSet> hospitalSetQueryWrapper = new QueryWrapper<>();
        hospitalSetQueryWrapper.eq("hoscode",hoscode);
        HospitalSet hospitalSet = baseMapper.selectOne(hospitalSetQueryWrapper);
        return hospitalSet.getSignKey();
    }
}
