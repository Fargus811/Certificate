package com.sergeev.esm.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class BearerTokenUtil {

    public static final String BEARER_PREFIX = "Bearer ";
    public static final int BEARER_HEADER_OFFSET = 7;

    public static String substringBearerHeader(String bearerToken){
        return bearerToken.substring(BEARER_HEADER_OFFSET);
    }
}
