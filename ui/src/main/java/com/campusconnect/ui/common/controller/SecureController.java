package com.campusconnect.ui.common.controller;

import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public abstract class SecureController {
    /**
     * This Controller provides utility for the JWT Authentication Filter
     * to mark controller endpoints for auth
     */

    @PostConstruct
    abstract public void postConstruct();

    public static class Endpoint {
        HttpMethod method;
        String url;

        Boolean secure;

        public Endpoint(HttpMethod method, String url, Boolean secure) {
            this.method = method;
            this.url = url;
            this.secure = secure;
        }
    }

    protected List<Endpoint> endpoints;

    public SecureController() {
        endpoints = new ArrayList<>();
    }

    protected void addEndpoint(
            HttpMethod method, String url, Boolean secure
    ) {
        this.endpoints.add(new Endpoint(method, url, secure));
    }

    protected void addEndPoints(
            Endpoint[] endpoints
    ) {
        this.endpoints.addAll(Arrays.asList(endpoints));
    }

    public List<Endpoint> getEndpoints() {
        return this.endpoints;
    }

    public List<Endpoint> getSecureEndpoints() {
        return this.endpoints
                .stream()
                .filter(endpoint -> endpoint.secure)
                .collect(Collectors.toList());
    }

}
