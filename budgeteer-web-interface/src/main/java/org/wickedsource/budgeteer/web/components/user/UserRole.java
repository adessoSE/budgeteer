package org.wickedsource.budgeteer.web.components.user;

public enum UserRole {
    USER("user"),
    ADMIN("admin");

    private final String type;

    UserRole(String s) {
        type = s;
    }

    @Override
    public String toString() {
        return this.type;
    }

    public static UserRole getEnum(String value){
        if(value.equals("user")){
            return UserRole.USER;
        }else {
            return UserRole.ADMIN;
        }
    }
}
