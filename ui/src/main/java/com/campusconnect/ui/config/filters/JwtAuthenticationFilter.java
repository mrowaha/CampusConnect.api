package com.campusconnect.ui.config.filters;


import com.campusconnect.domain.security.dto.*;
import com.campusconnect.domain.security.token.UserAuthenticationToken;
import com.campusconnect.domain.user.entity.User;
import com.campusconnect.domain.user.enums.Role;
import com.campusconnect.ui.common.controller.SecureController;
import com.campusconnect.ui.user.service.BilkenteerService;
import com.campusconnect.ui.user.service.ModeratorService;
import com.campusconnect.ui.utils.JwtUtilities;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.util.AntPathMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private  final JwtUtilities jwtUtilities ;
    private final BilkenteerService bilkenteerDetailsService;
    private final ModeratorService moderatorDetailsService;


    /**
     * moderator routes are routes that can be validated
     * on only JWT generated for moderator role
     */
    private final List<SecureController.Endpoint> moderatorRoutes;

    /**
     * bilkenteer routes are routes that can be validated
     * on only JWT generated for bilkenteer role
     */
    private final List<SecureController.Endpoint> bilkenteerRoutes;

    /**
     * shared routes are routes that can be validated
     * on either of the web token roles
     */
    private final List<SecureController.Endpoint> sharedRoutes;

    @Autowired
    public JwtAuthenticationFilter(
            JwtUtilities jwtUtilities,
            BilkenteerService bilkenteerService,
            ModeratorService moderatorService
    ) {
        this.jwtUtilities = jwtUtilities;
        this.bilkenteerDetailsService = bilkenteerService;
        this.moderatorDetailsService = moderatorService;
        this.moderatorRoutes = new ArrayList<>();
        this.bilkenteerRoutes = new ArrayList<>();
        this.sharedRoutes = new ArrayList<>();
    }

    public void insertModeratorRoutes(List<SecureController.Endpoint> routes) {
        moderatorRoutes.addAll(routes);
    }

    public void insertBilkenteerRoutes(List<SecureController.Endpoint> routes) {

        bilkenteerRoutes.addAll(routes);
    }

    public void insertSharedRoutes(List<SecureController.Endpoint> routes) {

        sharedRoutes.addAll(routes);
    }

    private void invalidateRequest(
            int errorCode,
            JwtError errorResponse,
            @NonNull HttpServletResponse response
    ) throws IOException {
        response.setStatus(errorCode);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        PrintWriter writer = response.getWriter();
        writer.print(new ObjectMapper()
                .writeValueAsString(errorResponse));
        writer.flush();
        writer.close();
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        boolean isSharedRoute = sharedRoutes.stream().anyMatch(
                route -> pathMatcher.match(route.getUrl(), request.getServletPath()) &&
                         route.getMethod().toString().equals(request.getMethod())
        );

        boolean isModeratorRoute = moderatorRoutes.stream().anyMatch(
                route -> pathMatcher.match(route.getUrl(), request.getServletPath()) &&
                        route.getMethod().toString().equals(request.getMethod())
        );
        // any request not a moderator route must be validated as BILKENTEER
        String token = jwtUtilities.getToken(request);
        if (token != null) {
            // extract role first
            Role userRole = jwtUtilities.extractRole(token);
            if (userRole == null) {
                this.invalidateRequest(
                        HttpServletResponse.SC_UNAUTHORIZED,
                        new InvalidRoleDto("invalid or no role claim", new Role[]{Role.BILKENTEER, Role.MODERATOR}),
                        response
                        );
                return;
            }

            if (!isSharedRoute) {
                if ((userRole == Role.BILKENTEER && isModeratorRoute) ||
                        (userRole == Role.MODERATOR && !isModeratorRoute)
                ) {
                    this.invalidateRequest(
                            HttpServletResponse.SC_UNAUTHORIZED,
                            new OutOfScopeDto("out of scope access", isModeratorRoute ? Role.MODERATOR : Role.BILKENTEER),
                            response
                    );
                    return;
                }
            }

            boolean valid = jwtUtilities.validateToken(token, userRole);
            if (valid) {
                String email = jwtUtilities.extractUsername(token);
                try {
                    User user = switch (userRole) {
                        case BILKENTEER -> bilkenteerDetailsService.loadUserByUsername(email);
                        case MODERATOR -> moderatorDetailsService.loadUserByUsername(email);
                    };
                    if (user != null) {
                        if (!user.isEnabled()) {
                            this.invalidateRequest(
                                    HttpServletResponse.SC_UNAUTHORIZED,
                                    new DisabledUserDto("account suspended", email),
                                    response
                            );
                            return;
                        }
                        UserAuthenticationToken authenticationToken =
                                new UserAuthenticationToken(user, user.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    } else {
                        throw new UsernameNotFoundException("invalid token bearer");
                    }
                } catch (UsernameNotFoundException e) {
                    this.invalidateRequest(
                            HttpServletResponse.SC_NOT_FOUND,
                            new InvalidBearerDto("invalid token bearer", email),
                            response
                    );
                    return;
                }
            }
            else {
                this.invalidateRequest(
                        HttpServletResponse.SC_FORBIDDEN,
                        new JwtError("invalid token"),
                        response
                );
                return;
            }
        }

        filterChain.doFilter(request,response);
    }
}
