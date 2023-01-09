package ru.skillbox.diplom.group32.social.service.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.csrf.CsrfFilter;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final WhiteList whiteList;
    private final JwtTokenProvider jwtTokenProvider;
    private static final String LOGOUT_ENDPOINT = "/**/logout";


    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .cors()
                .and()
                .csrf().disable()
                .addFilterBefore(new AuthorizationFilter(), CsrfFilter.class)
                .antMatcher("/**/swagger-ui/*").anonymous()
                .and()
                .authorizeRequests()
                .antMatchers(whiteList.getLinks()).permitAll()
                .and()
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .logout(
                        logout -> {
                            logout
                                    .logoutUrl(LOGOUT_ENDPOINT)
                                    .logoutSuccessUrl("/")
                                    .deleteCookies("jwt")
                                    .logoutSuccessHandler(getLogoutSuccess());
                        }
                )
                .addFilterBefore(new JwtTokenFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        return httpSecurity.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    protected LogoutSuccessHandler getLogoutSuccess() {
        return (httpServletRequest, httpServletResponse, auth) -> httpServletResponse.setStatus(HttpServletResponse.SC_OK);
    }
}
