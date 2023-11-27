package com.campusconnect.ui.user.service;

import com.campusconnect.domain.user.entity.Bilkenteer;
import com.campusconnect.domain.user.repository.BilkenteerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BilkenteerDetailsService  implements UserDetailsService {
    private final BilkenteerRepository bilkenteerRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Bilkenteer bilkenteer = bilkenteerRepository.findByEmail(email);
        if (bilkenteer == null) {
            throw new UsernameNotFoundException("User not found!");
        }
        return  bilkenteer;
    }
}
