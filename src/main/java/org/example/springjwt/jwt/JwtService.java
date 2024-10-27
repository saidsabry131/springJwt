package org.example.springjwt.jwt;



import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import java.util.stream.Collectors;

@Service
public class JwtService {

    private final String secretKey="D37C74B31448766BAC4BEAC32AC88F106B5BC45517B1CBEA5C99EB43B0384DB2";
    private final static Long VALIDITY= TimeUnit.MINUTES.toMillis(30);
    public JwtService() {

    }

    private SecretKey getKey() {
       // byte[] keyBytes = Decoders.BASE64.decode(secretKey);
       // return Keys.hmacShaKeyFor(keyBytes);

        byte[] key= Base64.getDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(key);
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("User","user");
       // claims.put("Roles",userDetails.getAuthorities().stream().map(authority->"ROLE_"+authority.getAuthority()).collect(Collectors.toList()));
        System.out.println("roles : "+userDetails.getAuthorities());
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+VALIDITY))
                .signWith(getKey())
                .compact();
    }

    public String extractUsername(String token) {
        Claims claims = getClaims(token);


        return claims.getSubject();
    }

    private Claims getClaims(String token) {
        Claims claims= Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims;
    }


//    public boolean validateToken(String token, UserDetails userDetails) {
//        final String username = extractUsername(token);
//        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
//    }



    public boolean isValidateToken(String token) {
        Claims claims=getClaims(token);
        return claims.getExpiration().after(new Date(System.currentTimeMillis()));
    }
}
