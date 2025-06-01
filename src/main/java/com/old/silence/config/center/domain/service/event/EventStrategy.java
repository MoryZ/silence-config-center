package com.old.silence.config.center.domain.service.event;

import com.old.silence.config.center.enums.EventType;

import javax.servlet.AsyncContext;
import java.io.IOException;

/**
 * @author moryzang
 */
public interface EventStrategy {

    EventType getType();

    void handleEvent(AsyncContext context, String content, String key) throws IOException;
}
