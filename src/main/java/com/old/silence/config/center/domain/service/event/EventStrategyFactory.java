package com.old.silence.config.center.domain.service.event;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;
import com.old.silence.config.center.enums.EventType;

import java.util.HashMap;
import java.util.Map;

/**
 * @author moryzang
 */
@Component
public class EventStrategyFactory {

    private final Map<EventType, EventStrategy> eventStrategies = new HashMap<>();

    public EventStrategyFactory(ObjectProvider<EventStrategy> objectProvider) {
        Map<EventType, EventStrategy> strategyMap = objectProvider.orderedStream()
                .collect(HashMap::new, (m, v) -> m.put(v.getType(), v), HashMap::putAll);

        eventStrategies.putAll(strategyMap);
    }

    public EventStrategy getEventStrategy(EventType eventTypeEnum) {
        return eventStrategies.get(eventTypeEnum);
    }


}