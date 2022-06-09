package com.sjr.yygh.hosp.service;


import com.sjr.yygh.model.hosp.Department;
import com.sjr.yygh.vo.hosp.DepartmentQueryVo;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface DepartmentService {
    void save(Map<String, Object> paramMap);
//    查询科室接口
    Page<Department> findPageDepartment(int page, int limit, DepartmentQueryVo departmentQueryVo);
//    删除科室接口
    void remove(String hoscode, String depcode);
}
