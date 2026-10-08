package com.phromec.machinery.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequest {
    private Integer userId;
    private String fullName;
    private String email;
    private String role;
    private Boolean status;
}
