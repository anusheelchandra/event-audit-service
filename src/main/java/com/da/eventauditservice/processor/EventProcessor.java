package com.da.eventauditservice.processor;

import com.da.eventauditservice.errorhandling.ValidationException;
import com.da.eventauditservice.model.Event;

import java.util.List;
import java.util.Map;

public interface EventProcessor<T> {
    List<Event> processEvent(T event, Map<String, Integer> resultHolder) throws ValidationException;
}
