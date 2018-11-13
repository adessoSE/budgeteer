package org.wickedsource.budgeteer.web.pages.user.resetpassword;

import java.io.Serializable;

public class ResetPasswordData implements Serializable {

    private String newPassword;
    private String newPasswordConfirmation;

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPasswordConfirmation() {
        return newPasswordConfirmation;
    }

    public void setNewPasswordConfirmation(String newPasswordConfirmation) {
        this.newPasswordConfirmation = newPasswordConfirmation;
    }
}
