package com.da.eventauditservice.processor;

import com.da.eventauditservice.model.Event;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@SuppressWarnings("rawtypes")
@RequiredArgsConstructor
public class EventProcessorFactory {

    private final Map<String, EventProcessor> processorMap;

    public EventProcessor getEventProcessor(Event event) {
        return processorMap.get(event.getClass().getName());
    }

}
