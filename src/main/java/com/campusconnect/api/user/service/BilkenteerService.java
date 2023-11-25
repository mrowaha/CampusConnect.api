package com.campusconnect.api.user.service;

import com.campusconnect.api.common.dto.BearerToken;
import com.campusconnect.api.config.JwtUtilities;
import com.campusconnect.api.user.exceptions.InvalidPasswordException;
import com.campusconnect.api.user.exceptions.UserAlreadyTakenException;
import com.campusconnect.api.user.exceptions.UserNotFoundException;
import com.campusconnect.api.user.dto.UserCreationDto;
import com.campusconnect.api.user.dto.UserLoginDto;
import com.campusconnect.api.user.entity.Bilkenteer;
import com.campusconnect.api.user.enums.Role;
import com.campusconnect.api.user.repository.BilkenteerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Transactional
@RequiredArgsConstructor
public class BilkenteerService {

    private final BilkenteerRepository bilkenteerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtilities jwtUtilities;

    public ResponseEntity<?> register(UserCreationDto creationDto) {
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
            String token = jwtUtilities.generateToken(creationDto.getEmail(), "BILKENTEER");
            return new ResponseEntity<>(new BearerToken(token , "Bearer "),HttpStatus.OK);

        }
    }

    public ResponseEntity<?> authenticate(UserLoginDto loginDto) throws UserNotFoundException {
        Bilkenteer bilkenteer = bilkenteerRepository.findByEmail(loginDto.getEmail());

        if (bilkenteer == null) {
            throw new UserNotFoundException();
        }

        if (!passwordEncoder.matches(loginDto.getPassword(), bilkenteer.getPassword())) {
            throw new InvalidPasswordException();
        }

        String token = jwtUtilities.generateToken(bilkenteer.getUsername(), "BILKENTEER");
        return new ResponseEntity<>(new BearerToken(token, "Bearer "), HttpStatus.OK);
    }
}
