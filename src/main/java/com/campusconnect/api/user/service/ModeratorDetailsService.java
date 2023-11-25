package com.campusconnect.api.user.service;

import com.campusconnect.api.user.entity.Bilkenteer;
import com.campusconnect.api.user.entity.Moderator;
import com.campusconnect.api.user.repository.ModeratorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ModeratorDetailsService implements UserDetailsService {

    private final ModeratorRepository moderatorRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Moderator moderator = moderatorRepository.findByEmail((email));
        if (moderator == null) {
            throw new UsernameNotFoundException("User not found!");
        }
        return moderator;
    }
}
