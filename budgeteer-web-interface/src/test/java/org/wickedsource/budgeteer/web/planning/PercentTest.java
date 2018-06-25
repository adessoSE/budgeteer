package org.wickedsource.budgeteer.web.planning;


import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PercentTest {

	@Test
	void testOf() throws Exception {
		Percent percent = new Percent(32);
		Assertions.assertEquals(BigDecimal.valueOf(32), percent.of(BigDecimal.valueOf(100)));
	}
}