package com.da.eventauditservice.processor;


import com.da.eventauditservice.model.Created;
import com.da.eventauditservice.model.Event;
import com.da.eventauditservice.model.Used;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static com.da.eventauditservice.testdata.TestData.created;
import static com.da.eventauditservice.testdata.TestData.usedWithoutConsequences;
import static org.assertj.core.api.Assertions.assertThat;

public class EventProcessorFactoryTest {

    private EventProcessorFactory eventProcessorFactory;

    @BeforeEach
    public void setup() {
        var usedEventProcessor = new UsedEventProcessor();
        var createdEventProcessor = new CreatedEventProcessor();
        eventProcessorFactory = new EventProcessorFactory(Map.of(Created.class.getName(), createdEventProcessor,
            Used.class.getName(), usedEventProcessor));
    }

    @Test
    public void shouldGetEventProcessorForCreatedEvent() {
        assertThat(eventProcessorFactory.getEventProcessor(created(1)))
            .isInstanceOf(CreatedEventProcessor.class);
    }

    @Test
    public void shouldGetEventProcessorForUsedEvent() {
        assertThat(eventProcessorFactory.getEventProcessor(usedWithoutConsequences(1)))
            .isInstanceOf(UsedEventProcessor.class);
    }

    @Test
    public void shouldReturnWhileGettingEventProcessorForEvent() {
        assertThat(eventProcessorFactory.getEventProcessor(new Event("1"))).isNull();
    }
}