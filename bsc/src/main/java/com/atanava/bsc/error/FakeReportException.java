package com.atanava.bsc.error;

import java.util.UUID;

public class FakeReportException extends RuntimeException {

    public static final String ERROR_MESSAGE_TEMPLATE = "Fake report received from unknown Base Station id: %s";

    public FakeReportException(String message) {
        super(message);
    }

    public FakeReportException(UUID baseId) {
        this(String.format(ERROR_MESSAGE_TEMPLATE, baseId));
    }
}
