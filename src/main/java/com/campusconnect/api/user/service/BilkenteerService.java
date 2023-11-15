package com.campusconnect.api.user.service;

import com.campusconnect.api.common.dto.BearerToken;
import com.campusconnect.api.common.dto.ErrorDto;
import com.campusconnect.api.config.JwtUtilities;
import com.campusconnect.api.user.dto.BilkenteerCreationDto;
import com.campusconnect.api.user.dto.BilkenteerLoginDto;
import com.campusconnect.api.user.entity.Bilkenteer;
import com.campusconnect.api.user.enums.Role;
import com.campusconnect.api.user.repository.BilkenteerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Transactional
@RequiredArgsConstructor
public class BilkenteerService {

    private final AuthenticationManager authenticationManager;
    private final BilkenteerRepository bilkenteerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtilities jwtUtilities;



    public ResponseEntity<?> register(BilkenteerCreationDto creationDto) {
        if(bilkenteerRepository.existsByEmail(creationDto.getEmail())) {
            return  new ResponseEntity<>(new ErrorDto("email is already taken"), HttpStatus.SEE_OTHER); }
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
            String token = jwtUtilities.generateToken(creationDto.getEmail(), "BILKENTEER");
            return new ResponseEntity<>(new BearerToken(token , "Bearer "),HttpStatus.OK);

        }
    }

    public ResponseEntity<?> authenticate(BilkenteerLoginDto loginDto) {
        Bilkenteer bilkenteer = bilkenteerRepository.findByEmail(loginDto.getEmail());
        if (bilkenteer == null) {
            return new ResponseEntity<>(new ErrorDto("User not found!"), HttpStatus.NOT_FOUND);
        }
        Authentication authentication= authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getEmail(),
                        loginDto.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtilities.generateToken(bilkenteer.getUsername(), "BILKENTEER");
        return new ResponseEntity<>(new BearerToken(token, "Bearer "), HttpStatus.OK);
    }

}
