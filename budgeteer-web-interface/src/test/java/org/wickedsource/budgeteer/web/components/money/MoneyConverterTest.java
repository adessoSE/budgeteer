package org.wickedsource.budgeteer.web.components.money;

import org.joda.money.Money;
import org.junit.Assert;
import org.junit.Test;

import java.util.Locale;

public class MoneyConverterTest {

    @Test
    public void testGermanLocale() {
        MoneyConverter converter = new MoneyConverter();

        Money money = converter.convertToObject("123.456,78", Locale.GERMAN);
        Assert.assertEquals(12345678, money.getAmountMinorInt());

        String string = converter.convertToString(money, Locale.GERMAN);
        Assert.assertEquals("123.456,78", string);
    }

    @Test
    public void testUSLocale() {
        MoneyConverter converter = new MoneyConverter();

        Money money = converter.convertToObject("123,456.78", Locale.US);
        Assert.assertEquals(12345678, money.getAmountMinorInt());

        String string = converter.convertToString(money, Locale.US);
        Assert.assertEquals("123,456.78", string);
    }
}
