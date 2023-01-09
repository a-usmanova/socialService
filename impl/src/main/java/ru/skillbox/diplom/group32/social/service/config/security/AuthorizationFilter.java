package ru.skillbox.diplom.group32.social.service.config.security;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
@Slf4j
@Component
public class AuthorizationFilter extends GenericFilterBean {


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("Start auth filter");
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        Cookie[] cookies = req.getCookies();
        Cookie cookie = null;

        if (cookies == null) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            cookie = Arrays.stream(req.getCookies()).
                    filter(x -> x.getName().equals("jwt")).findFirst().orElse(null);

            HttpServletResponse resp = (HttpServletResponse) servletResponse;
            if (cookie != null && !cookie.getValue().equals("")) {
                log.info("Cookie val - {}" + cookie.getValue());
                resp.addHeader("Authorization", "Bearer_" + cookie.getValue());
            }

            filterChain.doFilter(req, resp);
        }
    }
}