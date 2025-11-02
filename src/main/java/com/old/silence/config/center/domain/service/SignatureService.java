package com.old.silence.config.center.domain.service;

import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

/**
 * @author moryzang
 */
@Component
public class SignatureService {

    private static final String HMAC_SHA256 = "HmacSHA256";

    /**
     * 生成配置中心请求签名
     */
    public ConfigSignature generateSignature(String accessKey, String secretKey,
                                             String nonce,
                                             long timestamp,
                                             Map<String, String> params) {
        // 构建签名字符串
        String content = buildSignContent(accessKey, timestamp, nonce, params);

        // 计算签名
        String signature = hmacSha256(secretKey, content);

        return new ConfigSignature(accessKey, timestamp, nonce, signature);
    }

    /**
     * 构建签名字符串
     * 格式: accessKey + timestamp + nonce + sorted(params)
     */
    private String buildSignContent(String accessKey, long timestamp, String nonce,
                                    Map<String, String> params) {
        StringBuilder content = new StringBuilder();
        content.append(accessKey).append(timestamp).append(nonce);

        // 参数按字典序排序
        if (params != null && !params.isEmpty()) {
            params.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(entry -> content.append(entry.getKey()).append(entry.getValue()));
        }

        return content.toString();
    }

    /**
     * HMAC-SHA256 核心实现
     */
    private String hmacSha256(String secretKey, String content) {
        try {
            Mac mac = Mac.getInstance(HMAC_SHA256);
            SecretKeySpec secretKeySpec = new SecretKeySpec(
                    secretKey.getBytes(StandardCharsets.UTF_8), HMAC_SHA256);
            mac.init(secretKeySpec);
            byte[] bytes = mac.doFinal(content.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            throw new RuntimeException("HMAC-SHA256签名计算失败", e);
        }
    }

    /**
     * 生成随机数
     */
    private String generateNonce() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }

    /**
     * 验证签名
     */
    public boolean verifySignature(String accessKey, String secretKey, long timestamp,
                                   String nonce, String signature, Map<String, String> params) {
        // 验证时间戳（5分钟内有效）
        if (System.currentTimeMillis() - timestamp > 5 * 60 * 1000) {
            return false;
        }

        String expectedSign = generateSignature(accessKey, secretKey, nonce, timestamp, params).getSignature();
        return expectedSign.equals(signature);
    }

    /**
     * 签名结果对象
     */
    public static class ConfigSignature {
        private String accessKey;
        private long timestamp;
        private String nonce;
        private String signature;

        public ConfigSignature() {
        }

        public ConfigSignature(String accessKey, long timestamp, String nonce, String signature) {
            this.accessKey = accessKey;
            this.timestamp = timestamp;
            this.nonce = nonce;
            this.signature = signature;
        }

        public String getAccessKey() {
            return accessKey;
        }

        public void setAccessKey(String accessKey) {
            this.accessKey = accessKey;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public String getNonce() {
            return nonce;
        }

        public void setNonce(String nonce) {
            this.nonce = nonce;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }
    }
}