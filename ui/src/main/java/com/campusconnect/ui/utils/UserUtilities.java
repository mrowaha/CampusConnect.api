package com.campusconnect.ui.utils;

import com.campusconnect.domain.user.enums.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import cn.hutool.core.lang.Pair;

@Component
public class UserUtilities {

    public static class AuthToUserException extends RuntimeException {}
    public Pair<String, Role> getUserDetailsFromAuth(Authentication auth) throws AuthToUserException {
        try {
            GrantedAuthority authority = auth.getAuthorities().stream().findFirst()
                    .orElseThrow(RuntimeException::new);
            Role role = Role.valueOf(authority.getAuthority());
            String email =  (String) auth.getPrincipal();
            if (email == null) {
                throw new Exception();
            }
            return new Pair<>(email, role);
        } catch (Exception e) {
            throw new AuthToUserException();
        }
    }
}
