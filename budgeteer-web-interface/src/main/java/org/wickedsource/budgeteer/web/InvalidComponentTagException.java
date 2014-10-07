package org.wickedsource.budgeteer.web;

import org.apache.wicket.WicketRuntimeException;

/**
 * This exception is thrown, when a Wicket component was attached to an invalid HTML tag.
 */
public class InvalidComponentTagException extends WicketRuntimeException {

    public InvalidComponentTagException(String expectedComponentTag) {
        super(String.format("This component must be attached to a %s element!", expectedComponentTag));
    }
}
