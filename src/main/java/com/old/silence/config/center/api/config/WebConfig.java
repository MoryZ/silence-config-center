package com.old.silence.config.center.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author moryzang
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final SignatureAuthInterceptor signatureAuthInterceptor;

    public WebConfig(SignatureAuthInterceptor signatureAuthInterceptor) {
        this.signatureAuthInterceptor = signatureAuthInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(signatureAuthInterceptor)
                .addPathPatterns("/**")  // 拦截所有路径
                .excludePathPatterns("/health", "/actuator/**", "/public/**"); // 排除路径
    }
}