package com.sjr.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sjr.yygh.com.client.DictFeignClient;
import com.sjr.yygh.hosp.repository.HospitalRepository;
import com.sjr.yygh.hosp.service.HospitalService;
import com.sjr.yygh.model.hosp.Hospital;
import com.sjr.yygh.vo.hosp.HospitalQueryVo;
import lombok.val;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import sun.dc.pr.PRError;

import java.util.*;

@Service
public class HospitalServiceImpl implements HospitalService {

    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private DictFeignClient dictFeignClient;

    @Override
    public void save(Map<String, Object> paramMap) {
        Hospital hospital = JSONObject.parseObject(JSONObject.toJSONString(paramMap),Hospital.class);
        //判断是否存在
        Hospital targetHospital = hospitalRepository.getHospitalByHoscode(hospital.getHoscode());
        if(null != targetHospital) {
            hospital.setStatus(targetHospital.getStatus());
            hospital.setCreateTime(targetHospital.getCreateTime());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);
        } else {
            //0：未上线 1：已上线
            hospital.setStatus(0);
            hospital.setCreateTime(new Date());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);
        }

    }

    @Override
    public Hospital getByHoscode(String hoscode) {
        return hospitalRepository.getHospitalByHoscode(hoscode);
    }

    @Override
    public Page<Hospital> selectHospPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo) {
//        创建Pageable对象
        Pageable pageable = PageRequest.of(page-1,limit);
//         创建条件匹配器
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)//模糊查询
                .withIgnoreCase(true);//忽略大小写
//        hospitalQueryVo转换为hospital对象
        Hospital hospital = new Hospital();
        BeanUtils.copyProperties(hospitalQueryVo,hospital);
//        创建Example对象
        Example<Hospital> example = Example.of(hospital,matcher);
//        调用方法实现查询
        Page<Hospital> pages = hospitalRepository.findAll(example, pageable);
        List<Hospital> content = pages.getContent();
//        获取查询list集合，遍历进行医院等级的封装
        pages.getContent().forEach(item->{
            this.setHospitalHosType(item);
        });
        return pages;
    }

    @Override
    public void updateStatus(String id, Integer status) {
//        根据Id查询医院信息
        Hospital hospital = hospitalRepository.findById(id).get();
//        设置修改的值
        hospital.setStatus(status);
        hospital.setUpdateTime(new Date());
        hospitalRepository.save(hospital);
    }
//    医院详情信息
    @Override
    public Map<String, Object> getHospById(String id) {
        Map<String, Object> result = new HashMap<>();
        Hospital hospital = this.setHospitalHosType(hospitalRepository.findById(id).get());
//        医院的基本信息(包含医院等级)
        result.put("hospital",hospital);
        //单独处理更直观
        result.put("bookingRule", hospital.getBookingRule());
        //不需要重复返回
        hospital.setBookingRule(null);
        return result;
    }
//    根据医院编号获取医院名称
    @Override
    public String getHospName(String hoscode) {
        Hospital hospital = hospitalRepository.getHospitalByHoscode(hoscode);
        if(hospital!=null){
            return hospital.getHosname();
        }
        return null;
    }
//    根据医院名称查询
    @Override
    public List<Hospital> findByHosname(String hosname) {
        return hospitalRepository.findHospitalByHosnameLike(hosname);
    }
//    根据医院编号获取医院预约挂号详情
    @Override
    public Map<String, Object> item(String hoscode) {
        Map<String, Object> result = new HashMap<>();
        //医院详情
        Hospital hospital = this.setHospitalHosType(this.getByHoscode(hoscode));
        result.put("hospital", hospital);
        //预约规则
        result.put("bookingRule", hospital.getBookingRule());
        //不需要重复返回
        hospital.setBookingRule(null);
        return result;
    }

    private Hospital setHospitalHosType(Hospital hospital){
//        根据dictCode和value获取医院等级名称
        String hostypeString = dictFeignClient.getName("Hostype", hospital.getHostype());
        String ProvinceString = dictFeignClient.getName(hospital.getProvinceCode());
        String CityString = dictFeignClient.getName(hospital.getCityCode());
        String DistrictString = dictFeignClient.getName(hospital.getDistrictCode());
        hospital.getParam().put("fullAddress",ProvinceString+CityString+DistrictString);
        hospital.getParam().put("hostypeString",hostypeString);
        return hospital;
    }
}
