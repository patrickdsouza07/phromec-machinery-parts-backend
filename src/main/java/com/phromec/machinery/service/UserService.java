package com.phromec.machinery.service;

import com.phromec.machinery.dto.RegisterRequest;
import com.phromec.machinery.model.User;
import com.phromec.machinery.dto.UserResponse;
import com.phromec.machinery.dto.UserCreateRequest;
import com.phromec.machinery.dto.UserUpdateRequest;

import java.util.List;

public interface UserService {

    List<UserResponse> getAllUsers();

    User registerUser(RegisterRequest user);

    UserResponse createUser(UserCreateRequest request);

    UserResponse updateUser(UserUpdateRequest request);

    void deleteUser(Integer userId);
}
