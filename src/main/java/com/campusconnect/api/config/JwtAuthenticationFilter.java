package com.campusconnect.api.config;


import com.campusconnect.api.user.service.BilkenteerDetailsService;
import com.campusconnect.api.user.service.ModeratorDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private  final JwtUtilities jwtUtilities ;
    private final BilkenteerDetailsService bilkenteerDetailsService;
    private final ModeratorDetailsService moderatorDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String token = jwtUtilities.getToken(request) ;

        if (token != null) {
            // extract role first
            String role = jwtUtilities.extractRole(token);
            if (role == null) {
                filterChain.doFilter(request,response);
                return;
            }
            boolean valid = jwtUtilities.validateToken(token, role);
            if (valid) {
                String email = jwtUtilities.extractUsername(token);
                UserDetails userDetails = null;
                if (role.equals("BILKENTEER")) {
                    userDetails = bilkenteerDetailsService.loadUserByUsername(email);
                } else {
                    userDetails = moderatorDetailsService.loadUserByUsername(email);
                }

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
