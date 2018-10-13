package org.wickedsource.budgeteer.web.pages.user.resettoken;

import java.io.Serializable;

public class ResetTokenData implements Serializable {

    private String mail;

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
