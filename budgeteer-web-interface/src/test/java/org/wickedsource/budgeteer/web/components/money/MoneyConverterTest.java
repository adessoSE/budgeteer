package org.wickedsource.budgeteer.web.components.money;

import org.joda.money.Money;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Locale;

public class MoneyConverterTest {

    @Test
    public void testGermanLocale() {
        MoneyConverter converter = new MoneyConverter();

        Money money = converter.convertToObject("123.456,78", Locale.GERMAN);
        Assertions.assertEquals(12345678, money.getAmountMinorInt());

        String string = converter.convertToString(money, Locale.GERMAN);
        Assertions.assertEquals("123.456,78", string);
    }

    @Test
    public void testUSLocale() {
        MoneyConverter converter = new MoneyConverter();

        Money money = converter.convertToObject("123,456.78", Locale.US);
        Assertions.assertEquals(12345678, money.getAmountMinorInt());

        String string = converter.convertToString(money, Locale.US);
        Assertions.assertEquals("123,456.78", string);
    }
}
