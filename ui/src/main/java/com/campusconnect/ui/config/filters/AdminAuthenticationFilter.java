package com.campusconnect.ui.config.filters;

import com.campusconnect.domain.security.dto.ApiKeyError;
import com.campusconnect.ui.config.properties.AdminProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@Component
public class AdminAuthenticationFilter extends OncePerRequestFilter {
    private static final String AUTH_TOKEN_HEADER_NAME = "X-API-KEY";

    private final AdminProperties adminProperties;

    private List<String> adminRoutes;

    @Autowired
    public AdminAuthenticationFilter(AdminProperties adminProperties) {
        this.adminRoutes = new ArrayList<>();
        this.adminProperties = adminProperties;
    }

    public void insertAdminRoutes(List<String> routes) {
        this.adminRoutes.addAll(routes);
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        System.out.println("applying admin filter");

        AntPathMatcher pathMatcher = new AntPathMatcher();
        boolean isAdminRoute = adminRoutes.stream().anyMatch(
                route -> pathMatcher.match(route, request.getServletPath())
        );
        if (isAdminRoute) {
            String apiKey = request.getHeader(AUTH_TOKEN_HEADER_NAME);
            if (apiKey == null || !apiKey.equals(adminProperties.getApiKey())) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                PrintWriter writer = response.getWriter();
                writer.print(new ObjectMapper()
                        .writeValueAsString(new ApiKeyError("invalid or missing api key")));
                writer.flush();
                writer.close();
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
