package com.old.silence.config.center.infrastructure.persistence.dao;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.old.silence.config.center.domain.model.ConfigItem;
import com.old.silence.config.center.enums.ConfigItemFormatType;
import com.old.silence.config.center.enums.NameSpaceStatus;
import com.old.silence.config.center.vo.ConfigReleaseVo;

import java.math.BigInteger;


@Mapper
public interface ConfigItemDao extends BaseMapper<ConfigItem> {

    @Select(" SELECT ci.content "
            +" FROM config_item ci "
            +" JOIN config_environment ce ON ci.config_environment_id = ce.id "
            +" JOIN config_component cc ON ce.config_component_id = cc.id "
            +" WHERE ci.format_type = #{formatType.value} "
            +" AND ci.namespace_id = #{namespaceId} "
            +" AND ci.namespace_status = #{namespaceStatus} "
            +" AND ce.name = #{env} "
            +" AND cc.code = #{componentCode} "
    )
    String findByNameSpaceIdAndEnvNameAndComponentCodeAndFormatType(@Param("namespaceId") String namespaceId, @Param("env") String env,
                                                                    @Param("componentCode") String componentCode, @Param("formatType") ConfigItemFormatType formatType, @Param("namespaceStatus") NameSpaceStatus namespaceStatus);

    @Select(" SELECT config_component.code, config_environment.name as 'env', config_item.namespace_id as 'namespaceId' "
           +" FROM config_item "
           +" LEFT JOIN config_environment "
           +" ON config_item.config_environment_id = config_environment.id "
           +" LEFT JOIN config_component "
           +" ON config_environment.config_component_id = config_component.id "
           +" WHERE config_item.id = #{id} "
    )
    ConfigReleaseVo findReleaseInfoById(BigInteger id);

    @Update("UPDATE config_item SET namespace_status = #{nameSpaceStatus} WHERE id = #{id} ")
    int updateNamespaceStatusById(NameSpaceStatus nameSpaceStatus, BigInteger id);
}