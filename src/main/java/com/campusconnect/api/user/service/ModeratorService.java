package com.campusconnect.api.user.service;

import com.campusconnect.api.common.dto.BearerToken;
import com.campusconnect.api.config.JwtUtilities;
import com.campusconnect.api.user.dto.UserLoginDto;
import com.campusconnect.api.user.dto.UserCreationDto;
import com.campusconnect.api.user.entity.Moderator;
import com.campusconnect.api.user.enums.Role;
import com.campusconnect.api.user.exceptions.InvalidPasswordException;
import com.campusconnect.api.user.exceptions.UserAlreadyTakenException;
import com.campusconnect.api.user.exceptions.UserNotFoundException;
import com.campusconnect.api.user.repository.ModeratorRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@Transactional
@RequiredArgsConstructor
public class ModeratorService {

    private final ModeratorRepository moderatorRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtilities jwtUtilities;

    public ResponseEntity<?> register(UserCreationDto creationDto) {
        if(moderatorRepository.existsByEmail(creationDto.getEmail())) {
            throw new UserAlreadyTakenException();
        }
        else {
            Moderator moderator = Moderator.builder()
                            .firstName(creationDto.getFirstName())
                            .lastName(creationDto.getLastName())
                            .email(creationDto.getEmail())
                            .password(passwordEncoder.encode(creationDto.getPassword()))
                            .role(Role.MODERATOR)
                            .isActive(true)
                            .build();
            moderatorRepository.save(moderator);
            String token = jwtUtilities.generateToken(creationDto.getEmail(), "MODERATOR");
            return new ResponseEntity<>(new BearerToken(token , "Bearer "), HttpStatus.OK);
        }
    }

    public ResponseEntity<?> authenticate(UserLoginDto loginDto) throws UserNotFoundException {
        Moderator moderator = moderatorRepository.findByEmail(loginDto.getEmail());
        if (moderator == null) {
            throw new UserNotFoundException();
        }

        if (!passwordEncoder.matches(loginDto.getPassword(), moderator.getPassword())) {
            throw new InvalidPasswordException();
        }

        String token = jwtUtilities.generateToken(moderator.getUsername(), "MODERATOR");
        return new ResponseEntity<>(new BearerToken(token, "Bearer "), HttpStatus.OK);
    }


}
