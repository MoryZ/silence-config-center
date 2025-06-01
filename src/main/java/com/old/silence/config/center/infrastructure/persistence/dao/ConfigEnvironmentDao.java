package com.old.silence.config.center.infrastructure.persistence.dao;


import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.old.silence.config.center.domain.model.ConfigEnvironment;


@Mapper
public interface ConfigEnvironmentDao extends BaseMapper<ConfigEnvironment> {

}