package de.adesso.budgeteer.web.pages.user.forgotpassword;

import java.io.Serializable;

public class ForgotPasswordData implements Serializable {

    private String mail;

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
