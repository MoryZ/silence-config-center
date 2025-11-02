package com.old.silence.config.center.infrastructure.persistence.dao;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.old.silence.config.center.domain.model.ConfigAccessKeys;

@Mapper
public interface ConfigAccessKeysDao extends BaseMapper<ConfigAccessKeys> {

    @Select("SELECT access_key,secret_key from config_access_keys WHERE access_key = #{accessKey} AND is_enabled = #{enabled} ")
    ConfigAccessKeys findByAccessKey(String accessKey, Boolean enabled);
}