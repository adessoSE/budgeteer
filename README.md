[![Build Status](https://circleci.com/gh/adessoAG/budgeteer.svg?style=shield&circle-token=:circle-token)](https://circleci.com/gh/adessoAG/budgeteer) [![Gradle Status](https://gradleupdate.appspot.com/adessoAG/budgeteer/status.svg)](https://gradleupdate.appspot.com/adessoAG/budgeteer/status)

## What is Budgeteer?
Budgeteer is a budget monitoring tool that allows you to track
project budgets. A project in the sense of Budgeteer contains a set of
budgets each defined in a currency. For each person that contributes to the
project, you set up a daily rate in that currency. Budgeteer allows you to
import working hours and does the math for you, so that you always know where
your budgets are heading.

## Screenshots
### Start Page
![Start Page](https://raw.githubusercontent.com/adessoAG/budgeteer/master/screenshots/start-page.png)

### Budget Overview
![Budget Overview](https://raw.githubusercontent.com/adessoAG/budgeteer/master/screenshots/budget-overview.png)

### Month Report
![Month Report](https://raw.githubusercontent.com/adessoAG/budgeteer/master/screenshots/month-report.png)

## SSO using Keycloak

Instead of using the default budgeteer authentication you can activate a preconfigured [Keycloak](http://www.keycloak.org/) for authentication and authorisation. To activate the Keycloak adapter in budgeteer you need to set the following configurations:

```properties
// set this line to true
adapter.keycloak.activated = true 

// uncomment this line
spring.autoconfigure.exclude=org.keycloak.adapters.springboot.KeycloakSpringBootConfiguration 
```

 You can find more details about how to install and configure Keycloak [here](https://keycloak.gitbooks.io/documentation/securing_apps/topics/oidc/java/spring-boot-adapter.html).
