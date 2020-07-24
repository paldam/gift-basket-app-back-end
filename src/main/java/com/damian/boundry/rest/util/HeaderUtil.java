package com.damian.boundry.rest.util;

import org.springframework.http.HttpHeaders;

public class HeaderUtil {

    private HeaderUtil() {
    }

    public static HttpHeaders createAlert(String message, String param) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("app-alert", message);
        headers.add("app-params", param);
        return headers;
    }


    public static HttpHeaders createFailureAlert(String entityName, String defaultMessage) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("app-error", defaultMessage);
        headers.add("app-params", entityName);
        return headers;
    }
}
