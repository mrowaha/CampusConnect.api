package com.campusconnect.ui.user.service;

import com.campusconnect.domain.admin.dto.UserSuspendRequestDto;
import com.campusconnect.domain.admin.dto.UserSuspendResponseDto;
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
import org.bouncycastle.math.raw.Mod;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
public class ModeratorService implements UserService {

    // Repositories for database operations
    private final ModeratorRepository moderatorRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtilities jwtUtilities;

    /**
     * Loads a moderator by email.
     *
     * @param email Email of the moderator.
     * @return The loaded moderator.
     * @throws UserNotFoundException If the moderator with the specified email is not found.
     */
    @Override
    public User loadUserByUsername(String email) throws UserNotFoundException {
        return moderatorRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException("User not found")
        );
    }

    /**
     * Lists all moderators.
     *
     * @return List of moderator information DTOs.
     */
    @Override
    public List<UserInfoDto> listAll() {
        List<Moderator> moderators = moderatorRepository.findAll();
        return moderators.stream()
                .map(moderator -> UserInfoDto.builder()
                        .uuid(moderator.getUserId())
                        .role(moderator.getRole())
                        .lastName(moderator.getLastName())
                        .firstName(moderator.getFirstName())
                        .email(moderator.getEmail())
                        .isActive(moderator.getIsActive())
                        .build())
                .toList();
    }

    /**
     * Suspends a moderator.
     *
     * @param suspendRequestDto Request to suspend a moderator.
     * @return Response DTO with suspension details.
     * @throws UserNotFoundException If the moderator to be suspended is not found.
     */
    @Override
    public UserSuspendResponseDto suspend(UserSuspendRequestDto suspendRequestDto)
        throws UserNotFoundException
    {
        Moderator moderator = moderatorRepository.findById(suspendRequestDto.getUuid())
                .orElseThrow(UserNotFoundException::new);
        moderatorRepository.disable(suspendRequestDto.getUuid());
        return UserSuspendResponseDto.builder()
                .uuid(suspendRequestDto.getUuid())
                .successStatus(true)
                .message(String.format("Moderator %s suspended", suspendRequestDto.getUuid().toString()))
                .user(UserInfoDto.builder()
                        .firstName(moderator.getFirstName())
                        .lastName(moderator.getLastName())
                        .uuid(moderator.getUserId())
                        .email(moderator.getEmail())
                        .isActive(false)
                        .build()
                )
                .build();
    }

    /**
     * Unsuspends a moderator.
     *
     * @param suspendRequestDto Request to unsuspend a moderator.
     * @return Response DTO with unsuspension details.
     * @throws UserNotFoundException If the moderator to be unsuspended is not found.
     */
    @Override
    public UserSuspendResponseDto unsuspend(UserSuspendRequestDto suspendRequestDto) throws UserNotFoundException {
        Moderator moderator = moderatorRepository.findById(suspendRequestDto.getUuid())
                .orElseThrow(UserNotFoundException::new);
        moderatorRepository.enable(suspendRequestDto.getUuid());
        return UserSuspendResponseDto.builder()
                .uuid(suspendRequestDto.getUuid())
                .successStatus(true)
                .message(String.format("Moderator %s activated", suspendRequestDto.getUuid().toString()))
                .user(UserInfoDto.builder()
                        .firstName(moderator.getFirstName())
                        .lastName(moderator.getLastName())
                        .uuid(moderator.getUserId())
                        .email(moderator.getEmail())
                        .isActive(true)
                        .build()
                )
                .build();
    }


    /**
     * Registers a new moderator.
     *
     * @param creationDto Data for creating a new moderator.
     * @return Information DTO of the registered moderator.
     * @throws UserAlreadyTakenException If the email is already taken by another user.
     */
    public UserInfoDto register(UserCreationDto creationDto) throws UserAlreadyTakenException {
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
                            .enableAppNotification(true)
                            .enableEmailNotification(true)
                            .build();
            Moderator savedModerator = moderatorRepository.save(moderator);
            return UserInfoDto.builder()
                    .firstName(moderator.getFirstName())
                    .lastName(moderator.getLastName())
                    .isActive(true)
                    .email(moderator.getEmail())
                    .uuid(savedModerator.getUserId())
                    .role(moderator.getRole())
                    .build()
                    ;
        }
    }

    /**
     * Authenticates a moderator.
     *
     * @param loginDto Login request data.
     * @return Response DTO with moderator information and authentication token.
     * @throws UserNotFoundException    If the moderator with the specified email is not found.
     * @throws InvalidPasswordException  If the provided password is invalid.
     * @throws UserSuspendedException    If the moderator is suspended.
     */
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
