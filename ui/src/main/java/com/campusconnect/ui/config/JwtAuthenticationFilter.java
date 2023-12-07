package com.campusconnect.ui.config;


import com.campusconnect.domain.user.enums.Role;
import com.campusconnect.ui.user.service.BilkenteerService;
import com.campusconnect.ui.user.service.ModeratorService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.util.AntPathMatcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private  final JwtUtilities jwtUtilities ;
    private final BilkenteerService bilkenteerDetailsService;
    private final ModeratorService moderatorDetailsService;


    private List<String> moderatorRoutes;

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
    }

    public void insertModeratorRoutes(String[] routes) {
        moderatorRoutes.addAll(Arrays.asList(routes));
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        AntPathMatcher pathMatcher = new AntPathMatcher();
        boolean isModeratorRoute = moderatorRoutes.stream().anyMatch(
                route -> pathMatcher.match(route, request.getServletPath())
        );
        // any request not a moderator route must be validated as BILKENTEER
        String token = jwtUtilities.getToken(request);

        if (token != null) {
            // extract role first
            Role userRole = jwtUtilities.extractRole(token);
            if ((userRole == Role.BILKENTEER && isModeratorRoute) ||
                    (userRole == Role.MODERATOR && !isModeratorRoute)
            ) {
              throw new ServletException("unauthorized role access");
            }

            boolean valid = jwtUtilities.validateToken(token, userRole);
            if (valid) {
                String email = jwtUtilities.extractUsername(token);
                UserDetails userDetails = switch (userRole) {
                    case BILKENTEER -> bilkenteerDetailsService.loadUserByUsername(email);
                    case MODERATOR -> moderatorDetailsService.loadUserByUsername(email);
                };

                if (userDetails != null) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails.getUsername() ,null , userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }

            }
        }

        filterChain.doFilter(request,response);
    }
}
