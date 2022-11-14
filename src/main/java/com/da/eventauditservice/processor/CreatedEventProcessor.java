package com.da.eventauditservice.processor;

import com.da.eventauditservice.errorhandling.ErrorMessageUtil;
import com.da.eventauditservice.errorhandling.ValidationException;
import com.da.eventauditservice.model.Created;
import com.da.eventauditservice.model.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.da.eventauditservice.errorhandling.ErrorMessageUtil.RECREATING_ACTIVE_TOKEN;

@Slf4j
@RequiredArgsConstructor
public class CreatedEventProcessor implements EventProcessor<Created>{

    private static void checkRecreationOfActiveToken(Created event, Map<String, Integer> resultHolder)
            throws ValidationException {
        if (resultHolder.containsKey(event.getTokenId())) {
            throw new ValidationException(
                    ErrorMessageUtil.getValidationMessage(RECREATING_ACTIVE_TOKEN, event.getTokenId()));
        }
    }

    @Override
    public List<Event> processEvent(Created event, Map<String, Integer> resultHolder) throws ValidationException {
        log.info(String.format("Processing Created event : %s", (event.getTokenId())));
        checkRecreationOfActiveToken(event, resultHolder);
        resultHolder.put(event.getTokenId(), null);
        return Collections.emptyList();
    }
}
