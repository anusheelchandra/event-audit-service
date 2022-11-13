package com.da.eventauditservice.processor;

import com.da.eventauditservice.model.Created;
import com.da.eventauditservice.model.Event;
import com.da.eventauditservice.model.Used;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
@Slf4j
@RequiredArgsConstructor
public class CreatedEventProcessor {

    private static UsedEventProcessor usedEventProcessor = new UsedEventProcessor();

    public void processEvents(List<Event> events, Map<String, Integer> resultMap) {
        for (var event: events) {
            processEvent(event, resultMap);
        }
    }

    public void processEvent(Event event, Map<String, Integer> resultMap) {
        try {
            if (event instanceof Created) {
                log.info(String.format("Processing Created event : %s", (event.getTokenId())));
                assert resultMap.get(event.getTokenId()) == null : getValidationMessage(event.getTokenId());
                resultMap.put(event.getTokenId(), null);
            }
            if (event instanceof Used userEvent) {
                usedEventProcessor.processEvent(userEvent, resultMap);
            }
        } catch (Exception exception) {
            log.error(String.format("%s : %s", exception.getMessage(), event.getTokenId()));
        }
    }

    private static String getValidationMessage(String tokenId) {
        return String.format("Validation failure(recreation with active tokenId) for tokenId : %s", tokenId);
    }
}
