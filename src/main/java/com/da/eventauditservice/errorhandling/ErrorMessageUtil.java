package com.da.eventauditservice.errorhandling;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ErrorMessageUtil {

    NULL_EMPTY_EVENTS("Validation failure(input events are null/empty)"),
    RECREATING_ACTIVE_TOKEN("Validation failure(recreation with active tokenId) for tokenId"),
    CONSUMING_ARCHIVE_ABSENT_TOKEN(
            "Validation failure(using/consuming an non-existing/inactive token) for tokenId");

    private final String validationMessage;

    public static String getValidationMessage(ErrorMessageUtil errorMessageUtil, String tokenId) {
        return String.format("%s : {%s}", errorMessageUtil.validationMessage, tokenId);
    }

    public static String getValidationMessage(ErrorMessageUtil errorMessageUtil) {
        return String.format("%s", errorMessageUtil.validationMessage);
    }
}
