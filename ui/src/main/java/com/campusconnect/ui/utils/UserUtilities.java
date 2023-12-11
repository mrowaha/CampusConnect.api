package com.campusconnect.ui.utils;

import com.campusconnect.domain.security.token.UserAuthenticationToken;
import com.campusconnect.domain.user.entity.User;
import com.campusconnect.domain.user.enums.Role;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import cn.hutool.core.lang.Pair;

@Component
public class UserUtilities {

    public static class AuthToUserException extends RuntimeException {}
    public Pair<String, Role> getEmailRoleFromAuth(Authentication auth) throws AuthToUserException {
        try {
            if (auth instanceof  UserAuthenticationToken userAuthenticationToken) {
                User user = (User) userAuthenticationToken.getPrincipal();
                return new Pair<>(user.getEmail(), user.getRole());
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            throw new AuthToUserException();
        }
    }

    public User getUserFromAuth(Authentication authentication) throws AuthToUserException {
        try {
            if (authentication instanceof  UserAuthenticationToken userAuthenticationToken) {
                return (User) userAuthenticationToken.getPrincipal();
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            throw new AuthToUserException();
        }
    }
}
