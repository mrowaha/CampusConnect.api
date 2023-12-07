package com.campusconnect.ui.user.service;

import com.campusconnect.domain.user.dto.BearerToken;
import com.campusconnect.ui.config.JwtUtilities;
import com.campusconnect.ui.user.exceptions.InvalidPasswordException;
import com.campusconnect.ui.user.exceptions.UserAlreadyTakenException;
import com.campusconnect.ui.user.exceptions.UserNotFoundException;
import com.campusconnect.domain.user.dto.UserCreationDto;
import com.campusconnect.domain.user.dto.UserLoginDto;
import com.campusconnect.domain.user.entity.Bilkenteer;
import com.campusconnect.domain.user.enums.Role;
import com.campusconnect.domain.user.repository.BilkenteerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Transactional
@RequiredArgsConstructor
public class BilkenteerService implements UserDetailsService {

    private final BilkenteerRepository bilkenteerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtilities jwtUtilities;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Bilkenteer bilkenteer = bilkenteerRepository.findByEmail(email);
        if (bilkenteer == null) {
            throw new UsernameNotFoundException("User not found!");
        }
        return  bilkenteer;
    }

    public ResponseEntity<?> register(UserCreationDto creationDto) throws UserAlreadyTakenException {
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
            return new ResponseEntity<>(new BearerToken(token , "Bearer "),HttpStatus.OK);

        }
    }

    public ResponseEntity<?> authenticate(UserLoginDto loginDto)
            throws UserNotFoundException,InvalidPasswordException
    {
        Bilkenteer bilkenteer = bilkenteerRepository.findByEmail(loginDto.getEmail());

        if (bilkenteer == null) {
            throw new UserNotFoundException();
        }

        if (!passwordEncoder.matches(loginDto.getPassword(), bilkenteer.getPassword())) {
            throw new InvalidPasswordException();
        }

        String token = jwtUtilities.generateToken(bilkenteer.getUsername(), Role.BILKENTEER);
        return new ResponseEntity<>(new BearerToken(token, "Bearer "), HttpStatus.OK);
    }
}