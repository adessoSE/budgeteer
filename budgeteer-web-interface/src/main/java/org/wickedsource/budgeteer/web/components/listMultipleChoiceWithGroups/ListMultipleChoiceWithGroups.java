package org.wickedsource.budgeteer.web.components.listMultipleChoiceWithGroups;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.util.string.AppendingStringBuffer;

public class ListMultipleChoiceWithGroups<T> extends ListMultipleChoice<T> {

	private final IModel<Collection<OptionGroup<T>>> optionGroups;

	public ListMultipleChoiceWithGroups(
			String id,
			ListModel<T> chosenOptions,
			List<OptionGroup<T>> possibleOptions,
			IChoiceRenderer<? super T> choiceRenderer) {
		super(id, chosenOptions, new LinkedList<T>(), choiceRenderer);
		optionGroups = Model.of(possibleOptions);
		List<T> choiceList = new LinkedList<>();
		for (OptionGroup<T> group : optionGroups.getObject()) {
			choiceList.addAll(group.getOptions());
		}
		setChoices(choiceList);
	}

	@Override
	public void onComponentTagBody(final MarkupStream markupStream, final ComponentTag openTag) {
		Collection<? extends OptionGroup<T>> groups = optionGroups.getObject();

		final AppendingStringBuffer buffer =
				new AppendingStringBuffer((groups.size() * 50) + (getChoices().size() * 50) + 16);

		for (OptionGroup<T> group : groups) {
			buffer.append("<optgroup label='");
			buffer.append(getString(group.getGroupNameResourceKey()));
			buffer.append("'>");

			List<? extends T> choices = group.getOptions();
			final String selectedValue = getValue();

			buffer.append(getDefaultChoice(selectedValue));

			for (int index = 0; index < choices.size(); index++) {
				final T choice = choices.get(index);
				appendOptionHtml(buffer, choice, index, selectedValue);
			}
			buffer.append("</optgroup>");
		}

		buffer.append('\n');
		replaceComponentTagBody(markupStream, openTag, buffer);
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		optionGroups.detach();
	}
}
