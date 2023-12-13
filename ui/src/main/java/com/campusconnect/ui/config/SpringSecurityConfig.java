package com.campusconnect.ui.config;

import com.campusconnect.domain.security.RequiredScope;
import com.campusconnect.domain.security.SecurityScope;
import com.campusconnect.ui.common.controller.SecureController;
import com.campusconnect.ui.config.filters.AdminAuthenticationFilter;
import com.campusconnect.ui.config.filters.JwtAuthenticationFilter;
import com.campusconnect.ui.config.properties.AdminProperties;
import com.campusconnect.ui.config.properties.RoledJwtProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
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
import org.springframework.util.AntPathMatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Configuration
@EnableWebSecurity
@EnableConfigurationProperties({RoledJwtProperties.class, AdminProperties.class})
public class SpringSecurityConfig {

    private List<String> WHITE_LIST_URLS;

    private List<String> MODERATOR_URLS;

    private List<String> BILKENTEER_URLS;

    private List<String> SHARED_URLS;

    private List<String> ADMIN_URLS;

    private final List<SecureController> secureControllerList;
    private final JwtAuthenticationFilter jwtAuthenticationFilter ;

    private final AdminAuthenticationFilter adminAuthenticationFilter;

    @Autowired
    public SpringSecurityConfig(
            JwtAuthenticationFilter filter,
            AdminAuthenticationFilter adminAuthenticationFilter,
            List<SecureController> secureControllerList
    ) {

        this.jwtAuthenticationFilter = filter;
        this.adminAuthenticationFilter = adminAuthenticationFilter;

        this.secureControllerList = secureControllerList;
        this.MODERATOR_URLS = new ArrayList<>();
        this.SHARED_URLS = new ArrayList<>();
        this.BILKENTEER_URLS = new ArrayList<>();
        this.WHITE_LIST_URLS = new ArrayList<>();
        this.ADMIN_URLS = new ArrayList<>();
        for (SecureController appController : this.secureControllerList) {
//            appController.getScopes();
            for (SecureController.Endpoint endpoint : appController.getEndpoints()) {
                SecurityScope scope = endpoint.getScope();
                switch (scope) {
                    case NONE -> this.WHITE_LIST_URLS.add(endpoint.getUrl());
                    case BILKENTEER -> this.BILKENTEER_URLS.add(endpoint.getUrl());
                    case MODERATOR -> this.MODERATOR_URLS.add(endpoint.getUrl());
                    case SHARED -> this.SHARED_URLS.add(endpoint.getUrl());
                    case ADMIN -> this.ADMIN_URLS.add(endpoint.getUrl());
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
            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers(new String[]{"/auth/**", "/products", "/products/**"}).permitAll();

        for (SecureController appController : this.secureControllerList) {
            for (SecureController.Endpoint endpoint : appController.getEndpoints()) {
                if (endpoint.getScope() != SecurityScope.NONE && endpoint.getScope() != SecurityScope.ADMIN) {
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

        adminAuthenticationFilter.insertAdminRoutes(ADMIN_URLS);

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(adminAuthenticationFilter, JwtAuthenticationFilter.class);
        return  http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception
    {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
