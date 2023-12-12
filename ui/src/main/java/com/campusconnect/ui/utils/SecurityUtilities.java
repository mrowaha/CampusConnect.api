package com.campusconnect.ui.utils;

import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMethod;

public class SecurityUtilities {
    public static HttpMethod mapRequestMethodToHttpMethod(RequestMethod requestMethod) {
        return switch (requestMethod) {
            case GET -> HttpMethod.GET;
            case POST -> HttpMethod.POST;
            case PUT -> HttpMethod.PUT;
            case DELETE -> HttpMethod.DELETE;
            default -> throw new IllegalArgumentException("Unsupported RequestMethod: " + requestMethod);
        };
    }
}
