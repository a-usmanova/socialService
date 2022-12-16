package ru.skillbox.diplom.group32.social.service.utils.security;

import lombok.experimental.UtilityClass;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.skillbox.diplom.group32.social.service.config.security.JwtUser;

@UtilityClass
public class SecurityUtil {

    public static Long getJwtUserIdFromSecurityContext() {
        SecurityContext context = SecurityContextHolder.getContext();
        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) context.getAuthentication();
        JwtUser jwtUser = (JwtUser) auth.getPrincipal();
        return jwtUser.getId();
    }

    public static JwtUser getJwtUserFromSecurityContext() {
        SecurityContext context = SecurityContextHolder.getContext();
        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) context.getAuthentication();
        return (JwtUser) auth.getPrincipal();
    }

}
