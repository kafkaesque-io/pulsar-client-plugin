# pulsar-client-plugin
Pulsar client plugin for auth0, aws, and etc.

# auth0

Auth0 integration consists of the client side plugin and a broker auth plugin. The client plugin generates an auth0 jwt.

``` java
String domain = "https://<your auth0 domain>.auth0.com/oauth/token";
String clientId = "";
String clientSecret = "";
String audience = "https://useast2.aws.kafkaesque.io";

// Create client object
PulsarClient client = PulsarClient.builder()
                .serviceUrl(SERVICE_URL)
                .authentication(
                    AuthFactory.auth0(domain, clientId, clientSecret, audience)
                )
                .build();
```
