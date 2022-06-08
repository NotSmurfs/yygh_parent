package com.sjr.yygh.hosp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sjr.yygh.model.hosp.HospitalSet;

public interface HospitalSetService extends IService<HospitalSet> {
    //根据传递过来的医院编号，查询签名
    String getSignKey(String hoscode);
}
