package com.old.silence.config.center.api.config;

import jakarta.annotation.Nonnull;
import jakarta.security.auth.message.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.old.silence.config.center.api.config.annotation.SignatureAuth;
import com.old.silence.config.center.domain.service.DatabaseSecretManager;
import com.old.silence.config.center.domain.service.NonceManager;
import com.old.silence.config.center.domain.service.SignatureService;
import com.old.silence.core.context.CommonErrors;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author moryzang
 */
@Component
public class SignatureAuthInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(SignatureAuthInterceptor.class);
    private final NonceManager nonceManager;

    private final DatabaseSecretManager secretManager;

    private final SignatureService signatureService;

    public SignatureAuthInterceptor(NonceManager nonceManager, DatabaseSecretManager secretManager,
                                    SignatureService signatureService) {
        this.nonceManager = nonceManager;
        this.secretManager = secretManager;
        this.signatureService = signatureService;
    }


    @Override
    public boolean preHandle(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response,
                             @Nonnull Object handler) throws Exception {

        // 1. 检查是否为HandlerMethod
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        // 2. 检查是否需要签名认证
        SignatureAuth annotation = getSignatureAuthAnnotation(handlerMethod);
        if (annotation == null || !annotation.required()) {
            return true; // 跳过认证
        }

        // 3. 执行认证逻辑
        return doSignatureAuth(request, response);
    }

    /**
     * 获取签名认证注解（支持方法级别和类级别）
     */
    private SignatureAuth getSignatureAuthAnnotation(HandlerMethod handlerMethod) {
        // 先检查方法上的注解
        SignatureAuth methodAnnotation = handlerMethod.getMethodAnnotation(SignatureAuth.class);
        if (methodAnnotation != null) {
            return methodAnnotation;
        }

        // 再检查类上的注解
        return handlerMethod.getBeanType().getAnnotation(SignatureAuth.class);
    }

    /**
     * 执行签名认证
     */
    private boolean doSignatureAuth(HttpServletRequest request, HttpServletResponse response) throws IOException {

        try {
            // 1. 获取签名Header
            String accessKey = getHeader(request, "X-Ca-Key");
            String timestampStr = getHeader(request, "X-Ca-Timestamp");
            String nonce = getHeader(request, "X-Ca-Nonce");
            String signature = getHeader(request, "X-Ca-Signature");

            // 2. 验证Header完整性
            validateHeaders(accessKey, timestampStr, nonce, signature);

            // 3. 验证时间戳
            long timestamp = validateTimestamp(timestampStr);

            // 4. 验证nonce防重放
            validateNonce(nonce);

            // 5. 构建签名参数
            Map<String, String> signParams = buildSignParams(request);

            // 6. 获取secretKey并验证签名
            var configAccessKeys = secretManager.getConfigAccessKeys(accessKey);
            String secretKey = secretManager.getConfigAccessKeys(accessKey).getSecretKey();
            if (!signatureService.verifySignature(accessKey, secretKey, timestamp, nonce, signature, signParams)) {
                throw new AuthException("签名验证失败");
            }

            // 8. 缓存nonce（验证通过后才缓存）
            nonceManager.cacheNonce(nonce);

            log.debug("签名认证通过: accessKey={}, componentCode={}", accessKey, configAccessKeys.getComponentCode());
            return true;

        } catch (AuthException e) {
            log.warn("签名认证失败: {}", e.getMessage());
            sendErrorResponse(response, 401, e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("签名认证异常", e);
            sendErrorResponse(response, 500, "认证服务异常");
            return false;
        }
    }

    private String getHeader(HttpServletRequest request, String headerName) {
        String value = request.getHeader(headerName);
        return value != null ? value.trim() : null;
    }

    private void validateHeaders(String accessKey, String timestampStr,
                                 String nonce, String signature) {
        log.warn("1:{},2:{},3:{},4:{}", accessKey, timestampStr, nonce, signature);
        if (StringUtils.isAnyBlank(accessKey, timestampStr, nonce, signature)) {
            throw CommonErrors.INVALID_PARAMETER.createException("缺少必要的认证头信息");
        }

        if (!timestampStr.matches("\\d+")) {
            throw CommonErrors.INVALID_PARAMETER.createException("时间戳格式错误");
        }

        if (nonce.length() < 8) {
            throw CommonErrors.INVALID_PARAMETER.createException("随机数长度不足");
        }
    }

    private long validateTimestamp(String timestampStr) {
        long timestamp = Long.parseLong(timestampStr);
        long currentTime = System.currentTimeMillis();
        long diff = Math.abs(currentTime - timestamp);

        // 允许5分钟的时间偏差
        if (diff > 5 * 60 * 1000) {
            throw CommonErrors.INVALID_PARAMETER.createException("请求已过期");
        }

        return timestamp;
    }

    private void validateNonce(String nonce) {
        if (nonceManager.isNonceUsed(nonce)) {
            throw CommonErrors.INVALID_PARAMETER.createException("请求重复");
        }
    }

    private Map<String, String> buildSignParams(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();

        // URL参数
        request.getParameterMap().forEach((key, values) -> {
            if (values != null && values.length > 0) {
                params.put(key, values[0]);
            }
        });

        return params;
    }

    private void sendErrorResponse(HttpServletResponse response, int status, String message)
            throws IOException {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");

        Map<String, Object> error = new HashMap<>();
        error.put("code", status);
        error.put("message", message);
        error.put("timestamp", System.currentTimeMillis());
        error.put("path", RequestContextHolder.getRequestAttributes() != null ?
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getRequestURI() : "");

        response.getWriter().write(new ObjectMapper().writeValueAsString(error));
    }
}