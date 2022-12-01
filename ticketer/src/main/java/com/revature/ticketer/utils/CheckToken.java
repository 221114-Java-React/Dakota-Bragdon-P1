package com.revature.ticketer.utils;

import com.revature.ticketer.dtos.response.Principal;
import com.revature.ticketer.services.TokenService;

public class CheckToken {

    //Checks for valid employee priviledges
    public static boolean isValidEmployeeToken(String token, TokenService tokenService){

        if(isEmptyToken(token)) return false;
        Principal principal = tokenService.extractRequesterDetails(token);
        if (principal == null) return false;
        if(!principal.getRole().equals("e58ed763-928c-4155-bee9-fdbaaadc15f3")) return false;
        return true;
    }

    //Checks for valid manager priviledges
    public static boolean isValidManagerToken(String token, TokenService tokenService){

        if(isEmptyToken(token)) return false;
        Principal principal = tokenService.extractRequesterDetails(token);
        if (principal == null) return false;
        if(!principal.getRole().equals("e58ed763-928c-4155-bee9-fdbaaadc15f4")) return false;
        return true;
    }

    //Checks for valid admin priviledges
    public static boolean isValidAdminToken(String token, TokenService tokenService){

        if(isEmptyToken(token)) return false;
        Principal principal = tokenService.extractRequesterDetails(token);
        if (principal == null) return false;
        if(!principal.getRole().equals("e58ed763-928c-4155-bee9-fdbaaadc15f5")) return false;
        return true;
    }

    public String getOwner(String token, TokenService tokenService){
        //if 
        return "";
    }

    public static boolean isEmptyToken(String token){
        return (token == null || token.isEmpty()) ? true : false;
    }
}