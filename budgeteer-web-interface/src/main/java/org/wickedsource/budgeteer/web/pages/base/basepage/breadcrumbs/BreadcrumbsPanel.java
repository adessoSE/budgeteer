package org.wickedsource.budgeteer.web.pages.base.basepage.breadcrumbs;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class BreadcrumbsPanel extends Panel {

	public BreadcrumbsPanel(String wicketId, IModel<List<Breadcrumb>> model) {
		super(wicketId, model);
		setRenderBodyOnly(true);
		add(createBreadcrumbsList(model));
	}

	private ListView<Breadcrumb> createBreadcrumbsList(final IModel<List<Breadcrumb>> model) {
		return new ListView<Breadcrumb>("breadcrumbsList", model) {
			@Override
			protected void populateItem(ListItem<Breadcrumb> item) {
				Link link;
				if (item.getModelObject().getParameters() != null) {
					link = new BookmarkablePageLink("breadcrumbLink", item.getModelObject().getTargetPage(), item.getModelObject().getParameters());
				} else {
					link = new BookmarkablePageLink("breadcrumbLink", item.getModelObject().getTargetPage());
				}

				Label label = new Label("breadcrumbTitle", item.getModelObject().getTitleModel());
				label.setRenderBodyOnly(true);
				link.add(label);
				item.add(link);
			}
		};
	}

}
