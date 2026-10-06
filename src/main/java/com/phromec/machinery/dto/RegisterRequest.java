package com.phromec.machinery.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegisterRequest {

    private String username;
    private String fullName;
    private String email;
    private String password;
    private String phone;
    private String roleName;

    public RegisterRequest() {
    }

    @Override
    public String toString() {
        return "RegisterRequest{" +
                "username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", roleName='" + roleName + '\'' +
                '}';
    }
}
