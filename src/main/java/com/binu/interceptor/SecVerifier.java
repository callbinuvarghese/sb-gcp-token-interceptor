package com.binu.interceptor;


import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;


public class SecVerifier {
    private final Logger log = LoggerFactory.getLogger(SecVerifier.class);

    @Value("${oauth2.ClientID}")
    private String CLIENT_ID;
    private String CLIENT_ID_SUFFIX=".apps.googleusercontent.com"; //Only used for checking; not appended to the CLIENT_ID;
    // Uncomment above lines to read it from application-xxx.properties file
    //private static final String CLIENT_ID = "enter ClientID here; You can get it from Jim/Nicole";
    //private static final String CLIENT_ID = "4xxxx.apps.googleusercontent.com";

    public void setCLIENT_ID(String clientId){
        this.CLIENT_ID = clientId;
    }

    public boolean verify(String idTokenString) {
        //GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
        // Verify valid token, signed by google.com, intended for 3P
        NetHttpTransport transport = new NetHttpTransport();

        JsonFactory jsonFactory = new GsonFactory();
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                // Specify the CLIENT_ID of the app that accesses the backend:
                .setAudience(Collections.singletonList(CLIENT_ID))
                // Or, if multiple clients access the backend:
                //.setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
                .build();
        log.info("Created GoogleIdTokenVerifier");
        try {
            log.info("GoogleIdTokenVerifier.verify");
            GoogleIdToken idToken = verifier.verify(idTokenString);
            log.info("GoogleIdTokenVerifier.verify result:{}",idToken);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();
                // Print user identifier
                String userId = payload.getSubject();
                log.debug("User ID: " + userId);
                // Get profile information from payload
                String email = payload.getEmail();
                boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
                String name = (String) payload.get("name");
                String pictureUrl = (String) payload.get("picture");
                String locale = (String) payload.get("locale");
                String familyName = (String) payload.get("family_name");
                String givenName = (String) payload.get("given_name");
                String hd =(String) payload.get("hd");
                log.info("Logged in email:{} given:{}, family:{}, hd:{}; emailOk:{} ", email, givenName,familyName, hd, emailVerified);
                return  true;
            } else {
                log.error("Could not verify IdToken");
                return false;
            }
        } catch (GeneralSecurityException e) {
            log.error("GeneralSecurityException while validating token", e);
        } catch (IOException e) {
            log.error("IOException while validating token", e);
        }
        return false;

    }

    public boolean isValid_CLIENT_ID() {
        if (CLIENT_ID!=null) {
            if (CLIENT_ID.endsWith(CLIENT_ID_SUFFIX)) {
                log.debug("CLIENT_ID seems valid");
                return true;
            } else {
                log.error("Client ID for Security Token validation does not have the proper suffix:{}", CLIENT_ID_SUFFIX);
                return false;
            }
        } else {
            log.error("Client ID for Security Token validation is null");
            return false;
        }
    }
}
