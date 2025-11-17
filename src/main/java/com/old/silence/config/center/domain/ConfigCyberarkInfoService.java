package com.old.silence.config.center.domain;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import com.old.silence.config.center.domain.model.ConfigAccessKeys;
import com.old.silence.config.center.domain.model.ConfigCyberarkInfo;
import com.old.silence.config.center.domain.repository.ConfigAccessKeysRepository;
import com.old.silence.config.center.domain.repository.ConfigCyberarkInfoRepository;
import com.old.silence.config.center.dto.ConfigCyberarkInfoRequest;
import com.old.silence.config.center.vo.ConfigCyberarkInfoVo;
import com.old.silence.core.context.CommonErrors;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.util.UUID;

/**
 * @author moryzang
 */
@Service
public class ConfigCyberarkInfoService {

    private static final String AES_TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final byte[] IV = "pidms20180327!@#".getBytes(StandardCharsets.UTF_8);

    private final ConfigCyberarkInfoRepository configCyberarkInfoRepository;
    private final ConfigAccessKeysRepository configAccessKeysRepository;

    public ConfigCyberarkInfoService(ConfigCyberarkInfoRepository configCyberarkInfoRepository,
                                     ConfigAccessKeysRepository configAccessKeysRepository) {
        this.configCyberarkInfoRepository = configCyberarkInfoRepository;
        this.configAccessKeysRepository = configAccessKeysRepository;
    }

    public ConfigCyberarkInfoVo findByRemoteRequest(ConfigCyberarkInfoRequest configCyberarkInfoRequest) {
        // 1.校验签名
        validateSignature(configCyberarkInfoRequest);
        // 2.查找密码映射
        var password = findPasswordByComponentCodeAndCyberarkObject(configCyberarkInfoRequest.getAppId(), configCyberarkInfoRequest.getObject());

        //3.拼接返回
        return buildResponse(configCyberarkInfoRequest, password);
    }

    /**
     * 1. 校验签名
     */
    private void validateSignature(ConfigCyberarkInfoRequest request) {
        // 根据appId查询对应的appKey
        var configAccessKeys = configAccessKeysRepository.findByAccessKey(request.getAppId());
        if (configAccessKeys == null) {
            throw CommonErrors.INVALID_PARAMETER.createException("appId");
        }

        // 生成预期签名
        String expectedSignature = generateSignature(request.getAppId(), configAccessKeys.getAccessKey());

        // 比较签名
        if (!expectedSignature.equals(request.getSignature())) {
            throw CommonErrors.ACCESS_DENIED.createException("签名验证失败!");
        }
    }

    /**
     * 生成签名（与服务端一致）
     */
    private String generateSignature(String appId, String appKey) {
        String signContent = appId + '&' + appKey;
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
        ConfigAccessKeys accessKeys = configAccessKeysRepository.findByComponentCode(configCyberarkInfo.getComponentCode());
        if (accessKeys == null || StringUtils.isBlank(accessKeys.getSecretKey())) {
            throw CommonErrors.DATA_NOT_EXIST.createException("appKey 未配置, componentCode=" + configCyberarkInfo.getComponentCode());
        }
        return encrypt(configCyberarkInfo.getEncryptedValue(), accessKeys.getSecretKey());
    }

    private String encrypt(String plainText, String appKey) {
        try {
            Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
            SecretKeySpec secretKeySpec = new SecretKeySpec(appKey.getBytes(StandardCharsets.UTF_8), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, new IvParameterSpec(IV));
            byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            return Hex.encodeHexString(encrypted);
        } catch (GeneralSecurityException e) {
            throw CommonErrors.FATAL_ERROR.createException("加密失败");
        }
    }
}
