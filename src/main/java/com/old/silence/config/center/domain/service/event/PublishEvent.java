package com.old.silence.config.center.domain.service.event;

import jakarta.servlet.AsyncContext;
import jakarta.servlet.ServletResponse;

import com.old.silence.json.JacksonMapper;
import com.old.silence.config.center.enums.EventType;

import java.io.IOException;

/**
 * @author moryzang
 */
public class PublishEvent implements  EventStrategy{
    @Override
    public EventType getType() {
        return EventType.PUBLISH;
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
