package org.example.springjwt.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.springjwt.entity.Users;
import org.example.springjwt.jwt.JwtService;
import org.example.springjwt.service.MyUserDetailsService;
import org.example.springjwt.service.UsersPrincipal;
import org.example.springjwt.service.UsersService;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.http.HttpCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UsersService usersService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager manager;

    // final UsersPrincipal usersPrincipal;

    private final MyUserDetailsService userDetailsService;

    private final JwtService jwtService;

    public UserController(UsersService usersService, PasswordEncoder passwordEncoder, AuthenticationManager manager, MyUserDetailsService userDetailsService, JwtService jwtService) {
        this.usersService = usersService;
        this.passwordEncoder = passwordEncoder;
        this.manager = manager;
       // this.usersPrincipal = usersPrincipal;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    @GetMapping("/")
    public String home(HttpServletRequest request)
    {
        HttpSession session=request.getSession();

        Cookie cookie=new Cookie("session_id",session.getId());
        Cookie[] cookies=request.getCookies();

        return "home <br> cookies "+cookie.getValue()+"<br> sessions :  "+session.getId();
    }

    @GetMapping("/admin/home")
    public String adminHome()
    {
        return "welcome in admin page";
    }

    @GetMapping("/user/home")
    public String userHome()
    {
        return "welcome in user page";
    }

    @PostMapping("/register")
   public Users register(@RequestBody Users users)
    {

        users.setPassword(passwordEncoder.encode(users.getPassword()));
        return usersService.register(users);
    }

    @PostMapping("/api/login")
    public String login(@RequestBody Users users, HttpServletRequest request)
    {
        Authentication authentication= manager.authenticate(new UsernamePasswordAuthenticationToken(users.getUsername(),users.getPassword()));

        if(authentication.isAuthenticated())
        {
            UserDetails principal= userDetailsService.loadUserByUsername(users.getUsername());
             String token = jwtService.generateToken(principal);


            return "successfully \n"+"token\n"+ token+"\n user :"+users;

        }else{
            return "failed";
        }


    }




}
