package com.sergeev.esm.util;

import lombok.Getter;

import java.util.List;

/**
 * The type Rest reason.
 */
@Getter
public class RestMessage {

    public static final int ERROR_CODE_OF_NOT_VALID_ARGUMENTS = 40422;
    public static final int ERROR_CODE_OF_CONFLICT_ARGUMENTS = 40409;
    public static final int ERROR_CODE_OF_SERVER_ERROR = 50500;
    public static final int ERROR_CODE_FORBIDDEN = 40403;

    private String reason;
    private List<String> messages;
    private int errorCode;

    /**
     * Instantiates a new Rest reason.
     *
     * @param reason    the reason
     * @param messages  the messages
     * @param errorCode the error code
     */
    public RestMessage(String reason, List<String> messages, int errorCode) {
        this.reason = reason;
        this.messages = messages;
        this.errorCode = errorCode;
    }

    /**
     * Instantiates a new Rest reason.
     *
     * @param messages the messages
     */
    public RestMessage(List<String> messages) {
        this.messages = messages;
    }

    /**
     * Instantiates a new Rest reason.
     *
     * @param reason the reason
     */
    public RestMessage(String reason) {
        this.reason = reason;
    }

    /**
     * Instantiates a new Rest message.
     *
     * @param reason    the reason
     * @param errorCode the error code
     */
    public RestMessage(String reason, int errorCode) {
        this.reason = reason;
        this.errorCode = errorCode;
    }
}
