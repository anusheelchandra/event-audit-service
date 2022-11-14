package com.da.eventauditservice.service;


import com.da.eventauditservice.errorhandling.ValidationException;
import com.da.eventauditservice.model.Created;
import com.da.eventauditservice.model.Used;
import com.da.eventauditservice.processor.CreatedEventProcessor;
import com.da.eventauditservice.processor.EventProcessorFactory;
import com.da.eventauditservice.processor.UsedEventProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.da.eventauditservice.testdata.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class EventProcessorHandlerTest {

    private EventProcessorHandler handler;

    @BeforeEach
    public void setup() {
        var usedEventProcessor = new UsedEventProcessor();
        var createdEventProcessor = new CreatedEventProcessor();
        var eventProcessor = new EventProcessorFactory(Map.of(Created.class.getName(), createdEventProcessor,
                                                              Used.class.getName(), usedEventProcessor));
        handler = new EventProcessorHandler(eventProcessor);
    }

    @Test
    public void shouldFetchActiveTokens() throws ValidationException {
        var result  = handler.fetchActiveTokens(buildEvents(1, false));
        assertThat(result.getTokenIds()).isNotNull();
        assertThat(result.getTokenIds()).hasSize(2);
        assertThat(result.getTokenIds()).containsExactly("2", "3");
    }

    @Test
    public void shouldFetchActiveTokensWhenReCreatingWithInactiveTokenId() throws ValidationException {
        var result  = handler.fetchActiveTokens(buildEvents(1, true));
        assertThat(result.getTokenIds()).isNotNull();
        assertThat(result.getTokenIds()).hasSize(3);
        assertThat(result.getTokenIds()).containsExactly("2", "3", "1");
    }

    @Test
    public void shouldThrowExceptionForNullEventList() {
        assertThatThrownBy(() -> handler.fetchActiveTokens(null))
            .isExactlyInstanceOf(ValidationException.class)
            .hasMessage("Validation failure(input events are null/empty)");
    }

    @Test
    public void shouldThrowExceptionForEmptyEventList() {
        assertThatThrownBy(() -> handler.fetchActiveTokens(Collections.emptyList()))
            .isExactlyInstanceOf(ValidationException.class)
            .hasMessage("Validation failure(input events are null/empty)");
    }

    @Test
    public void shouldThrowExceptionTryingToUseNonExistingToken() {
        assertThatThrownBy(() -> handler.fetchActiveTokens(List.of(usedWithConsequences(1))))
            .isExactlyInstanceOf(ValidationException.class)
            .hasMessage("Validation failure(using/consuming an non-existing/inactive token) for tokenId : {1}");
    }

    @Test
    public void shouldThrowExceptionTryingToReCreateAnActiveToken() {
        assertThatThrownBy(() -> handler.fetchActiveTokens(List.of(created(1), created(1))))
                .isExactlyInstanceOf(ValidationException.class)
                .hasMessage("Validation failure(recreation with active tokenId) for tokenId : {1}");
    }

}