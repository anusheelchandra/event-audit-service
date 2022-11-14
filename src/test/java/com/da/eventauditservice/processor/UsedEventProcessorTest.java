package com.da.eventauditservice.processor;

import com.da.eventauditservice.errorhandling.ValidationException;
import com.da.eventauditservice.model.Created;
import com.da.eventauditservice.model.Used;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.da.eventauditservice.testdata.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UsedEventProcessorTest {

    private UsedEventProcessor eventProcessor = new UsedEventProcessor();

    @Test
    public void shouldProcessUsedEventWithoutConsequencesSuccessfully() throws ValidationException {
        Map<String, Integer> resultHolder = new LinkedHashMap<>();
        resultHolder.put("1", null);
        var result = eventProcessor.processEvent(usedWithoutConsequences(1), resultHolder);
        assertThat(result).isEmpty();
        assertThat(resultHolder).isNotEmpty();
        assertThat(resultHolder).hasSize(1);
        assertThat(resultHolder).containsKey("1");
    }

    @Test
    public void shouldProcessConsumeEventWithoutConsequencesSuccessfully() throws ValidationException {
        Map<String, Integer> resultHolder = new LinkedHashMap<>();
        resultHolder.put("1", null);
        var result = eventProcessor.processEvent(consumeWithoutConsequences(1), resultHolder);
        assertThat(result).isEmpty();
        assertThat(resultHolder).isEmpty();
    }

    @Test
    public void shouldProcessUsedEventWithConsequenceAndReturnCreatedAsConsequence() throws ValidationException {
        Map<String, Integer> resultHolder = new LinkedHashMap<>();
        resultHolder.put("1", null);
        var result = eventProcessor.processEvent(usedWithCreatedAsConsequence(1), resultHolder);
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isInstanceOf(Created.class);
        assertThat(result.get(0).getTokenId()).isEqualTo("2");
        assertThat(resultHolder).isNotEmpty();
        assertThat(resultHolder).hasSize(1);
        assertThat(resultHolder).containsKey("1");
    }

    @Test
    public void shouldProcessUsedEventWithConsequenceAndReturnUsedAsConsequence() throws ValidationException {
        Map<String, Integer> resultHolder = new LinkedHashMap<>();
        resultHolder.put("1", null);
        var result = eventProcessor.processEvent(usedWithUsedAsConsequence(1), resultHolder);
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isInstanceOf(Used.class);
        assertThat(result.get(0).getTokenId()).isEqualTo("1");
        assertThat(resultHolder).isNotEmpty();
        assertThat(resultHolder).hasSize(1);
        assertThat(resultHolder).containsKey("1");
    }

    @Test
    public void shouldThrowExceptionTryingToUseInactiveToken() {
        Map<String, Integer> resultHolder = new LinkedHashMap<>();
        resultHolder.put("1", null);
        assertThatThrownBy(() -> eventProcessor.processEvent(usedWithoutConsequences(2), resultHolder))
            .isExactlyInstanceOf(ValidationException.class)
            .hasMessage("Validation failure(using/consuming an non-existing/inactive token) for tokenId : {2}");
    }

    @Test
    public void shouldThrowExceptionTryingToConsumeInactiveToken() {
        Map<String, Integer> resultHolder = new LinkedHashMap<>();
        resultHolder.put("1", null);
        assertThatThrownBy(() -> eventProcessor.processEvent(consumeWithoutConsequences(2), resultHolder))
            .isExactlyInstanceOf(ValidationException.class)
            .hasMessage("Validation failure(using/consuming an non-existing/inactive token) for tokenId : {2}");
    }
}