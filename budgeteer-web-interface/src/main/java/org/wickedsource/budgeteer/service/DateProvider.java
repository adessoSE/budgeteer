package org.wickedsource.budgeteer.service;

import java.util.Date;

import org.springframework.stereotype.Component;

/**
* Simple provider for the current date so that the date can be mocked in tests.
*/
@Component
public class DateProvider {

	public Date currentDate() {
		return new Date();
	}

}
