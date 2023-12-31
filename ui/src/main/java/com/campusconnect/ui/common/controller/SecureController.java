package com.campusconnect.ui.common.controller;

import com.campusconnect.domain.security.RequiredScope;
import com.campusconnect.domain.security.SecurityScope;
import com.campusconnect.ui.utils.SecurityUtilities;
import jakarta.annotation.PostConstruct;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class SecureController {
    /**
     * This Controller provides utility for the JWT Authentication Filter
     * to mark controller endpoints for auth
     */

    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    public SecureController() {
        endpoints = new ArrayList<>();
    }

    @Autowired
    public void setRequestMappingHandlerMapping(RequestMappingHandlerMapping mappingHandlerMapping) {
        this.requestMappingHandlerMapping = mappingHandlerMapping;
    }

    @PostConstruct
    public void postConstruct() {
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
        Class<?> targetControllerClass = this.getClass();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
            HandlerMethod handlerMethod = entry.getValue();
            if (handlerMethod.getBeanType().equals(targetControllerClass)) {
                RequestMappingInfo mappingInfo = entry.getKey();
                RequestMethod method = mappingInfo.getMethodsCondition().getMethods()
                        .stream().findFirst()
                        .orElseThrow(RuntimeException::new);
                String atIndexFirst = mappingInfo.toString().split(",")[0];
                if (atIndexFirst == null) {
                    throw  new RuntimeException("failed to extract mapping information");
                }
                String path = atIndexFirst.split(" ")[1];
                if (path == null) {
                    throw new RuntimeException("failed to extract path from mapping information");
                }
                int cutBy = Character.compare(path.charAt(path.length()-1), '}');
                if (cutBy == 0) {
                    path = path.substring(1, path.length()-2);
                } else {
                    path = path.substring(1, path.length()-1);
                }
                if (handlerMethod.hasMethodAnnotation(RequiredScope.class)) {
                    SecurityScope scope = Objects.requireNonNull(handlerMethod.getMethodAnnotation(RequiredScope.class)).scope();
                    HttpMethod mappedMethod = SecurityUtilities.mapRequestMethodToHttpMethod(method);
                    log.info("{}",String.format("path %s, method %s, scope %s", path, mappedMethod.toString(), scope));
                    this.addEndpoint(mappedMethod, path, scope);
                } else {
                    throw new RuntimeException("all secure controller types must annotate methods with RequiredScope");
                }
            }
        }
    }



    @ToString
    @Getter
    @Setter
    @Builder
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

    @Getter
    private List<Endpoint> endpoints;
    protected void addEndpoint(
            HttpMethod method,
            String mapping,
            SecurityScope scope
    ) {
        if (this.endpoints == null) this.endpoints = new ArrayList<>();
        this.endpoints.add(new Endpoint(method, mapping, scope));
    }

    protected void addEndPoints(
            Endpoint[] endpoints
    ) {
        if (this.endpoints == null) this.endpoints = new ArrayList<>();
        this.endpoints.addAll(Arrays.asList(endpoints));
    }

    public List<Endpoint> getSecureEndpoints() {
        return this.endpoints
                .stream()
                .filter(endpoint -> endpoint.scope != SecurityScope.NONE)
                .collect(Collectors.toList());
    }

}
