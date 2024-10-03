package com.sbackjung.transferstay.dto;

import com.sbackjung.transferstay.domain.UserDomain;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
@Builder
public class UserDetailsDto implements UserDetails {
    private final UserDomain userDomain;

    public UserDetailsDto(UserDomain userDomain){
        this.userDomain = userDomain;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 권한이 null 또는 빈 문자열인 경우 기본 권한을 설정
        String role = userDomain.getRole();
        if (role == null || role.isEmpty()) {
            role = "ROLE_USER";  // 기본 권한 설정
        }
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() {
        return this.userDomain.getPassword();
    }

    @Override
    public String getUsername() {
        return this.userDomain.getEmail();
    }

    public Long getUserId() {
        return this.userDomain.getUserId();
    }
}
