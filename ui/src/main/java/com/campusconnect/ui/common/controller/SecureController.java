package com.campusconnect.ui.common.controller;

import com.campusconnect.domain.user.enums.Role;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
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

    public enum SecurityScope {
        NONE,
        MODERATOR,
        BILKENTEER,
        SHARED
    };

    @ToString
    @Getter
    @Setter
    public static class Endpoint {
        HttpMethod method;
        String url;
        SecurityScope scope;
        public Endpoint(HttpMethod method, String url, SecurityScope scope) {
            this.method = method;
            this.url = url;
            this.scope = scope;
        }
    }

    private List<Endpoint> endpoints;

    public SecureController() {
        endpoints = new ArrayList<>();
    }

    protected void addEndpoint(
            HttpMethod method,
            String controllerUrl,
            String mappingUrl,
            SecurityScope scope
    ) {
        if (this.endpoints == null) this.endpoints = new ArrayList<>();
        this.endpoints.add(new Endpoint(method, controllerUrl+mappingUrl, scope));
    }

    protected void addEndPoints(
            Endpoint[] endpoints
    ) {
        if (this.endpoints == null) this.endpoints = new ArrayList<>();
        this.endpoints.addAll(Arrays.asList(endpoints));
    }

    public List<Endpoint> getEndpoints() {
        return this.endpoints;
    }

    public List<Endpoint> getSecureEndpoints() {
        return this.endpoints
                .stream()
                .filter(endpoint -> endpoint.scope != SecurityScope.NONE)
                .collect(Collectors.toList());
    }

}
