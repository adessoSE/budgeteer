package org.wickedsource.budgeteer.web.pages.base.delete;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.wickedsource.budgeteer.web.pages.base.dialogpage.DialogPage;

public abstract class DeleteDialog extends DialogPage{

    public DeleteDialog(){
        add(createNoButton());
        add(createYesButton());
        add(createConfirmationText());
    }

    private Label createConfirmationText(){
        return new Label("confirmationText", confirmationText());
    }

    private Link createYesButton(){
        return new Link<Void>("yesButton") {
            @Override
            public void onClick() {
                onYes();
            }
        };
    }

    private Link createNoButton(){
        return new Link<Void>("noButton") {
            @Override
            public void onClick() {
                onNo();
            }
        };
    }

    protected abstract void onYes();
    protected abstract void onNo();
    protected abstract String confirmationText();
}