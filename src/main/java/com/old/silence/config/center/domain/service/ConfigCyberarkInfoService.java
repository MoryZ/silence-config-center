package com.old.silence.config.center.domain.service;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.old.silence.config.center.domain.model.ConfigCyberarkInfo;
import com.old.silence.config.center.domain.repository.ConfigCyberarkInfoRepository;
import com.old.silence.config.center.dto.ConfigCyberarkInfoRequest;
import com.old.silence.config.center.util.AESUtils;
import com.old.silence.config.center.vo.ConfigCyberarkInfoVo;
import com.old.silence.core.context.CommonErrors;

import java.time.Instant;
import java.util.UUID;

/**
 * @author moryzang
 */
@Service
public class ConfigCyberarkInfoService {


    private static final Logger log = LoggerFactory.getLogger(ConfigCyberarkInfoService.class);
    private final ConfigCyberarkInfoRepository configCyberarkInfoRepository;

    public ConfigCyberarkInfoService(ConfigCyberarkInfoRepository configCyberarkInfoRepository) {
        this.configCyberarkInfoRepository = configCyberarkInfoRepository;
    }

    public ConfigCyberarkInfoVo findByRemoteRequest(ConfigCyberarkInfoRequest configCyberarkInfoRequest) {
        // 1.校验签名
        var password = validateSignature(configCyberarkInfoRequest);


        //3.拼接返回
        return buildResponse(configCyberarkInfoRequest, password);
    }

    /**
     * 1. 校验签名
     */
    private String validateSignature(ConfigCyberarkInfoRequest request) {
        if (request == null || StringUtils.isBlank(request.getAppId()) || StringUtils.isBlank(request.getSignature())) {
            throw CommonErrors.INVALID_PARAMETER.createException("appId/signature");
        }

        // 根据appId查询对应的appKey
        var configCyberarkInfo = configCyberarkInfoRepository.findByComponentCodeAndCyberarkObject(request.getAppId(), request.getObject());
        if (configCyberarkInfo == null) {
            log.error("appId {} has no secret key", request.getAppId());
            throw CommonErrors.INVALID_PARAMETER.createException(request.getAppId());
        }

        // 生成预期签名
        String expectedSignature = generateSignature(request.getAppId(), configCyberarkInfo.getAppKey());

        // 比较签名
        if (!expectedSignature.equals(request.getSignature())) {
            log.error("appId {} has signature of {} ", request.getAppId(), request.getSignature());
            throw CommonErrors.ACCESS_DENIED.createException("签名验证失败!");
        }
        return configCyberarkInfo.getEncryptedValue();
    }

    /**
     * 生成签名（与服务端一致）
     */
    private String generateSignature(String appId, String secretKey) {
        String signContent = appId + '&' + secretKey;
        return Hex.encodeHexString(DigestUtils.sha1(signContent), false);
    }

    /**
     * 2. 查找密码映射
     */
    private String findPasswordByComponentCodeAndCyberarkObject(String componentCode, String object) {
        var codeAndCyberarkObject = configCyberarkInfoRepository.findByComponentCodeAndCyberarkObject(componentCode, object);
       if (codeAndCyberarkObject == null) {
            throw CommonErrors.DATA_NOT_EXIST.createException("未找到密码映射: componentCode=" + componentCode + ", object=" + object);
       }
        return codeAndCyberarkObject.getEncryptedValue();
    }

    /**
     * 3. 拼接返回
     */
    private ConfigCyberarkInfoVo buildResponse(ConfigCyberarkInfoRequest request, String password) {
        ConfigCyberarkInfoVo response = new ConfigCyberarkInfoVo();
        response.setAppId(request.getAppId());
        response.setSafe(request.getSafe());
        response.setObject(request.getObject());
        response.setReason(request.getReason());
        response.setRequestId(request.getRequestId());
        response.setResponseId(generateResponseId());
        response.setPassword(password);
        response.setResponseTime(Instant.now());
        response.setSignature(request.getSignature());

        return response;
    }

    private String generateResponseId() {
        return "RESP_" + System.currentTimeMillis() + "_" +
                UUID.randomUUID().toString().substring(0, 8);
    }

    public void create(ConfigCyberarkInfo configCyberarkInfo) {
        String cipherText = encryptValueIfNecessary(configCyberarkInfo);
        configCyberarkInfo.setEncryptedValue(cipherText);
        configCyberarkInfoRepository.create(configCyberarkInfo);
    }

    public void update(ConfigCyberarkInfo configCyberarkInfo) {
        String cipherText = encryptValueIfNecessary(configCyberarkInfo);
        configCyberarkInfo.setEncryptedValue(cipherText);
        configCyberarkInfoRepository.update(configCyberarkInfo);
    }

    private String encryptValueIfNecessary(ConfigCyberarkInfo configCyberarkInfo) {

        return AESUtils.encrypt(configCyberarkInfo.getEncryptedValue(), configCyberarkInfo.getAppKey());
    }


}
