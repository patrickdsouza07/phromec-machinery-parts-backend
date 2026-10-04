package com.phromec.management.security;

import com.phromec.management.model.Role;
import com.phromec.management.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {
    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        Role userRole = user.getRole();
            authorities.add(new SimpleGrantedAuthority("ROLE_" + userRole.getRoleName()));
            userRole.getPermissions().forEach(permission ->
                authorities.add(new SimpleGrantedAuthority(permission.getPermissionName()))
            );
        return authorities;
    }

    @Override public String getPassword() { return user.getPasswordHash(); }
    @Override public String getUsername() { return user.getUsername(); }
    @Override public boolean isEnabled() { return user.isActive(); }
}
