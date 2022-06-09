package com.sjr.yygh.hosp.repository;

import com.sjr.yygh.model.hosp.Department;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends MongoRepository<Department,String> {
//    根据医院编号和科室编号来查询科室信息
    Department getDepartmentByHoscodeAndDepcode(String hoscode, String depcode);
}
