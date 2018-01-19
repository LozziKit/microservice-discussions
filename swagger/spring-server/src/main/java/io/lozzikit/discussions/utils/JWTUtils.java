package io.lozzikit.discussions.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

/**
 * Simplified from https://www.toptal.com/java/rest-security-with-jwt-spring-security-and-java
 */
public class JWTUtils {

    /**
     * Parse a token and get the associated userId
     */
    public long getUserId(String token) {
        // We are not checking signature for now
        Claims claims = Jwts.parser().parseClaimsJws(token).getBody();

        return claims.get("userid", Long.class);
    }

}