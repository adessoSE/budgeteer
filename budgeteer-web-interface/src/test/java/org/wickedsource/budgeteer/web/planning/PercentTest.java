package org.wickedsource.budgeteer.web.planning;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class PercentTest {

    @Test
    void testOf() throws Exception {
        Percent percent = new Percent(32);
        Assertions.assertEquals(BigDecimal.valueOf(32), percent.of(BigDecimal.valueOf(100)));
    }
}