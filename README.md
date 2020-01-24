# pulsar-client-plugin
Pulsar client plugin for auth0, aws, and etc.

## auth0 integration
Integration of auth0 enables Pulsar client authenticated against [auth0](https://www.auth0.com) backend instead of the default Pulsar token. The authentication follows [the recommaned M2M flow](https://auth0.com/blog/using-m2m-authorization/). 

Auth0 integration consists of the client side plugin and a broker auth plugin. The client plugin generates an auth0 jwt, which in turn can be autheticated and authorized by the broker side. The broker plugin has to be configured on Pulsar, not part of this repo. Please contact [Kafkaesque](https://kafkaesque.io/contact/#) to enable the broker side authorization.

The Jar artefact is loaded on GitHub package registry.

In .m2/settings.xml,
``` .m2/settings.xml
<repositories>
  <repository>
    <id>github</id>
    <url>https://maven.pkg.github.com/kafkaesque-io/pulsar-client-plugin</url>
  </repository>
</repositories>

<servers>
  <server>
    <id>github</id>
    <username>GITHUB_USERNAME</username>
    <password>GITHUB_TOKEN</password>
  </server>
</servers>
```

In pom.xml,
```pom.xml
<dependency>
  <groupId>io.kafkaesque.pulsar</groupId>
  <artifactId>pulsar-client-plugin</artifactId>
  <version>0.0.6</version>
</dependency>

```

Java Client example:
``` example.java
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
