package com.old.silence.config.center.infrastructure.persistence.dao;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.old.silence.config.center.domain.model.ConfigEnvironment;
import com.old.silence.config.center.vo.ConfigEnvironmentVo;

import java.math.BigInteger;


@Mapper
public interface ConfigEnvironmentDao extends BaseMapper<ConfigEnvironment> {

    @Select(" SELECT config_component.code as 'componentCode', config_environment.name "
            + " FROM config_environment "
            + " LEFT JOIN config_component "
            + " ON config_environment.config_component_id = config_component.id "
            + " WHERE config_environment.id = #{id} "

    )
    ConfigEnvironmentVo findById(BigInteger id);

    @Select(" SELECT config_component.code as 'componentCode', config_environment.name "
            + " FROM config_environment "
            + " WHERE config_environment.config_component_id = #{configComponentId} AND name = #{environmentName} "

    )
    ConfigEnvironment findByConfigComponentIdAndName(BigInteger configComponentId, String environmentName);
}