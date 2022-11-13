package com.da.eventauditservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Used extends Event{

    private Boolean consumed;
    private List<Event> consequences;

    public Used(String tokenId, boolean consumed, List<Event> consequences) {
        super(tokenId);
        this.consumed = consumed;
        this.consequences = consequences;
    }
}
