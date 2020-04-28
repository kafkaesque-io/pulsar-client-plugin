package io.kafkaesque.pulsar.client.auth;

import com.google.common.base.Charsets;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.function.Supplier;

import org.apache.pulsar.client.api.AuthenticationDataProvider;
import org.apache.pulsar.client.api.PulsarClientException;

/**
 * Cognito JWT based authentication provider.
 */
public class AuthenticationCognito extends AuthenticationAuth0 {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String authMethod = AuthMethod.COGNITO;

    public AuthenticationCognito(String token) {
        super(() -> token);
    }

    public AuthenticationCognito(Supplier<String> tokenSupplier) {
        super(tokenSupplier);
    }

    @Override
    public String getAuthMethodName() {
        return authMethod;
    }

    @Override
    public AuthenticationDataProvider getAuthData() throws PulsarClientException {
        return new AuthenticationDataCognito(tokenSupplier);
    }

}
