package com.old.silence.config.center.infrastructure.persistence.dao;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.old.silence.config.center.domain.model.ConfigItem;
import com.old.silence.config.center.enums.ConfigItemFormatType;
import com.old.silence.config.center.vo.ConfigReleaseVo;

import java.math.BigInteger;


@Mapper
public interface ConfigItemDao extends BaseMapper<ConfigItem> {

    String findByCriteria(@Param("namespaceId") String namespaceId, @Param("env") String env,
                          @Param("componentCode") String componentCode, @Param("formatType") ConfigItemFormatType formatType);

    ConfigReleaseVo findReleaseInfoById(BigInteger id);
}