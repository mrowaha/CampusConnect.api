package com.campusconnect.ui.user.service;

import com.campusconnect.domain.security.dto.BearerToken;
import com.campusconnect.domain.user.dto.BilkenteerLoginResponse;
import com.campusconnect.domain.user.entity.User;
import com.campusconnect.ui.utils.JwtUtilities;
import com.campusconnect.ui.user.exceptions.InvalidPasswordException;
import com.campusconnect.ui.user.exceptions.UserAlreadyTakenException;
import com.campusconnect.ui.user.exceptions.UserNotFoundException;
import com.campusconnect.domain.user.dto.UserCreationDto;
import com.campusconnect.domain.user.dto.UserLoginRequestDto;
import com.campusconnect.domain.user.entity.Bilkenteer;
import com.campusconnect.domain.user.enums.Role;
import com.campusconnect.domain.user.repository.BilkenteerRepository;
import com.campusconnect.ui.user.exceptions.UserSuspendedException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Transactional
@RequiredArgsConstructor
public class BilkenteerService implements UserService {

    private final BilkenteerRepository bilkenteerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtilities jwtUtilities;

    @Override
    public User loadUserByUsername(String email) throws UsernameNotFoundException {
        return bilkenteerRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException("User not found"));
    }

    public BearerToken register(UserCreationDto creationDto) throws UserAlreadyTakenException {
        if(bilkenteerRepository.existsByEmail(creationDto.getEmail())) {
            throw new UserAlreadyTakenException();
        }
        else {
            Bilkenteer bilkenteer = Bilkenteer.builder()
                    .firstName(creationDto.getFirstName())
                    .lastName(creationDto.getLastName())
                    .email(creationDto.getEmail())
                    .password(passwordEncoder.encode(creationDto.getPassword()))
                    .enableAppNotification(true)
                    .enableEmailNotification(true)
                    .role(Role.BILKENTEER)
                    .trustScore(4)
                    .isSuspended(false)
                    .phoneNumbers(new ArrayList<>())
                    .address(null)
                    .build();
            bilkenteerRepository.save(bilkenteer);
            String token = jwtUtilities.generateToken(creationDto.getEmail(), Role.BILKENTEER);
            return new BearerToken(token , "Bearer ");
        }
    }

    public BilkenteerLoginResponse authenticate(UserLoginRequestDto loginDto)
            throws UserNotFoundException,InvalidPasswordException, UserSuspendedException
    {
        Bilkenteer bilkenteer = bilkenteerRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(UserNotFoundException::new);
        if (!passwordEncoder.matches(loginDto.getPassword(), bilkenteer.getPassword())) {
            throw new InvalidPasswordException();
        }

        if (bilkenteer.getIsSuspended()) {
            throw new UserSuspendedException();
        }

        String token = jwtUtilities.generateToken(bilkenteer.getUsername(), Role.BILKENTEER);
        BearerToken bearerToken = new BearerToken(token, "Bearer ");
        return BilkenteerLoginResponse.builder()
                .uuid(bilkenteer.getUserId())
                .email(bilkenteer.getEmail())
                .firstName(bilkenteer.getFirstName())
                .lastName(bilkenteer.getLastName())
                .role(bilkenteer.getRole())
                .trustScore(bilkenteer.getTrustScore())
                .token(bearerToken)
                .build();
    }
}
