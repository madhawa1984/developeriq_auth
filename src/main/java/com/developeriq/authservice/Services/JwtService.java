package com.developeriq.authservice.Services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    private static final String SEC_KEY = "Ag4KNg54rH9MXGDvdpMVuxLuCk5WoAnq7EqZxHjF5s8Uk/N89ffsNHYpCQKTcVt2";

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    // this was called from the auth filter but need to be called by /validate end point
    public boolean validateToken(String token, UserDetails userDetails) {
        final  String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !hasExpiredToken(token));

    }

    private boolean hasExpiredToken(String token) {
        return extractExpireDate(token).before(new Date());
    }

    private Date extractExpireDate(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String generateToken(Map<String, Object> extractClaims, UserDetails userDetails) {
        return Jwts.builder().setClaims(extractClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+ 1000 * 60 * 24))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();

    }
    public String extractUserName(String jwtToken) {
      return extractClaim(jwtToken, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
      final Claims claims = getClaims(token);
      return claimResolver.apply(claims);
    }

    private Claims getClaims(String token) {
      return Jwts.parserBuilder()
              .setSigningKey(getSigningKey())
              .build()
              .parseClaimsJws(token)
              .getBody();
    }

    private Key getSigningKey() {
      byte[] keyByte = Decoders.BASE64.decode(SEC_KEY);
      return Keys.hmacShaKeyFor(keyByte);
    }

}
