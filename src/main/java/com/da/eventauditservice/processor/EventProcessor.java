package com.da.eventauditservice.processor;

import com.da.eventauditservice.model.Event;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class EventProcessor {

    private final Map<String, CreatedEventProcessor> processorMap;

    public CreatedEventProcessor getEventProcessor(Event event) {
        return processorMap.get(event.getClass().getName());
    }

}
