package com.campusconnect.ui.user.service;

import com.campusconnect.domain.admin.dto.UserSuspendRequestDto;
import com.campusconnect.domain.admin.dto.UserSuspendResponseDto;
import com.campusconnect.domain.ProductTag.entity.ProductTag;
import com.campusconnect.domain.ProductTag.repository.ProductTagRepository;
import com.campusconnect.domain.security.dto.BearerToken;
import com.campusconnect.domain.user.dto.*;
import com.campusconnect.domain.user.entity.User;
import com.campusconnect.domain.user.pojo.BilkenteerPhoneNumbers;
import com.campusconnect.domain.user.repository.ModeratorRepository;
import com.campusconnect.ui.productTag.exceptions.TagNotFoundException;
import com.campusconnect.ui.utils.JwtUtilities;
import com.campusconnect.ui.user.exceptions.InvalidPasswordException;
import com.campusconnect.ui.user.exceptions.UserAlreadyTakenException;
import com.campusconnect.ui.user.exceptions.UserNotFoundException;
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
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class BilkenteerService implements UserService {

    // Repositories and utilities for database and authentication operations
    private final BilkenteerRepository bilkenteerRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModeratorRepository moderatorRepository;
    private final ProductTagRepository productTagRepository;
    private final JwtUtilities jwtUtilities;

    // Implementation of UserService method to load a user by username (email)
    @Override
    public User loadUserByUsername(String email) throws UsernameNotFoundException {
        return bilkenteerRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException("User not found"));
    }

    // Implementation of UserService method to list all Bilkenteer users
    @Override
    public List<UserInfoDto> listAll() {
        List<Bilkenteer> bilkenteers = bilkenteerRepository.findAll();
        return bilkenteers.stream()
                .map(bilkenteer -> UserInfoDto.builder()
                        .uuid(bilkenteer.getUserId())
                        .email(bilkenteer.getEmail())
                        .lastName(bilkenteer.getLastName())
                        .firstName(bilkenteer.getFirstName())
                        .role(bilkenteer.getRole())
                        .isActive(!bilkenteer.getIsSuspended())
                        .build()
                )
                .toList();
    }

    // Implementation of UserService method to suspend a Bilkenteer user
    @Override
    public UserSuspendResponseDto suspend(UserSuspendRequestDto suspendRequestDto) throws UserNotFoundException {
        bilkenteerRepository.findById(suspendRequestDto.getUuid())
                .orElseThrow(UserNotFoundException::new);
        bilkenteerRepository.disable(suspendRequestDto.getUuid());
        return UserSuspendResponseDto.builder()
                .uuid(suspendRequestDto.getUuid())
                .successStatus(true)
                .message(String.format("Bilkenteer %s suspended", suspendRequestDto.getUuid().toString()))
                .build();
    }

    // Implementation of UserService method to unsuspend a Bilkenteer user
    @Override
    public UserSuspendResponseDto unsuspend(UserSuspendRequestDto suspendRequestDto) throws UserNotFoundException {
        bilkenteerRepository.findById(suspendRequestDto.getUuid())
                .orElseThrow(UserNotFoundException::new);
        bilkenteerRepository.enable(suspendRequestDto.getUuid());
        return UserSuspendResponseDto.builder()
                .uuid(suspendRequestDto.getUuid())
                .successStatus(true)
                .message(String.format("Bilkenteer %s activated", suspendRequestDto.getUuid().toString()))
                .build();
    }

    /**
     * Registers a new Bilkenteer user.
     *
     * @param creationDto Information about the user to be created.
     * @return BearerToken containing the authentication token for the registered user.
     * @throws UserAlreadyTakenException If the user with the same email already exists.
     */
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
                    .subscribedTags(new HashSet<>())
                    .build();
            bilkenteerRepository.save(bilkenteer);
            String token = jwtUtilities.generateToken(creationDto.getEmail(), Role.BILKENTEER);
            return new BearerToken(token , "Bearer ");
        }
    }

    /**
     * Authenticates a Bilkenteer user based on login credentials.
     *
     * @param loginDto Information about the user's login credentials.
     * @return BilkenteerLoginResponse containing user information and authentication token.
     * @throws UserNotFoundException    If the user with the specified email is not found.
     * @throws InvalidPasswordException If the provided password is invalid.
     * @throws UserSuspendedException   If the user is suspended.
     */
    public BilkenteerLoginResponse authenticate(UserLoginRequestDto loginDto)
            throws UserNotFoundException,InvalidPasswordException, UserSuspendedException
    {
        Bilkenteer bilkenteer = bilkenteerRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(UserNotFoundException::new);

        System.out.println("USER id = " + bilkenteer.getUserId());

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
    /**
     * Adds contact information to a Bilkenteer user profile.
     *
     * @param userid          ID of the Bilkenteer user.
     * @param contactInfoDto  Information about the contact details to be added.
     */
    public void addContactInfo(UUID userid, BilkenteerContactInfoDto contactInfoDto) {
        bilkenteerRepository.updateAddressBy(
                userid,
                contactInfoDto.getAddress(),
                contactInfoDto.getPhoneNumbers()
        );
    }

    /**
     * Retrieves contact information of a Bilkenteer user.
     *
     * @param userId ID of the Bilkenteer user.
     * @return BilkenteerContactInfoDto containing contact information.
     */
    public BilkenteerContactInfoDto getContactInfo(UUID userId) {
        Bilkenteer bilkenteer = bilkenteerRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        return BilkenteerContactInfoDto.builder()
                .phoneNumbers(bilkenteer.getPhoneNumbers())
                .address(bilkenteer.getAddress())
                .build();
    }

    /**
     * Finds a user by ID, searching in both Bilkenteer and Moderator repositories.
     *
     * @param userId ID of the user to find.
     * @return Found user or null if not found.
     */
    public User findUser(UUID userId) {

        User user = moderatorRepository.findById(userId).orElse(null);

        if (Objects.isNull(user)) {
            user = bilkenteerRepository.findById(userId).orElse(null);
        }

        return user;
    }

    /**
     * Subscribes a Bilkenteer user to a product tag.
     *
     * @param bilkenteerId ID of the Bilkenteer user.
     * @param tagName       Name of the product tag to subscribe to.
     * @return Updated Bilkenteer user with the subscribed tag.
     */
    public Bilkenteer subscribeToTag(UUID bilkenteerId, String tagName) {
        Bilkenteer bilkenteer = bilkenteerRepository.findById(bilkenteerId)
                .orElseThrow(UserNotFoundException::new);

        ProductTag tag = productTagRepository.findByName(tagName)
                .orElseThrow(TagNotFoundException::new);

        bilkenteer.getSubscribedTags().add(tag);
        bilkenteerRepository.save(bilkenteer);

        return bilkenteer;
    }

    /**
     * Unsubscribes a Bilkenteer user from a product tag.
     *
     * @param bilkenteerId ID of the Bilkenteer user.
     * @param tagName       Name of the product tag to unsubscribe from.
     * @return Updated Bilkenteer user without the unsubscribed tag.
     */
    public Bilkenteer unsubscribeFromTag(UUID bilkenteerId, String tagName) {
        Bilkenteer bilkenteer = bilkenteerRepository.findById(bilkenteerId)
                .orElseThrow(UserNotFoundException::new);

        ProductTag tag = productTagRepository.findByName(tagName)
                .orElseThrow(TagNotFoundException::new);

        bilkenteer.getSubscribedTags().remove(tag);
        bilkenteerRepository.save(bilkenteer);

        return bilkenteer;
    }

}
