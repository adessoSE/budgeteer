package org.wickedsource.budgeteer.web.components.fontawesome;

/**
 * Enumeration of FontAwesome icons and their CSS classes.
 */
public enum FontAwesomeIconType {

    CHECK_SQUARE_O("fa fa-check-square-o"),

    SQUARE_O("fa fa-square-o");

    private String cssClass;

    private FontAwesomeIconType(String cssClass) {
        this.cssClass = cssClass;
    }

    public String getCssClass() {
        return cssClass;
    }
}
