package org.wickedsource.budgeteer.web.planning;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

public class PercentTest {

    @Test
    public void testOf() throws Exception {
        Percent percent = new Percent(32);
        Assert.assertEquals(BigDecimal.valueOf(32), percent.of(BigDecimal.valueOf(100)));
    }
}