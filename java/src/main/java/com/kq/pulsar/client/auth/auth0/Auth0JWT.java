package com.kq.pulsar.client.auth.auth0;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.auth0.jwt.exceptions.JWTVerificationException;

import org.apache.pulsar.shade.io.netty.util.internal.StringUtil;
import org.apache.pulsar.shade.org.apache.commons.lang3.Validate;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;

/**
 * Generate an auth0 JWT with basic scope validation. 
 */
public class Auth0JWT {

    String tokenServerUrl;
    String clientId;
    String clientSecret;
    String audience;

    private Auth0JWT(String domain, String clientId, String clientSecret, String audience) {
        this.tokenServerUrl = Validate.notEmpty(domain);
        this.clientId = Validate.notEmpty(clientId);
        this.clientSecret = Validate.notEmpty(clientSecret);
        this.audience = Validate.notEmpty(audience);
    }

    /**
     * Create an Auth0JWT object.
     * @param domain
     * @param clientId
     * @param clientSecret
     * @param audience
     * @return
     */
    public static Auth0JWT create(String domain, String clientId, String clientSecret, String audience) {
        return new Auth0JWT(domain, clientId, clientSecret, audience);
    }

    /**
     * 
     * @return
     * @throws UnsupportedEncodingException
     */
    public JSONObject generate() throws UnsupportedEncodingException {

        Unirest.config().enableCookieManagement(false);
        Map<String, String> bodyMap = new HashMap<String, String>();
        bodyMap.put("client_id", this.clientId);
        bodyMap.put("client_secret", this.clientSecret);
        bodyMap.put("audience", this.audience);
        bodyMap.put("grant_type", "client_credentials");


        HttpResponse<JsonNode> response = Unirest.post(this.tokenServerUrl).header("content-type", "application/json")
                .body(bodyMap)
                .asJson();

        Unirest.config().reset();
        int statusCode = response.getStatus();
        //TODO: add retry-after 503, 429, 301
        if (statusCode != 200) {
            throw new JWTVerificationException("invalide auth0.com status code " + statusCode);
        }

        JSONObject jsonObj = response.getBody().getObject();
        return jsonObj;
    }

    /**
     * Generate and returns a auth0 JWT.
     * @return
     * @throws JWTVerificationException
     */
    public String generateAndCheck() throws JWTVerificationException{
        JSONObject resp;
        try {
            resp = generate();
        } catch (UnsupportedEncodingException e) {
            throw new JWTVerificationException(e.getMessage());
        }
        String token = resp.getString("access_token");
        String scope = resp.getString("scope");
        if (scope.split(" ").length > 1) {
            throw new JWTVerificationException("only one scope is allowed");
        }
        if (scope.split(" ").length == 0) {
            throw new JWTVerificationException("one scope is required");
        }

        if (StringUtil.isNullOrEmpty(token)) {
            throw new JWTVerificationException("auth0 JWT is empty");
        }
        return token;
    }

}