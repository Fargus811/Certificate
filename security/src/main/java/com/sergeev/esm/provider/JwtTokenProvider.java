package com.sergeev.esm.provider;

import com.sergeev.esm.exception.JwtAuthenticationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * The type Jwt token provider.
 */
@Component
@RequiredArgsConstructor
@EnableConfigurationProperties
public class JwtTokenProvider {

    private static final String BEARER = "Bearer ";
    private static final String EMPTY_LINE = "";
    private static final int BEARER_HEADER_OFFSET = 7;
    private static final int CONVERT_TO_MILLIS = 1000;

    @Value("${jwt.secret}")
    private String SECRET ;
    @Value("${jwt.token.validity}")
    private long JWT_TOKEN_VALIDITY;

    private final UserDetailsService userDetailsService;

    /**
     * Gets username from token.
     *
     * @param token the token
     * @return the username from token
     */
    public String getUsernameFromToken(String token) {
        String userName;
        try {
            userName = getClaimFromToken(token, Claims::getSubject);
        } catch (Exception e) {
            throw new JwtAuthenticationException("JWT token is expired or invalid");
        }
        return userName;
    }

    private Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * Generate token string.
     *
     * @param userDetails the user details
     * @return the string
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }

    @SneakyThrows
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * CONVERT_TO_MILLIS))
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    /**
     * Is token valid boolean.
     *
     * @param token       the token
     * @param userDetails the user details
     * @return the boolean
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Resolve token string.
     *
     * @param req the req
     * @return the string
     */
    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader(AUTHORIZATION);
        if (bearerToken != null && bearerToken.startsWith(BEARER)) {
            return bearerToken.substring(BEARER_HEADER_OFFSET);
        }
        return null;
    }

    /**
     * Validate token boolean.
     *
     * @param token the token
     * @return the boolean
     */
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtAuthenticationException("JWT token is expired or invalid");
        }
    }

    /**
     * Gets authentication.
     *
     * @param token the token
     * @return the authentication
     */
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, EMPTY_LINE, userDetails.getAuthorities());
    }

    private String getUsername(String token) {
        String userName;
        try {
            userName = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody().getSubject();
        } catch (Exception e) {
            throw new JwtAuthenticationException("JWT token is expired or invalid");
        }
        return userName;
    }
}
