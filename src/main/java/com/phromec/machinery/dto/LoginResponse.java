package com.phromec.machinery.dto;

import java.util.List;

public record LoginResponse(String token, String username, String fullName, String email, List<String> roles) {

}

