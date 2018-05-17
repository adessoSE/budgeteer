package org.wickedsource.budgeteer.web.pages.templates.edit;

import org.apache.wicket.markup.html.link.Link;
import org.wickedsource.budgeteer.web.pages.base.dialogpage.DialogPage;

import java.util.concurrent.Callable;

public class DeleteDialog extends DialogPage{

    transient private Callable<Void> successMethod;
    transient private Callable<Void> failMethod;

    public DeleteDialog(Callable<Void> successMethod, Callable<Void> failMethod){
        add(createNoButton());
        add(createYesButton());
        this.successMethod = successMethod;
        this.failMethod = failMethod;
    }

    private Link createYesButton(){
        return new Link<Void>("yesButton") {
            @Override
            public void onClick() {
                try {
                    successMethod.call();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
    }

    private Link createNoButton(){
        return new Link<Void>("noButton") {
            @Override
            public void onClick() {
                try {
                    failMethod.call();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
    }
}
