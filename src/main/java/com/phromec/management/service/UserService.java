package com.phromec.management.service;

import com.phromec.management.model.RegisterRequest;
import com.phromec.management.model.User;
import com.phromec.management.model.UserResponse;

import java.util.List;

public interface UserService {

    List<UserResponse> getAllUsers();

    User registerUser(RegisterRequest user);
}
