package org.example.springjwt;

import io.jsonwebtoken.Jwts;
import jakarta.xml.bind.DatatypeConverter;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;

public class JwtServiceTest {

    @Test
    void generateSecretKey()
    {
      SecretKey secretKey= Jwts.SIG.HS256.key().build();
      String key= DatatypeConverter.printHexBinary(secretKey.getEncoded());

        System.out.println("key : "+key);
    }
}
