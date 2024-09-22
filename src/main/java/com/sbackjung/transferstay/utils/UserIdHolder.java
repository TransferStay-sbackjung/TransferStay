package com.sbackjung.transferstay.utils;

import com.sbackjung.transferstay.config.exception.CustomException;
import com.sbackjung.transferstay.config.exception.ErrorCode;
import com.sbackjung.transferstay.dto.UserDetailsDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserIdHolder {
    public static Long getUserIdFromToken(){
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated()){
            throw new CustomException(ErrorCode.UN_AUTHORIZE,"인증되지않은 사용자입니다.");
        }

        UserDetailsDto user =
                (UserDetailsDto) authentication.getPrincipal();

        if(user == null){
            throw new CustomException(ErrorCode.INTER_SERVER_ERROR);
        }
        return user.getUserId();
    }
}
