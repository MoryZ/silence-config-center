package com.old.silence.config.center.domain.service.event;

import jakarta.servlet.AsyncContext;
import jakarta.servlet.ServletResponse;
import org.springframework.stereotype.Component;
import com.old.silence.config.center.enums.EventType;
import com.old.silence.json.JacksonMapper;

import java.io.IOException;

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
        String jsonContent = JacksonMapper.getSharedInstance().toJson(content);
        ServletResponse response = context.getResponse();
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonContent);
        context.complete();
    }
}
