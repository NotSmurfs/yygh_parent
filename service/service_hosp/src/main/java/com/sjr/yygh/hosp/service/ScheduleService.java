package com.sjr.yygh.hosp.service;

import com.sjr.yygh.model.hosp.Schedule;
import com.sjr.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface ScheduleService {
//    上传排班接口
    void save(Map<String, Object> paramMap);
//    查询排班接口
    Page<Schedule> findPageSchedule(int page, int limit, ScheduleQueryVo scheduleQueryVo);
//    删除排班接口
    void remove(String hoscode, String hosScheduleId);
//    根据医院编号和科室编号查询排班规则数据
    Map<String, Object> getRuleSchedule(long page, long limit, String hoscode, String depcode);
//    根据医院编号、科室编号和工作日期，查询排班详细信息
    List<Schedule> getDetailSchedule(String hoscode, String depcode, String workDate);
}
