package org.wickedsource.budgeteer.service;

import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Simple provider for the current date so that the date can be mocked in tests.
 */
@Component
public class DateProvider {

    public Date currentDate() {
        return new Date();
    }

}
