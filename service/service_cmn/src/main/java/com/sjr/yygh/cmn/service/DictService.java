package com.sjr.yygh.cmn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sjr.yygh.model.cmn.Dict;
import com.sjr.yygh.model.hosp.HospitalSet;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface DictService extends IService<Dict> {
    //    根据数据Id查询子数据列表
    List<Dict> findChildData(Long id);
    //    导出数据字典的接口
    void exportData(HttpServletResponse response);

    void importData(MultipartFile file);
}
