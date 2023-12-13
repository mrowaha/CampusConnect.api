package com.campusconnect.ui.user.service;

import com.campusconnect.domain.user.entity.User;
import com.campusconnect.domain.user.repository.BilkenteerRepository;
import com.campusconnect.domain.user.repository.ModeratorRepository;
import com.campusconnect.ui.user.exceptions.UserNotFoundException;

import java.util.Objects;
import java.util.UUID;

public interface UserService {
    User loadUserByUsername(String email) throws UserNotFoundException;
}
