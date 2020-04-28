package io.kafkaesque.pulsar.client.auth;

import java.util.Arrays;
import java.util.List;

public final class AuthMethod {

    public final static String TOKEN = "token";

    public final static String AUTH0 = "auth0";

    public final static String COGNITO = "cognito";
    
    public final static List<String> supportedMethods = Arrays.asList(TOKEN, AUTH0, COGNITO);
}