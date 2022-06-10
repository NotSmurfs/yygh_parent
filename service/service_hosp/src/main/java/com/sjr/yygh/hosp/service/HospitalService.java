package com.sjr.yygh.hosp.service;

import com.sjr.yygh.model.hosp.Hospital;
import com.sjr.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface HospitalService {
//    上传医院接口
    void save(Map<String, Object> paramMap);
//    根据医院编号进行查询
    Hospital getByHoscode(String hoscode);
//    医院列表(条件查询分页)
    Page<Hospital> selectHospPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo);
}
