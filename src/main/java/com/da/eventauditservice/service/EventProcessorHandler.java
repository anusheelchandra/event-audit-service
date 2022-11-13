package com.da.eventauditservice.service;

import com.da.eventauditservice.model.ActiveTokens;
import com.da.eventauditservice.model.Event;
import com.da.eventauditservice.processor.EventProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class EventProcessorHandler {

    //private final CreatedEventProcessor createdEventProcessor;
    private final EventProcessor eventProcessor;

    public ActiveTokens fetchActiveTokens(List<Event> events) {
        Map<String, Integer> resultMap = new LinkedHashMap<>();
        processEvents(events, resultMap);
        return new ActiveTokens(resultMap.keySet());
    }

    private void processEvents(List<Event> events, Map<String, Integer> resultMap) {
        for (var event: events) {
            eventProcessor.getEventProcessor(event).processEvent(event, resultMap);
        }
    }
 /*
    private void processEvent(Event event, Map<String, Integer> resultMap) {
        if (event instanceof Created) {
            createdEventProcessor.processEvent(event, resultMap);
        } else if (event instanceof Used usedEvent) {
            usedEventProcessor.processEvent(usedEvent, resultMap);
            if (!usedEvent.getConsequences().isEmpty()) {
                processEvents(usedEvent.getConsequences(), resultMap);
            }
        }
    }*/
}
