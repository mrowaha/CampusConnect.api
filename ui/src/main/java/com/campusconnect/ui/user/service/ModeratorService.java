package com.campusconnect.ui.user.service;

import com.campusconnect.domain.security.dto.BearerToken;
import com.campusconnect.domain.user.dto.*;
import com.campusconnect.domain.user.entity.User;
import com.campusconnect.ui.utils.JwtUtilities;
import com.campusconnect.domain.user.entity.Moderator;
import com.campusconnect.domain.user.enums.Role;
import com.campusconnect.ui.user.exceptions.InvalidPasswordException;
import com.campusconnect.ui.user.exceptions.UserAlreadyTakenException;
import com.campusconnect.ui.user.exceptions.UserNotFoundException;
import com.campusconnect.domain.user.repository.ModeratorRepository;
import com.campusconnect.ui.user.exceptions.UserSuspendedException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@Transactional
@RequiredArgsConstructor
public class ModeratorService implements UserService {

    private final ModeratorRepository moderatorRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtilities jwtUtilities;

    @Override
    public User loadUserByUsername(String email) throws UserNotFoundException {
        return moderatorRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException("User not found")
        );
    }

    public BearerToken register(UserCreationDto creationDto) throws UserAlreadyTakenException {
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
            String token = jwtUtilities.generateToken(creationDto.getEmail(), Role.MODERATOR);
            return new BearerToken(token , "Bearer ");
        }
    }

    public ModeratorLoginResponseDto authenticate(UserLoginRequestDto loginDto)
            throws UserNotFoundException, InvalidPasswordException
    {
        Moderator moderator = moderatorRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(UserNotFoundException::new);

        if (!passwordEncoder.matches(loginDto.getPassword(), moderator.getPassword())) {
            throw new InvalidPasswordException();
        }

        if (!moderator.getIsActive()) {
            throw new UserSuspendedException();
        }

        String token = jwtUtilities.generateToken(moderator.getUsername(), Role.MODERATOR);
        BearerToken bearerToken = new BearerToken(token, "Bearer ");
        return ModeratorLoginResponseDto.builder()
                .uuid(moderator.getUserId())
                .email(moderator.getEmail())
                .firstName(moderator.getFirstName())
                .lastName(moderator.getLastName())
                .role(moderator.getRole())
                .token(bearerToken)
                .build();
    }
}
