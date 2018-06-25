package org.wickedsource.budgeteer.web.components.dataTable;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.util.lang.Args;

/**
* Wicket EventBehavior that works in JQuery-Datatables. If there are multiple EventBehaviors added to on Table,
* there may get a strange behavior because this EventBehavior works with classes instead of ids.
* Elements that can trigger the Event get the class wicketBehavior + eventName (e.g. wicketBehaviorclick)
*/
public abstract class CustomDataTableEventBehavior extends AbstractDefaultAjaxBehavior{

	private MarkupContainer table;
	private String event;
	private String customClassname;

	public CustomDataTableEventBehavior(MarkupContainer table, String event){
		this.table = table;
		table.setOutputMarkupId(true);

		Args.notEmpty(event, "event");
		event = event.toLowerCase();
		if (event.startsWith("on")){
			event = event.substring(2);
		}
		this.event = event;

		this.customClassname = "wicketBehavior"+event;
	}

	@Override
	protected void onComponentTag(ComponentTag tag) {
		super.onComponentTag(tag);
		tag.append("data-callbackurl", getCallbackUrl(), " ");
		tag.append("class", customClassname ," ");
	}

	@Override
	public void renderHead(final Component component, final IHeaderResponse response){
		super.renderHead(component, response);
		component.setOutputMarkupId(true);
		response.render(OnDomReadyHeaderItem.forScript(getScript()));
	}

	private String getScript(){
		return
		"$('#"+table.getMarkupId()+" tbody').on('"+event+"', '."+customClassname+"', " +
			"function () {\n" +
					"if($(this).attr('id') !== undefined && $(this).data('callbackurl') !== undefined){"+
						"Wicket.Ajax.ajax({\"c\":$(this).attr('id'),\"u\":$(this).data('callbackurl')});\n" +
					"}" +
			"}" +
		");";
	}

	protected final void respond(final AjaxRequestTarget target)
	{
		onEvent(target);
	}

	/**
	* Listener method for the ajax event
	*
	* @param target
	*      the current request handler
	*/
	protected abstract void onEvent(final AjaxRequestTarget target);

}
