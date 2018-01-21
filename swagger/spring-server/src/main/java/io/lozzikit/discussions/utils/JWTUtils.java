package io.lozzikit.discussions.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Simplified from https://www.toptal.com/java/rest-security-with-jwt-spring-security-and-java
 */
public class JWTUtils {

    // We are using the "PasImprimanteLouperSYM" as a "secret"
    // (it seems to be what the user team is using)
    private final static String SECRET = "UGFzSW1wcmltYW50ZUxvdXBlclNZTQ==";

    public static class UserInfo{
        private long userId;
        private String username;
        public UserInfo(long userId, String username){
            this.userId = userId;
            this.username = username;
        }

        public long getUserId() {
            return userId;
        }

        public String getUsername() {
            return username;
        }
    }

    /**
     * Generates a JWT token containing username as subject, and userId and role as additional claims. These properties are taken from the specified
     * User object. Tokens validity is infinite.
     *
     * @param u the user for which the token will be generated
     * @return the JWT token
     */
    public String generateToken(UserInfo u) {
        Claims claims = Jwts.claims().setSubject(u.getUsername());
        claims.put("userID", u.getUserId() + "");

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }


    /**
     * Parse a token and get the associated userId
     */
    public static UserInfo getUserInfo(String token) {
        // Getting the token only (doing away with the "bearer" word)
        Pattern pattern = Pattern.compile("^(bearer )?(.*)$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(token);

        matcher.matches();
        token = matcher.group(matcher.groupCount());


        // Parsing the JWT
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();

        long userId = Long.parseLong(claims.get("userID", String.class));
        String username = claims.getSubject();

        return new UserInfo(userId, username);
    }

}