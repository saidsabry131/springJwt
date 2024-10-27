package org.example.springjwt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringJwtApplication {

    public static void main(String[] args) {

//        String token= "bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzYWlkIiwiaWF0IjoxNzI5NzgyNjAwLCJleHAiOjE3Mjk3ODI3MDh9.fMvLpEetRCtIL8D23v1OrWXSsUfazRFMKK_OM1mqhok";
//         token=token.substring(7);
//
//        System.out.println(token);
        SpringApplication.run(SpringJwtApplication.class, args);
    }

}
