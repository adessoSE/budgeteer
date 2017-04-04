[![Build Status](https://circleci.com/gh/thombergs/budgeteer.svg?style=shield&circle-token=:circle-token)](https://circleci.com/gh/thombergs/budgeteer) [![Gradle Status](https://gradleupdate.appspot.com/thombergs/budgeteer/status.svg)](https://gradleupdate.appspot.com/thombergs/budgeteer/status)

Simple Budget monitoring via web.

### Keycloak Adapter

Instead of using the default budgeteer authentication you can activate a preconfigured [Keycloak](http://www.keycloak.org/) for authentication and authorisation. To activate the Keycloak adapter in budgeteer you need to set the following configurations:

```properties
// set this line to true
adapter.keycloak.activated = true 

// uncomment this line
spring.autoconfigure.exclude=org.keycloak.adapters.springboot.KeycloakSpringBootConfiguration 
```

 You can find more details about how to install and configure Keycloak [here](https://keycloak.gitbooks.io/documentation/securing_apps/topics/oidc/java/spring-boot-adapter.html).