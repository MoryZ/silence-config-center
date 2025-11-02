package com.old.silence.config.center.infrastructure.persistence.dao;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.old.silence.config.center.domain.model.ConfigAccessKeys;
import com.old.silence.config.center.domain.model.ConfigCyberarkInfo;

import java.util.stream.Stream;

@Mapper
public interface ConfigCyberarkInfoDao extends BaseMapper<ConfigCyberarkInfo> {

    @Select("SELECT access_key,secret_key from config_cyberark_info WHERE component_code = #{componentCode} " +
            " AND cyberark_object = #{cyberarkObject} "+
            " AND is_enabled = #{enabled} ")
    ConfigCyberarkInfo findByComponentCodeAndCyberarkObject(String componentCode, String cyberarkObject, Boolean enabled);
}