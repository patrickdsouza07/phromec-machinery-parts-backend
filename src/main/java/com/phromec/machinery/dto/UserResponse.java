package com.phromec.machinery.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserResponse {

    private Integer userId;
    private String fullName;
    private String email;
    private String phone;
    private String roleName;

    public UserResponse(
            Integer userId,
            String fullName,
            String email,
            String phone,
            String roleName
    ) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.roleName = roleName;
    }

}
