package io.kafkaesque.pulsar.client.auth;

import io.kafkaesque.pulsar.client.auth.auth0.Auth0JWT;
import io.kafkaesque.pulsar.client.auth.cognito.CognitoJWT;

import org.apache.pulsar.client.api.Authentication;

public final class AuthFactory {

    private AuthFactory() {}

    /**
     * Request JWT from auth0 and pass the JWT to pulsar broker.
     * @param domain
     * @param clientId
     * @param clientSecret
     * @param audience
     * @return
     */
    public static Authentication auth0(String domain, String clientId, String clientSecret, String audience) {
        return new AuthenticationAuth0(Auth0JWT.create(domain, clientId, clientSecret, audience).generateAndCheck());

    }

    /**
     * Request JWT from auth0 with optional authMethod.
     * @param domain
     * @param clientId
     * @param clientSecret
     * @param audience
     * @param authMethod
     * @return
     */
    public static Authentication auth0(String domain, String clientId, String clientSecret, String audience, String authMethod) {
        String token = Auth0JWT.create(domain, clientId, clientSecret, audience).generateAndCheck();

        AuthenticationAuth0 auth0 = new AuthenticationAuth0(token);
        auth0.setAuthMethodName(authMethod);
        return auth0;
    }

    /**
     * Request JWT from AWS Cognito and pass the JWT to pulsar broker.
     * @param domain
     * @param clientId
     * @param clientSecret
     * @param scope
     * @return
     */
    public static Authentication cognito(String domain, String clientId, String clientSecret, String scope) {
        return new AuthenticationCognito(CognitoJWT.create(domain, clientId, clientSecret, scope).generateAndCheck());

    }

}