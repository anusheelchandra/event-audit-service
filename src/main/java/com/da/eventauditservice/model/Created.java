package com.da.eventauditservice.model;


public final class Created extends Event{

    public Created(String tokenId) {
        super(tokenId);
    }
}
