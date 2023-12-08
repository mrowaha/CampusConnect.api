package com.campusconnect.ui.config;

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


@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(RoledJwtProperties.class)
public class SpringSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter ;

    @Autowired
    public SpringSecurityConfig(
            JwtAuthenticationFilter filter
    ) {
        this.jwtAuthenticationFilter = filter;
    }

    @Bean
    public SecurityFilterChain filterChain (HttpSecurity http) throws Exception
    {

        String[] moderatorProtectedRoutes = new String[]{
                "/moderator/**"
        };

        String[] bilkenteerProtectedRoutes = new String[] {
                "/bilkenteer/**"
        };

        String[] sharedProtectedRoutes = new String[] {
                "/s3/**"
        };

        http
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeHttpRequests()
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers(new String[]{"/auth/**"}).permitAll()
                .requestMatchers(moderatorProtectedRoutes).authenticated()
                .requestMatchers(bilkenteerProtectedRoutes).authenticated()
                .requestMatchers(sharedProtectedRoutes).authenticated();

        jwtAuthenticationFilter.insertModeratorRoutes(moderatorProtectedRoutes);
        jwtAuthenticationFilter.insertBilkenteerRoutes(bilkenteerProtectedRoutes);
        jwtAuthenticationFilter.insertSharedRoutes(sharedProtectedRoutes);

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return  http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception
    {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
