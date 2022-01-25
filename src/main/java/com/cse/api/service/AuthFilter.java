package com.cse.api.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cse.api.constants.Constants;

import java.io.IOException;

public class AuthFilter extends GenericFilterBean {
    public String useremail;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        String authHeader = httpRequest.getHeader("Authorization");
        if (authHeader != null) {
            String[] authHeaderArr = authHeader.split("Bearer ");
            if (authHeaderArr.length > 1 && authHeaderArr[1] != null) {
                String token = authHeaderArr[1];
                // System.out.print(token);
                try {
                    Claims claims = Jwts.parser().setSigningKey(Constants.API_SECRET_KEY)
                            .parseClaimsJws(token).getBody();

                    useremail = claims.get("email").toString();
                    // System.out.println("set user id" + claims.get("role").toString());

                    httpRequest.setAttribute("userId",
                            Long.parseLong(claims.get("userId").toString()));
                    httpRequest.setAttribute("role",
                            Integer.parseInt(claims.get("role").toString()));

                    httpRequest.setAttribute("email", claims.get("email").toString());

                } catch (Exception e) {
                    System.out.println(e.toString());
                    httpResponse.sendError(HttpStatus.FORBIDDEN.value(), "invalid/expired please login again");
                    return;
                }
            } else {
                httpResponse.sendError(HttpStatus.FORBIDDEN.value(),
                        "Invalid token, Authorization token must be Bearer [token]");
                return;
            }
        } else {
            httpResponse.sendError(HttpStatus.FORBIDDEN.value(), "Authorization token must be provided ");
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

}
