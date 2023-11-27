package com.campusconnect.ui.user.service;

import com.campusconnect.ui.common.dto.BearerToken;
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
