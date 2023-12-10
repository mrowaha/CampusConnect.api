package com.campusconnect.ui.config;

import com.campusconnect.ui.common.controller.SecureController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.ArrayList;
import java.util.List;


@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(RoledJwtProperties.class)
public class SpringSecurityConfig {

    private List<String> WHITE_LIST_URLS;

    private List<String> MODERATOR_URLS;

    private List<String> BILKENTEER_URLS;

    private List<String> SHARED_URLS;
    private final List<SecureController> secureControllerList;
    private final JwtAuthenticationFilter jwtAuthenticationFilter ;

    @Autowired
    public SpringSecurityConfig(
            JwtAuthenticationFilter filter,
            List<SecureController> secureControllerList
    ) {
        this.jwtAuthenticationFilter = filter;
        this.secureControllerList = secureControllerList;
        this.MODERATOR_URLS = new ArrayList<>();
        this.SHARED_URLS = new ArrayList<>();
        this.BILKENTEER_URLS = new ArrayList<>();
        this.WHITE_LIST_URLS = new ArrayList<>();
        for (SecureController appController : this.secureControllerList) {
            for (SecureController.Endpoint endpoint : appController.getEndpoints()) {
                SecureController.SecurityScope scope = endpoint.getScope();
                switch (scope) {
                    case NONE -> this.WHITE_LIST_URLS.add(endpoint.getUrl());
                    case BILKENTEER -> this.BILKENTEER_URLS.add(endpoint.getUrl());
                    case MODERATOR -> this.MODERATOR_URLS.add(endpoint.getUrl());
                    case SHARED -> this.SHARED_URLS.add(endpoint.getUrl());
                }
            }
        }
    }

    @Bean
    public SecurityFilterChain filterChain (HttpSecurity http) throws Exception
    {
        http
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeHttpRequests()
            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();

        for (SecureController appController : this.secureControllerList) {
            for (SecureController.Endpoint endpoint : appController.getEndpoints()) {
                if (endpoint.getScope() != SecureController.SecurityScope.NONE) {
                    http.authorizeHttpRequests().
                            requestMatchers(endpoint.getMethod(), endpoint.getUrl()).authenticated();
                } else {
                    http.authorizeHttpRequests()
                            .requestMatchers(endpoint.getMethod(), endpoint.getUrl()).permitAll();
                }
            }
        }

        jwtAuthenticationFilter.insertModeratorRoutes(MODERATOR_URLS);
        jwtAuthenticationFilter.insertBilkenteerRoutes(BILKENTEER_URLS);
        jwtAuthenticationFilter.insertSharedRoutes(SHARED_URLS);

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return  http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception
    {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
