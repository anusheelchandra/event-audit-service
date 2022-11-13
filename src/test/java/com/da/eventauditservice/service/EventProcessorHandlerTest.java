package com.da.eventauditservice.service;


import com.da.eventauditservice.model.Created;
import com.da.eventauditservice.model.Event;
import com.da.eventauditservice.model.Used;
import com.da.eventauditservice.processor.CreatedEventProcessor;
import com.da.eventauditservice.processor.EventProcessor;
import com.da.eventauditservice.processor.UsedEventProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class EventProcessorHandlerTest {

    private EventProcessorHandler handler;
    private UsedEventProcessor usedEventProcessor;
    private CreatedEventProcessor createdEventProcessor;

    @BeforeEach
    public void setup() {
        usedEventProcessor = new UsedEventProcessor();
        createdEventProcessor = new CreatedEventProcessor();
        var eventProcessor = new EventProcessor(Map.of(Created.class.getName(), createdEventProcessor,
                Used.class.getName(), usedEventProcessor));
        handler = new EventProcessorHandler(eventProcessor);
    }

    @Test
    public void shouldFetchActiveTokens() {
        var result  = handler.fetchActiveTokens(buildEvents(1, false));
        assertThat(result.getTokenIds()).isNotNull();
        assertThat(result.getTokenIds()).hasSize(2);
        assertThat(result.getTokenIds()).containsExactly("2", "3");
    }

    @Test
    public void shouldFetchActiveTokensWhenReCreatingWithInactiveTokenId() {
        var result  = handler.fetchActiveTokens(buildEvents(1, true));
        assertThat(result.getTokenIds()).isNotNull();
        assertThat(result.getTokenIds()).hasSize(3);
        assertThat(result.getTokenIds()).containsExactly("2", "3", "1");
    }

    private List<Event> buildEvents() {
        var created1 = new Created("1");
        var used1 = new Used("1", false, List.of(new Created("2"),
                new Used("1", true, List.of(new Created("3")))));
        return List.of(created1, used1);
    }

    private List<Event> buildEvents(int id, boolean reCreateToken) {
        return reCreateToken ? List.of(created(id), used(id), created(id)) : List.of(created(id), used(id));
    }

    private Created created(int id) {
        return new Created(String.valueOf(id));
    }

    private Used used(int idToUse) {
        return new Used(String.valueOf(idToUse), false, List.of(new Created(String.valueOf(idToUse + 1)),
                new Used(String.valueOf(idToUse), true, List.of(new Created(String.valueOf(idToUse + 2))))));
    }
}