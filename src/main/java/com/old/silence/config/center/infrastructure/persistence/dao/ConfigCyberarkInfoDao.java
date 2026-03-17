package com.old.silence.config.center.infrastructure.persistence.dao;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.old.silence.config.center.domain.model.ConfigCyberarkInfo;


@Mapper
public interface ConfigCyberarkInfoDao extends BaseMapper<ConfigCyberarkInfo> {

    @Select("SELECT id, component_code AS componentCode, cyberark_object AS cyberarkObject, encrypted_value AS encryptedValue, " +
            "app_key AS appKey, safe, folder, description, is_enabled AS enabled " +
            "FROM config_cyberark_info WHERE component_code = #{componentCode} " +
            "AND cyberark_object = #{cyberarkObject} " +
            "AND is_enabled = #{enabled}")
    ConfigCyberarkInfo findByComponentCodeAndCyberarkObject(String componentCode, String cyberarkObject, Boolean enabled);
}