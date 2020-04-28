package io.kafkaesque.pulsar.client.auth;

import java.util.Map;

import java.util.Set;
import java.util.function.Supplier;

import javax.naming.AuthenticationException;

import org.apache.pulsar.common.api.AuthData;

import org.apache.pulsar.client.api.AuthenticationDataProvider;

import static java.nio.charset.StandardCharsets.UTF_8;
/**
 * This plugin is for AWS Cognito JWT Pulsar authentication data.
 */
public class AuthenticationDataCognito implements AuthenticationDataProvider {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private final Supplier<String> token;

    public AuthenticationDataCognito(Supplier<String> token) {
        this.token = token;
	}

    /*
     * HTTP
     */

    /**
     * Check if data for HTTP are available.
     *
     * @return true if this authentication data contain data for HTTP
     */
    public boolean hasDataForHttp() {
        return true;
    }

    /**
     *
     * @return a authentication scheme, or {@code null} if the request will not be authenticated.
     */
    public String getHttpAuthType() {
        return null;
    }

    /**
     *
     * @return an enumeration of all the header names
     */
    public Set<Map.Entry<String, String>> getHttpHeaders() throws Exception {
        return null;
    }

    /*
     * Command
     */

    /**
     * Check if data from Pulsar protocol are available.
     *
     * @return true if this authentication data contain data from Pulsar protocol
     */
    public boolean hasDataFromCommand() {
        return token.get() != null;
    }

    /**
     *
     * @return authentication data which will be stored in a command
     */
    public String getCommandData() {
        return token.get();
    }

    /**
     * For mutual authentication, This method use passed in `data` to evaluate and challenge,
     * then returns null if authentication has completed;
     * returns authenticated data back to server side, if authentication has not completed.
     *
     * <p>Mainly used for mutual authentication like sasl.
     */
    public AuthData authenticate(AuthData data) throws AuthenticationException {
        byte[] bytes = (hasDataFromCommand() ? this.getCommandData() : "").getBytes(UTF_8);
        return AuthData.of(bytes);
    }
    
}