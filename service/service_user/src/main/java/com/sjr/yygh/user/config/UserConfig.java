package com.sjr.yygh.user.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.sjr.yygh.user.mapper")
public class UserConfig {
}
