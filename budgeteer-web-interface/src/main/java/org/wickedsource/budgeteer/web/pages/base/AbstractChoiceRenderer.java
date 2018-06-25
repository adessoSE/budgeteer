package org.wickedsource.budgeteer.web.pages.base;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.slf4j.Logger;

public abstract class AbstractChoiceRenderer<T> implements IChoiceRenderer<T> {

	private static final Logger log = getLogger(AbstractChoiceRenderer.class);

	@Override
	public String getIdValue(T object, int index) {
		return String.valueOf(index);
	}

	@Override
	public T getObject(String id, IModel<? extends List<? extends T>> choiceModel) {
		if (isEmpty(id)) {
			return null;
		}
		try {
			int index = Integer.parseUnsignedInt(id);
			return choiceModel.getObject().get(index);
		} catch (NumberFormatException | IndexOutOfBoundsException e) {
			log.warn("Ignored attempt to inject invalid object '" + id + "' via ImporterChoiceRenderer");
			return null;
		}
	}
}
