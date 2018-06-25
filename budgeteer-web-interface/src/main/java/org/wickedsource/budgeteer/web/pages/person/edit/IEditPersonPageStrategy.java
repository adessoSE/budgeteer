package org.wickedsource.budgeteer.web.pages.person.edit;

import java.io.Serializable;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.wickedsource.budgeteer.web.pages.person.edit.personrateform.EditPersonForm;

/**
* Strategy for the EditPersonPage, which can either be used to UPDATE an existing person or to CREATE a new person.
*/
public interface IEditPersonPageStrategy extends Serializable {

	Label createPageTitleLabel(String id);

	Label createSubmitButtonLabel(String id);

	Panel createNotificationList(String id, long personId);

	EditPersonForm createForm(String id, long personId);

}