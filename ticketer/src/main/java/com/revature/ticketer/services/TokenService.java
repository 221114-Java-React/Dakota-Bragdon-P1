package com.revature.ticketer.services;

import java.util.Date;

import com.revature.ticketer.dtos.response.Principal;
import com.revature.ticketer.utils.JwtConfig;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;

public class TokenService {
    private JwtConfig jwtConfig;
     
    public TokenService(){
        super();
    }

    public TokenService(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    public String generateToken(Principal subject){
        long now = System.currentTimeMillis();
        JwtBuilder builtToken = Jwts.builder()
            .setId(subject.getId()) //Gets the id
            .setIssuer("ticketer") //Issued by our program
            .setIssuedAt(new Date(now))
            .setExpiration(new Date(now + jwtConfig.getExpiration())) //Expiration data is current time + expiration time (an hour)
            .setSubject(subject.getUsername()) //Gets the username
            .claim("role", subject.getRole()) //Gets role
            .signWith(jwtConfig.getSigAlg(), jwtConfig.getSigningKey());
        
        return builtToken.compact();
    }

    public Principal extractRequesterDetails(String token){
        try{
            Claims claims = Jwts.parser()
                .setSigningKey(jwtConfig.getSigningKey())
                .parseClaimsJws(token)
                .getBody();
            
            return new Principal(claims.getId(), claims.getSubject(),claims.get("role", String.class));//Returns a user's details from a token
        } catch (Exception e) {
            return null;
        }
    }
}
