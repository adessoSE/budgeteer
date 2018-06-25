package org.wickedsource.budgeteer.web.pages.base.basepage.notifications;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.wickedsource.budgeteer.service.notification.Notification;
import org.wickedsource.budgeteer.web.components.notificationlist.NotificationLinkFactory;
import org.wickedsource.budgeteer.web.components.notificationlist.NotificationMessageFactory;

public class NotificationDropdown extends Panel {

	private NotificationLinkFactory notificationLinkFactory = new NotificationLinkFactory();

	private NotificationMessageFactory notificationMessageFactory = new NotificationMessageFactory();

	public NotificationDropdown(String id, NotificationModel model) {
		super(id, model);
		add(createNotificationCountLabel("notificationCountLabel"));
		add(createDropDownHeader("dropdownHeader"));
		add(createDropdownMenu("dropdownMenu"));
	}

	@SuppressWarnings("unchecked")
	private NotificationModel getModel() {
		return (NotificationModel) getDefaultModel();
	}

	private Label createNotificationCountLabel(String wicketId) {
		Label label = new Label(wicketId, getModel().getNotificationCountModel()) {
			@Override
			public boolean isVisible() {
				return 0 != (Integer) getDefaultModelObject();
			}
		};
		return label;
	}

	private Label createDropDownHeader(String wicketId) {
		return new Label(wicketId, getModel().getHeaderModel());
	}

	private WebMarkupContainer createDropdownMenu(String wicketId) {
		final ListView<Notification> listview = new ListView<Notification>("notificationList", getModel()) {
			@Override
			protected void populateItem(final ListItem<Notification> item) {
				Link link = new Link("notificationLink") {
					@SuppressWarnings("unchecked")
					@Override
					public void onClick() {
						setResponsePage(notificationLinkFactory.getLinkForNotification(item.getModelObject(), (Class<? extends WebPage>) getPage().getClass(), getPage().getPageParameters()));
					}
				};
				item.add(link);

				Label messageLabel = new Label("notificationMessage", notificationMessageFactory.getMessageForNotification(item.getModelObject()));
				link.add(messageLabel);
			}
		};

		WebMarkupContainer menu = new WebMarkupContainer(wicketId) {
			@Override
			public boolean isVisible() {
				return 0 != listview.getModelObject().size();
			}
		};
		menu.add(listview);

		return menu;
	}

}
