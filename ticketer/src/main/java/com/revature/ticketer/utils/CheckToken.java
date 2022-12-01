package com.revature.ticketer.utils;

import com.revature.ticketer.Exceptions.InvalidAuthException;
import com.revature.ticketer.dtos.response.Principal;
import com.revature.ticketer.services.TokenService;

public class CheckToken {

    //Checks for valid employee priviledges
    public static boolean isValidEmployeeToken(String token, TokenService tokenService){

        if(isEmptyToken(token)) throw new InvalidAuthException("ERROR: You are not signed in");
        Principal principal = tokenService.extractRequesterDetails(token);
        if (principal == null) throw new InvalidAuthException("ERROR: Invalid Token");
        if(!principal.getRole().equals("e58ed763-928c-4155-bee9-fdbaaadc15f3")) throw new InvalidAuthException("ERROR: You lack authorization to do this");

        return true;
    }

    //Checks for valid manager priviledges
    public static boolean isValidManagerToken(String token, TokenService tokenService){

        if(isEmptyToken(token)) throw new InvalidAuthException("ERROR: You are not signed in");
        Principal principal = tokenService.extractRequesterDetails(token);
        if (principal == null) throw new InvalidAuthException("ERROR: Invalid Token");
        if(!principal.getRole().equals("e58ed763-928c-4155-bee9-fdbaaadc15f4")) throw new InvalidAuthException("ERROR: You lack authorization to do this");

        return true;
    }

    //Checks for valid admin priviledges
    public static boolean isValidAdminToken(String token, TokenService tokenService){

        if(isEmptyToken(token)) throw new InvalidAuthException("ERROR: You are not signed in");
        Principal principal = tokenService.extractRequesterDetails(token);
        if (principal == null) throw new InvalidAuthException("ERROR: Invalid Token");
        if(!principal.getRole().equals("e58ed763-928c-4155-bee9-fdbaaadc15f5")) throw new InvalidAuthException("ERROR: You lack authorization to do this");

        return true;
    }

    public static boolean isEmptyToken(String token){
        return (token == null || token.isEmpty()) ? true : false;
    }
}
