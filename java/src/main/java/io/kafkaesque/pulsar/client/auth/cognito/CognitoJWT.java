package io.kafkaesque.pulsar.client.auth.cognito;

import java.io.UnsupportedEncodingException;

import com.auth0.jwt.exceptions.JWTVerificationException;

import org.apache.pulsar.shade.io.netty.util.internal.StringUtil;
import org.apache.pulsar.shade.org.apache.commons.lang3.Validate;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;

/**
 * Generate a cognito JWT with basic scope validation. 
 */
public class CognitoJWT {

    String tokenServerUrl;
    String clientId;
    String clientSecret;
    String scope;

    private CognitoJWT(String domain, String clientId, String clientSecret, String scope) {
        this.tokenServerUrl = Validate.notEmpty(domain);
        if (!domain.endsWith("/oauth2/token")) {
            this.tokenServerUrl = domain + "/oauth2/token";
        }
        this.clientId = Validate.notEmpty(clientId);
        this.clientSecret = Validate.notEmpty(clientSecret);
        this.scope = Validate.notEmpty(scope);
    }

    /**
     * Create a CognitoJWT object.
     * @param domain
     * @param clientId
     * @param clientSecret
     * @param scope
     * @return
     */
    public static CognitoJWT create(String domain, String clientId, String clientSecret, String scope) {
        return new CognitoJWT(domain, clientId, clientSecret, scope);
    }

    /**
     * 
     * @return
     * @throws UnsupportedEncodingException
     */
    public JSONObject generate() throws UnsupportedEncodingException {

        Unirest.config().enableCookieManagement(false);
        String reqBody = "grant_type=client_credentials&scope=" + this.scope;

        HttpResponse<JsonNode> response = Unirest.post(this.tokenServerUrl)
                .header("content-type", "application/x-www-form-urlencoded")
                .basicAuth(this.clientId, this.clientSecret)
                .body(reqBody).asJson();

        Unirest.config().reset();
        int statusCode = response.getStatus();
        JSONObject jsonObj = response.getBody().getObject();
        //TODO: may retry with some 400 or 500 code
        if (statusCode != 200) {
            throw new JWTVerificationException("invalid aws cognito status code " + statusCode);
        }

        return jsonObj;
    }

    /**
     * Generate and returns a Cognito JWT.
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
        if (StringUtil.isNullOrEmpty(token)) {
            throw new JWTVerificationException("Cognito JWT is empty");
        }
        return token;
    }

}