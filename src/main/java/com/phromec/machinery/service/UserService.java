package com.phromec.machinery.service;

import com.phromec.machinery.dto.RegisterRequest;
import com.phromec.machinery.model.User;
import com.phromec.machinery.dto.UserResponse;

import java.util.List;

public interface UserService {

    List<UserResponse> getAllUsers();

    User registerUser(RegisterRequest user);
}
