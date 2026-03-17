package com.old.silence.config.center.domain.service.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.AsyncContext;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import com.old.silence.config.center.enums.EventType;

import java.io.IOException;
import java.util.Map;

/**
 * @author moryzang
 */
@Component
public class DeletedEvent implements EventStrategy {
    @Override
    public EventType getType() {
        return EventType.REMOVE;
    }

    @Override
    public void handleEvent(AsyncContext context, String content, String key) throws IOException {
        Map<String, Object> jsonResult = Map.of("code", 200, "message", "delete", "data", content);
        HttpServletResponse response = (HttpServletResponse) context.getResponse();

        if (response.isCommitted()) {
            return;
        }

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        // 直接写入输出流，避免字符串被再次序列化
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getOutputStream(), jsonResult);
        response.getOutputStream().flush();
        context.complete();
    }
}
