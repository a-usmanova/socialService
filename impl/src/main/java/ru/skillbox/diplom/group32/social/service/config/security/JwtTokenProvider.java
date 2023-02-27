package ru.skillbox.diplom.group32.social.service.config.security;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.JwsAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;
import ru.skillbox.diplom.group32.social.service.model.auth.Role;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {

    @Value("${jwt.token.secret}")
    private String secret;

    private final JwtUserDetailsService jwtUserDetailsService;


    @PostConstruct
    protected void init() {
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    public JwtDecoder jwtDecoder() {
        SecretKey originalKey = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(originalKey).build();
        return jwtDecoder;
    }

    public JwtEncoder jwtEncoder() {
        SecretKey key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
        JWKSource<SecurityContext> immutableSecret = new ImmutableSecret<>(key);
        return new NimbusJwtEncoder(immutableSecret);
    }

    public String createToken(Long userId, String email, List<Role> roles) {
        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .claim("id", userId)
                .claim("email", email)
                .claim("roles", getRoleNames(roles))
                .expiresAt(ZonedDateTime.now().plusHours(3).toInstant())
                .build();
        JwsAlgorithm jwsAlgorithm = () -> JWSAlgorithm.HS256.getName();

        return jwtEncoder()
                .encode(JwtEncoderParameters.from(JwsHeader.with(jwsAlgorithm).build(), jwtClaimsSet))
                .getTokenValue();

    }

    public Authentication getAuthentication(String token) {
        Jwt jwt = jwtDecoder().decode(token);

        if (jwt.getClaim("roles").toString().equals("SERVICE") && jwt.getClaim("email").toString().equals("survey@mail.ru")) {
            UserDetails userDetails = jwtUserDetailsService.loadSurvey(jwt);
            return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
        }

        UserDetails userDetails = jwtUserDetailsService.loadUserFromJwt(jwt);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String resolveToken(HttpServletRequest request) {
        String skillToken = request.getHeader("Authorization");
        log.info("Before check ResolvedToken with Bearer - " + skillToken);
        if (skillToken != null && skillToken.startsWith("Bearer ")) {
            log.info("ResolvedToken - " + skillToken.substring(7, skillToken.length()));
            return skillToken.substring(7, skillToken.length());
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {

            Jwt jwt = jwtDecoder().decode(token);

            if (jwt.getExpiresAt().isAfter(ZonedDateTime.now().toInstant())) {
                return true;
            }

        } catch (JwtException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        return false;
    }

    private List<String> getRoleNames(List<Role> userRoles) {
        List<String> result = new ArrayList<>();

        userRoles.forEach(role -> {
            result.add(role.getName());
        });

        return result;
    }

    public Long getCurrentUserIdFromJwt(String token) {
        Jwt jwt = jwtDecoder().decode(token);
        Long id = jwt.getClaim("id");
        log.info("Current User from JWT in WebSocketSession: {}", id);
        return id;
    }
}
