package org.wickedsource.budgeteer.SheetTemplate;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Stream;

public interface SheetTemplateSerializable {
	public String getName();
	public Object getValue();

	static String getAttribute(String attribute, Collection<? extends SheetTemplateSerializable> collection) {
		Stream<? extends SheetTemplateSerializable> stream = collection == null ? Stream.empty() : collection.stream();

		return stream
				.filter(entry -> entry.getName().equals(attribute))
				.map(SheetTemplateSerializable::getValue)
				.filter(Objects::nonNull)
				.map(Object::toString)
				.findFirst()
				.orElse("");
	}
}
