package com.da.eventauditservice.processor;

import com.da.eventauditservice.errorhandling.ValidationException;
import com.da.eventauditservice.testdata.TestData;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CreatedEventProcessorTest {

    private CreatedEventProcessor eventProcessor = new CreatedEventProcessor();

    @Test
    public void shouldProcessCreatedEventSuccessfully() throws ValidationException {
        Map<String, Integer> resultHolder = new LinkedHashMap<>();
        var result = eventProcessor.processEvent(TestData.created(1), resultHolder);
        assertThat(result).isEmpty();
        assertThat(resultHolder).isNotEmpty();
        assertThat(resultHolder).containsOnlyKeys("1");
    }

    @Test
    public void shouldThrowExceptionWhileRecreatingAnActiveToken() {
        Map<String, Integer> resultHolder = new LinkedHashMap<>();
        resultHolder.put("1", null);
        assertThatThrownBy(() -> eventProcessor.processEvent(TestData.created(1), resultHolder))
            .isExactlyInstanceOf(ValidationException.class)
            .hasMessage("Validation failure(recreation with active tokenId) for tokenId : {1}");
    }
}