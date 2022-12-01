package com.revature.ticketer.utils;

import java.io.FileReader;
import java.io.IOException;
import java.security.Key;
import java.util.Properties;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import io.jsonwebtoken.SignatureAlgorithm;

public class JwtConfig {

    //Controls when the JWT expires (in ms and converted to an hour)
    private final int expiration = 60 * 60 *1000;

    //An algorithm used to encrypt  data
    private final SignatureAlgorithm sigAlg = SignatureAlgorithm.HS256;

    //Key needed to decrypt the encrypted data
    private final Key signingKey;

    //Used to read properties file
    private final Properties properties = new Properties();

    public JwtConfig() {
        try {
            properties.load(new FileReader("src/main/resources/db.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Hashing the key with a salt generator. This salt will be stored in db properties
        byte[] saltyBytes = DatatypeConverter.parseBase64Binary(properties.getProperty("salt"));
        signingKey = new SecretKeySpec(saltyBytes, sigAlg.getJcaName());
    }

    public int getExpiration() {
        return expiration;
    }

    public SignatureAlgorithm getSigAlg() {
        return sigAlg;
    }

    public Key getSigningKey() {
        return signingKey;
    }
}
