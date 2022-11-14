package com.da.eventauditservice.processor;

import com.da.eventauditservice.errorhandling.ValidationException;
import com.da.eventauditservice.model.Event;
import com.da.eventauditservice.model.Used;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

import static com.da.eventauditservice.errorhandling.ErrorMessageUtil.CONSUMING_ARCHIVE_ABSENT_TOKEN;
import static com.da.eventauditservice.errorhandling.ErrorMessageUtil.getValidationMessage;


@Slf4j
@NoArgsConstructor
public class UsedEventProcessor implements EventProcessor<Used>{

    private static void checkUseOfInactiveOrAbsentToken(Used event, Map<String, Integer> resultHolder)
            throws ValidationException {
        if (!resultHolder.containsKey(event.getTokenId())) {
            throw new ValidationException(
                    getValidationMessage(CONSUMING_ARCHIVE_ABSENT_TOKEN, event.getTokenId()));
        }
    }

    @Override
    public List<Event> processEvent(Used event, Map<String, Integer> resultHolder) throws ValidationException {
        log.info(String.format("Processing Used event : %s", event.getTokenId()));
        checkUseOfInactiveOrAbsentToken(event, resultHolder);
        if (event.getConsumed()) {
            log.info(String.format("Processing Consumed event, token to be removed  : %s", event.getTokenId()));
            resultHolder.remove(event.getTokenId());
        }
        return event.getConsequences();
    }

}
