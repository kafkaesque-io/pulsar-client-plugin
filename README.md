# pulsar-client-plugin
Pulsar client plugin for auth0, aws, and etc.

## auth0 integration
Integration of auth0 enables Pulsar client authenticated against [auth0](https://www.auth0.com) backend instead of the default Pulsar token. The authentication follows [the recommaned M2M flow](https://auth0.com/blog/using-m2m-authorization/). 

Auth0 integration consists of the client side plugin and a broker auth plugin. The client plugin generates an auth0 jwt, which in turn can be autheticated and authorized by the broker side. The broker plugin has to be configured on Pulsar, not part of this repo. Please contact [Kafkaesque](https://kafkaesque.io/contact/#) to enable the broker side authorization.

```maven

```

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
