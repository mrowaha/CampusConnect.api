package com.campusconnect.ui.user.service;

import com.campusconnect.domain.admin.dto.UserSuspendRequestDto;
import com.campusconnect.domain.admin.dto.UserSuspendResponseDto;
import com.campusconnect.domain.user.dto.UserInfoDto;
import com.campusconnect.domain.user.entity.User;
import com.campusconnect.ui.user.exceptions.UserNotFoundException;

import java.util.List;

public interface UserService {
    User loadUserByUsername(String email) throws UserNotFoundException;

    List<UserInfoDto> listAll();

    UserSuspendResponseDto suspend(UserSuspendRequestDto suspendRequestDto) throws UserNotFoundException;

    UserSuspendResponseDto unsuspend(UserSuspendRequestDto suspendRequestDto) throws UserNotFoundException;
}
