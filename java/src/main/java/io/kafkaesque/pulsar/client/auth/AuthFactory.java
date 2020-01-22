package io.kafkaesque.pulsar.client.auth;

import io.kafkaesque.pulsar.client.auth.auth0.Auth0JWT;

import org.apache.pulsar.client.api.Authentication;

public final class AuthFactory {

    private AuthFactory() {}

    /**
     * Request JWT from autho and pass the JWT to pulsar broker.
     * @param domain
     * @param clientId
     * @param clientSecret
     * @param audience
     * @return
     */
    public static Authentication auth0(String domain, String clientId, String clientSecret, String audience) {
        return new AuthenticationAuth0(Auth0JWT.create(domain, clientId, clientSecret, audience).generateAndCheck());

    }
}