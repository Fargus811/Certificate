package com.sergeev.esm.dto;

import java.util.Map;

public class GoogleUserInfo {

    private static final String GOOGLE_USER_ID = "sub";
    private static final String GOOGLE_USER_NAME = "name";
    private static final String GOOGLE_USER_LAST_NAME = "family_name";
    private static final String GOOGLE_USER_FIRST_NAME = "given_name";
    private static final String GOOGLE_USER_EMAIL = "email";

    /**
     * The map with google user attributes.
     */
    private final Map<String, Object> attributes;

    public GoogleUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public String getId() {
        return (String) attributes.get(GOOGLE_USER_ID);
    }

    public String getGoogleUserFirstName(){
        return (String) attributes.get(GOOGLE_USER_FIRST_NAME);
    }
    public String getGoogleUserLastName() {
        return (String) attributes.get(GOOGLE_USER_LAST_NAME);
    }

    public String getUserName() {
        return (String) attributes.get(GOOGLE_USER_NAME);
    }

    public String getEmail() {
        return (String) attributes.get(GOOGLE_USER_EMAIL);
    }
}
