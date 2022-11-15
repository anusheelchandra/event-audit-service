package com.da.eventauditservice.service;

import com.da.eventauditservice.errorhandling.ValidationException;
import com.da.eventauditservice.model.ActiveTokens;
import com.da.eventauditservice.model.Event;
import com.da.eventauditservice.processor.EventProcessorFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.da.eventauditservice.errorhandling.ErrorMessageUtil.NULL_EMPTY_EVENTS;
import static com.da.eventauditservice.errorhandling.ErrorMessageUtil.getValidationMessage;

@SuppressWarnings("unchecked")
@Slf4j
@RequiredArgsConstructor
public class EventProcessorHandler {

    private final EventProcessorFactory eventProcessorFactory;

    private static void checkForNullOrEmptyEvents(List<Event> events) throws ValidationException {
        if (CollectionUtils.isEmpty(events)) {
            throw new ValidationException(getValidationMessage(NULL_EMPTY_EVENTS));
        }
    }

    public ActiveTokens fetchActiveTokens(List<Event> events) throws ValidationException {
        checkForNullOrEmptyEvents(events);
        Map<String, Integer> resultHolder = new LinkedHashMap<>(); //we can also use LinkedHashSet
        processEvents(events, resultHolder);
        return new ActiveTokens(resultHolder.keySet());
    }

    private void processEvents(List<Event> events, Map<String, Integer> resultHolder) throws ValidationException {
        for (var event: events) {
            var consequences = eventProcessorFactory.getEventProcessor(event).processEvent(event, resultHolder);
            processEvents(consequences, resultHolder);
        }
    }

}
