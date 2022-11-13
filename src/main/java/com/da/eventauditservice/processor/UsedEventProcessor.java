package com.da.eventauditservice.processor;

import com.da.eventauditservice.model.Used;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class UsedEventProcessor extends CreatedEventProcessor{

    public void processEvent(Used event, Map<String, Integer> resultMap) {
        log.info(String.format("Processing Used event : %s", event.getTokenId()));
        try {
            if (event.getConsumed()) {
                log.info(String.format("Processing Consumed event, token to be removed  : %s", event.getTokenId()));
                assert resultMap.get(event.getTokenId()) == null : getValidationMessage(event.getTokenId());
                resultMap.remove(event.getTokenId());
            }
            if (event.getConsequences() != null) {
                super.processEvents(event.getConsequences(), resultMap);
            }
        } catch (Exception exception) {
            log.error(String.format("%s: %s",exception.getMessage(), event.getTokenId()));
        }

    }

    private static String getValidationMessage(String tokenId) {
        return String.format("Validation failure(consuming an archive/inactive) for tokenId: %s", tokenId);
    }

}
