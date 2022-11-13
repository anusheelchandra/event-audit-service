package com.da.eventauditservice.service;

import com.da.eventauditservice.model.Created;
import com.da.eventauditservice.model.Event;
import com.da.eventauditservice.model.Used;
import com.da.eventauditservice.processor.UsedEventProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class EventServiceTest {

    private UsedEventProcessor eventProcessor;

    private EventService eventService;

    @BeforeEach
    public void setup() {
        //eventProcessor = new UsedEventProcessor();
        //eventService = new EventService(eventProcessor);
        eventService = new EventService();
    }

    @Test
    public void shouldGetActiveTokens() {
        var result = eventService.getActiveTokens(buildEvents());
        assertThat(result).isNotNull();
        assertThat(result.getTokenIds()).isNotNull();
        assertThat(result.getTokenIds()).hasSize(3);
        assertThat(result.getTokenIds()).containsExactly("2", "3", "1");
    }

    private List<Event> buildEvents() {
        var created1 = new Created("1");
        var used1 = new Used("1", false, List.of(new Created("2"),
                new Used("1", true, List.of(new Created("3")))));
        return List.of(created1, used1, created1);
    }
}