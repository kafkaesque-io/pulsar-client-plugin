# pulsar-client-plugin
Pulsar client plugin for auth0 and AWS Cognito authentication.

The Jar artifact is loaded on GitHub package registry.

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
  <version>0.0.7</version>
</dependency>

```

## auth0 integration
Integration of auth0 enables Pulsar client authenticated against [auth0](https://www.auth0.com) backend instead of the default Pulsar token. The authentication follows [the recommended M2M flow](https://auth0.com/blog/using-m2m-authorization/). 

Auth0 integration consists of the client side plugin and a broker auth plugin. The client plugin generates an auth0 JWT, which in turn can be authenticated and authorized by the broker side. The broker plugin has to be configured on Pulsar and is not part of this repo. Please contact [Kafkaesque](https://kafkaesque.io/contact/#) to enable the broker side plugin.

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

### AWS Cognito integration
Integration of AWS Cognito enables Pulsar client authenticated against [AWS Cognito](https://aws.amazon.com/cognito/). The authentication flow requires creation of Cognito user pool and App client. The App client must allow `Client credential` OAuth flow, and specify custome scopes for OAuth 2.0 grants. Here is [a good example](https://lobster1234.github.io/2018/05/31/server-to-server-auth-with-amazon-cognito/) explaining machine to machine authentication with Cognito.

The client plugin enables client credential to exchange an access token following [the Cognito deverloper's guide](https://docs.aws.amazon.com/cognito/latest/developerguide/token-endpoint.html). Under the hood, we will use `client_credentials` as grant_type. Scope must be preconfigured under the a User Pool's resource server and enabled by checking off `App client`'s OAuth2 Allowed Custom Scopes. This can be done via AWS CLI or console. The scope name will be used for authorization.

Resource server's identifier and client Id, that becomes `sub` in the Cognito JWT, can be used for whitelist verification on the Pulsar broker side's authentication.

Cognito integration consists of the client side plugin and a broker auth plugin. The client plugin generates an access token, which in turn can be authenticated and authorized by the broker side. The broker plugin has to be configured on Pulsar and is not part of this repo. Please contact [Kafkaesque](https://kafkaesque.io/contact/#) to enable the broker side plugin.

Java Client example:
``` example.java
String domain = "https://<your domain>.auth.us-east-2.amazoncognito.com/oauth2/token";
String clientId = "";
String clientSecret = "";
String scope = "kafkaesque.io/ming.pulsar";

// Create client object
PulsarClient client = PulsarClient.builder()
                .serviceUrl(SERVICE_URL)
                .authentication(
                    AuthFactory.cognito(domain, clientId, clientSecret, scope)
                )
                .build();
```
