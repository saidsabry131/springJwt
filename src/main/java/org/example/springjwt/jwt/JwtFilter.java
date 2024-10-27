package org.example.springjwt.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.springjwt.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter  extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final ApplicationContext context;

    private final MyUserDetailsService userDetailsService;

    @Autowired
    public JwtFilter(JwtService jwtService, ApplicationContext context, MyUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.context = context;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader=request.getHeader("Authorization");
        String token;
        String username;

        if(authHeader==null ||!authHeader.startsWith("Bearer "))
        {
            filterChain.doFilter(request,response);
            return;

        }

        token=authHeader.substring(7);
        username = jwtService.extractUsername(token);
//        if(username !=null && SecurityContextHolder.getContext().getAuthentication()==null)
//        {
//            UserDetails userDetails= context.getBean(MyUserDetailsService.class).loadUserByUsername(username);
//            if(jwtService.validateToken(token,userDetails))
//            {
//                UsernamePasswordAuthenticationToken authenticationToken
//                        =new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
//
//                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//
//                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//            }
//        }

        if(username!=null && SecurityContextHolder.getContext().getAuthentication() == null)
        {
            UserDetails userDetails
                    =userDetailsService.loadUserByUsername(username);


            if(userDetails!= null &&jwtService.isValidateToken(token))
            {
                UsernamePasswordAuthenticationToken authenticationToken
                        =new UsernamePasswordAuthenticationToken(username,userDetails.getPassword(),userDetails.getAuthorities());

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            }
        }

        filterChain.doFilter(request,response);
    }
}
