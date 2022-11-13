package com.da.eventauditservice.service;


import com.da.eventauditservice.model.ActiveTokens;
import com.da.eventauditservice.model.Created;
import com.da.eventauditservice.model.Event;
import com.da.eventauditservice.model.Used;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@RequiredArgsConstructor
@Service
public class EventService {

    private static final Logger LOGGER = Logger.getLogger(EventService.class.getName());

    //private final UsedEventProcessor eventProcessor;

    public ActiveTokens getActiveTokens(List<Event> events) {
        Map<String, Integer> resultMap = new LinkedHashMap<>();
        processEvents(events, resultMap);
        return new ActiveTokens(resultMap.keySet());
    }

    private void processEvents(List<Event> events, Map<String, Integer> resultMap) {
        for (var event: events) {
            processEvent(event, resultMap);
        }
    }

    private void processEvent(Event event, Map<String, Integer> resultMap) {
        if (event instanceof Created) {
            LOGGER.info(String.format("Processing Created event : %s", (event.getTokenId())));
            resultMap.put(event.getTokenId(), null);
        } else if (event instanceof Used) {
            LOGGER.info(String.format("Processing Used event : %s", event.getTokenId()));
            var usedTokenId = event.getTokenId();
            var consequences = ((Used) event).getConsequences();
            if (((Used) event).getConsumed()) {
                LOGGER.info(String.format("Token to be removed  : %s", usedTokenId));
                resultMap.remove(usedTokenId);
            }
            if (!consequences.isEmpty()) {
                processEvents(consequences, resultMap);
            }
        }
    }

}
