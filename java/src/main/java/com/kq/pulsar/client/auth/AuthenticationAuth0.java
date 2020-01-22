package com.kq.pulsar.client.auth;

import com.google.common.base.Charsets;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.function.Supplier;

import org.apache.pulsar.client.api.Authentication;
import org.apache.pulsar.client.api.AuthenticationDataProvider;
import org.apache.pulsar.client.api.EncodedAuthenticationParameterSupport;
import org.apache.pulsar.client.api.PulsarClientException;

/**
 * JWT based authentication provider.
 */
public class AuthenticationAuth0 implements Authentication, EncodedAuthenticationParameterSupport {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Supplier<String> tokenSupplier;

    public AuthenticationAuth0(String token) {
        this(() -> token);
    }

    public AuthenticationAuth0(Supplier<String> tokenSupplier) {
        this.tokenSupplier = tokenSupplier;
    }

    @Override
    public void close() throws IOException {
        // noop
    }

    @Override
    public String getAuthMethodName() {
        return "auth0";
    }

    @Override
    public AuthenticationDataProvider getAuthData() throws PulsarClientException {
        return new AuthenticationDataAuth0(tokenSupplier);
    }

    @Override
    public void configure(String encodedAuthParamString) {
        // Interpret the whole param string as the token. If the string contains the notation `token:xxxxx` then strip
        // the prefix
        if (encodedAuthParamString.startsWith("token:")) {
            this.tokenSupplier = () -> encodedAuthParamString.substring("token:".length());
        } else if (encodedAuthParamString.startsWith("file:")) {
            // Read token from a file
            URI filePath = URI.create(encodedAuthParamString);
            this.tokenSupplier = () -> {
                try {
                    return new String(Files.readAllBytes(Paths.get(filePath)), Charsets.UTF_8).trim();
                } catch (IOException e) {
                    throw new RuntimeException("Failed to read token from file", e);
                }
            };
        } else {
            this.tokenSupplier = () -> encodedAuthParamString;
        }
    }

    @Override
    public void configure(Map<String, String> authParams) {
        // noop
    }

    @Override
    public void start() throws PulsarClientException {
        // noop
    }

}
