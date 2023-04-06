package com.binu.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;

@Component
public class SecInterceptor implements HandlerInterceptor {
    private final Logger log = LoggerFactory.getLogger(SecInterceptor.class);
    SecVerifier secVerifier = new SecVerifier();

    public void setCliendId(String ClientID) {
        this.secVerifier.setCLIENT_ID(ClientID);
    }
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.debug("Request intercepted: " + request.getHeader("Authorization"));
        if (request.getHeader("Authorization") == null){
            log.error("Authorization not sent. Validation NOK.");
            return false;
        }
        else if (request.getHeader("Authorization").startsWith("Bearer"))    {
            String[] parts = request.getHeader("Authorization").split(" ");
            log.info("Authorization header with parts:{}",parts.length);
            if (parts.length !=2) {
                log.error("Validation failed. Token not found");
                return false;
            } else {
                log.error("Validation spliting token into chunks..");
                String tokenStr = parts[1];
                String[] chunks = tokenStr.split("\\.");
                log.info("Token with chunks:{}",chunks.length);
                if (chunks.length < 2) {
                    log.error("Validation failed. Token not standard");
                    return false;
                } else {
                    Base64.Decoder decoder = Base64.getUrlDecoder();
                    String header = new String(decoder.decode(chunks[0]));
                    String payload = new String(decoder.decode(chunks[1]));
                    log.info("headed:{}", header);
                    log.info("Payload:{}", payload);
                    if (chunks.length> 2) {
                        String signature = chunks[2];
                        log.info("signature:{}", signature);
                    } else {
                        log.error("Might have provided access token. Id token expected");
                        return false;
                    }
                    //validate Signature here using the Google Token Verifier
                    // https://developers.google.com/identity/gsi/web/guides/verify-google-id-token
                    // Uncomment the following 4 lines when you have the CLIENT ID in place.
                    if (secVerifier.isValid_CLIENT_ID()) {
                        if (!secVerifier.verify(tokenStr)) {
                            log.error("Could not verify idToken:{}",tokenStr);
                            return false;
                        }
                    } else {
                        log.error("CLIENT ID is not valid for security validation");
                        return true; //just for testing
                        //return false; //uncomment after testing
                    }
                    log.info("Validation OK.");
                    return true;
                }
            }
        } else {
            log.error("Validation failed. Bearer not found");
            return false;
        }
    }


    @Override
    public void afterCompletion
            (HttpServletRequest request, HttpServletResponse response, Object
                    handler, Exception exception) throws Exception {

        System.out.println("Request and Response is completed");
    }

}
