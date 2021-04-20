package com.sergeev.esm.dto;

import java.util.Map;


/**
 * The type Google user info.
 */
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

    /**
     * Instantiates a new Google user info.
     *
     * @param attributes the attributes
     */
    public GoogleUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public String getId() {
        return (String) attributes.get(GOOGLE_USER_ID);
    }

    /**
     * Get google user first name string.
     *
     * @return the string
     */
    public String getGoogleUserFirstName(){
        return (String) attributes.get(GOOGLE_USER_FIRST_NAME);
    }

    /**
     * Gets google user last name.
     *
     * @return the google user last name
     */
    public String getGoogleUserLastName() {
        return (String) attributes.get(GOOGLE_USER_LAST_NAME);
    }

    /**
     * Gets user name.
     *
     * @return the user name
     */
    public String getUserName() {
        return (String) attributes.get(GOOGLE_USER_NAME);
    }

    /**
     * Gets email.
     *
     * @return the email
     */
    public String getEmail() {
        return (String) attributes.get(GOOGLE_USER_EMAIL);
    }
}
