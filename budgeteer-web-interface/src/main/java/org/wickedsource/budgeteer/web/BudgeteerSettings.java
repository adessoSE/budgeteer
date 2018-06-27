package org.wickedsource.budgeteer.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("wicket")
public class BudgeteerSettings {

	private String configurationType;

	@Value("${adapter.keycloak.activated}")
	private String keycloakActivated;

	public String getConfigurationType() {
		return configurationType;
	}

	public void setConfigurationType(String configurationType) {
		this.configurationType = configurationType;
	}

	public boolean isKeycloakActivated() {
		return Boolean.valueOf(keycloakActivated);
	}
}
