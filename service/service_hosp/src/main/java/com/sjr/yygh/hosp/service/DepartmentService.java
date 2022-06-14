package com.sjr.yygh.hosp.service;


import com.sjr.yygh.model.hosp.Department;
import com.sjr.yygh.vo.hosp.DepartmentQueryVo;
import com.sjr.yygh.vo.hosp.DepartmentVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface DepartmentService {
    void save(Map<String, Object> paramMap);
//    查询科室接口
    Page<Department> findPageDepartment(int page, int limit, DepartmentQueryVo departmentQueryVo);
//    删除科室接口
    void remove(String hoscode, String depcode);
//    查询医院所有科室列表
    List<DepartmentVo> findDeptTree(String hoscode);
//    根据科室编号和医院编号查询科室名称
    String getDepName(String hoscode, String depcode);
}
