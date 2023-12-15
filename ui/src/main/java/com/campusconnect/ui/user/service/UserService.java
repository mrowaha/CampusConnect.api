package com.campusconnect.ui.user.service;

import com.campusconnect.domain.user.entity.User;
import com.campusconnect.ui.user.exceptions.UserNotFoundException;

public interface UserService {
    User loadUserByUsername(String email) throws UserNotFoundException;
}
