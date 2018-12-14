package de.adesso.budgeteer.service.user;

public enum TokenStatus {
    VALID(0),
    INVALID(-1),
    EXPIRED(-2);

    private final int statusCode;

    TokenStatus(int statusCode) {
        this.statusCode = statusCode;
    }

    public int statusCode() {
        return statusCode;
    }
}
