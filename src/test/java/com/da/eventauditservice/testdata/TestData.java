package com.da.eventauditservice.testdata;

import com.da.eventauditservice.model.Created;
import com.da.eventauditservice.model.Event;
import com.da.eventauditservice.model.Used;

import java.util.Collections;
import java.util.List;

public class TestData {

    public static List<Event> buildEvents(int id, boolean reCreateToken) {
        return reCreateToken ? List.of(created(id), usedWithConsequences(id), created(id)) : List.of(created(id), usedWithConsequences(id));
    }

    public static Created created(int id) {
        return new Created(String.valueOf(id));
    }

    public static Used usedWithCreatedAsConsequence(int idToUse) {
        return new Used(String.valueOf(idToUse), false, List.of(new Created(String.valueOf(idToUse + 1))));
    }

    public static Used usedWithUsedAsConsequence(int idToUse) {
        return new Used(String.valueOf(idToUse), false,
                List.of(new Used("1", false, Collections.emptyList())));
    }

    public static Used usedWithConsequences(int idToUse) {
        return new Used(String.valueOf(idToUse), false, List.of(new Created(String.valueOf(idToUse + 1)),
                new Used(String.valueOf(idToUse), true, List.of(new Created(String.valueOf(idToUse + 2)))))
        );
    }

    public static Used usedWithoutConsequences(int idToUse) {
        return new Used(String.valueOf(idToUse), false, Collections.emptyList());
    }

    public static Used consumeWithoutConsequences(int idToUse) {
        return new Used(String.valueOf(idToUse), true, Collections.emptyList());
    }
}
