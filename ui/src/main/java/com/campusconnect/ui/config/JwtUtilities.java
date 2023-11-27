package com.campusconnect.ui.config;

import com.campusconnect.ui.common.exceptions.InvalidRoleException;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;


import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.function.Function;

@Slf4j
@Component
public class JwtUtilities{

    private final RoledJwtProperties secrets;


    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    @Autowired
    public JwtUtilities(RoledJwtProperties secrets) {
        this.secrets = secrets;
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractRole(String token) {
        final Claims claims = extractAllClaims(token);
        if (claims == null) {
            return null;
        }
        return (String)claims.get("role");
    }

    public Claims extractAllClaims(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser().setSigningKey(secrets.getBilkenteerSecret()).parseClaimsJws(token).getBody();
        } catch (SignatureException e) {
            claims = Jwts.parser().setSigningKey(secrets.getModeratorSecret()).parseClaimsJws(token).getBody();
        } finally {
            return claims;
        }
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    public Date extractExpiration(String token) { return extractClaim(token, Claims::getExpiration); }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String email = extractUsername(token);
        return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(String email , String role) {
        if (role.equals("BILKENTEER")) {
            return Jwts.builder().setSubject(email).claim("role",role).setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(Date.from(Instant.now().plus(jwtExpiration, ChronoUnit.MILLIS)))
                    .signWith(SignatureAlgorithm.HS256, secrets.getBilkenteerSecret()).compact();
        } else {
            return Jwts.builder().setSubject(email).claim("role",role).setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(Date.from(Instant.now().plus(jwtExpiration, ChronoUnit.MILLIS)))
                    .signWith(SignatureAlgorithm.HS256, secrets.getModeratorSecret()).compact();
        }
    }

    public boolean validateToken(String token, String role) {
        try {
            if (role.equals("BILKENTEER")) {
                Jwts.parser().setSigningKey(secrets.getBilkenteerSecret()).parseClaimsJws(token);
            } else if (role.equals("MODERATOR")) {
                Jwts.parser().setSigningKey(secrets.getModeratorSecret()).parseClaimsJws(token);
            } else {
                throw new InvalidRoleException();
            }
            return true;
        } catch (SignatureException e) {
            log.info("Invalid JWT signature.");
            log.trace("Invalid JWT signature trace: {}", e);
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token.");
            log.trace("Invalid JWT token trace: {}", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
            log.trace("Expired JWT token trace: {}", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
            log.trace("Unsupported JWT token trace: {}", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
            log.trace("JWT token compact of handler are invalid trace: {}", e);
        } catch (InvalidRoleException e) {
            log.info("JWT token has invalid role field, {}", role);
        }
        return false;
    }

    public String getToken (HttpServletRequest httpServletRequest) {
        final String bearerToken = httpServletRequest.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer "))
        {
            return bearerToken.substring(7, bearerToken.length());
        } // The part after "Bearer "
        return null;
    }

}
