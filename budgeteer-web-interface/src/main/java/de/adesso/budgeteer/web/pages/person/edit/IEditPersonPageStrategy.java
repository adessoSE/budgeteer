package de.adesso.budgeteer.web.pages.person.edit;

import de.adesso.budgeteer.web.pages.person.edit.personrateform.EditPersonForm;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

import java.io.Serializable;

/**
 * Strategy for the EditPersonPage, which can either be used to UPDATE an existing person or to CREATE a new person.
 */
public interface IEditPersonPageStrategy extends Serializable {

    Label createPageTitleLabel(String id);

    Label createSubmitButtonLabel(String id);

    Panel createNotificationList(String id, long personId);

    EditPersonForm createForm(String id, long personId);

}