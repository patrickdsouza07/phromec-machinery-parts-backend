package com.phromec.machinery.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateRequest {
    private String fullName;
    private String email;
    private String role;
    private Boolean status;
    private String temporaryPassword;
}
