package org.wickedsource.budgeteer.web.pages.person.edit;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.pages.base.dialogpage.DialogPageWithBacklink;
import org.wickedsource.budgeteer.web.pages.person.edit.personrateform.EditPersonForm;

@Mount({"people/edit/${id}", "people/edit"})
public class EditPersonPage extends DialogPageWithBacklink {

	private IEditPersonPageStrategy strategy;

	/**
	* Use this constructor to create a page with a form to edit an existing user.
	*
	* @param parameters page parameters containing the id of the user to edit.
	*/
	public EditPersonPage(PageParameters parameters, Class<? extends WebPage> backlinkPage, PageParameters backlinkParameters) {
		super(parameters, backlinkPage, backlinkParameters);
		strategy = new UpdateStrategy(this);
		addComponents();
	}

	/**
	* Use this constructor to create a page with a form to create a new user.
	*/
	public EditPersonPage(Class<? extends WebPage> backlinkPage, PageParameters backlinkParameters) {
		super(backlinkPage, backlinkParameters);
		strategy = new CreateStrategy(this);
		addComponents();
	}

	private void addComponents() {
		EditPersonForm form = strategy.createForm("form", getPersonId());
		add(form);
		add(strategy.createPageTitleLabel("pageTitle"));
		form.add(strategy.createNotificationList("notificationList", getPersonId()));
		add(createBacklink("backlink1"));
		form.add(createBacklink("backlink2"));
	}

	/**
	* Creates a valid PageParameters object to pass into the constructor of this page class.
	*
	* @param personId id of the person whose details to display.
	* @return a valid PageParameters object.
	*/
	public static PageParameters createParameters(long personId) {
		PageParameters parameters = new PageParameters();
		parameters.add("id", personId);
		return parameters;
	}

	private long getPersonId() {
		StringValue value = getPageParameters().get("id");
		if (value == null || value.isEmpty() || value.isNull()) {
			return 0l;
		} else {
			return value.toLong();
		}
	}
}
