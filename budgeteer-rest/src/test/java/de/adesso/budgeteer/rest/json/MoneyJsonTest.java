package de.adesso.budgeteer.rest.json;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@ContextConfiguration(classes = {MoneyJsonSerializer.class, MoneyJsonDeserializer.class})
class MoneyJsonTest {

    @Autowired
    private JacksonTester<Money> json;

    @Test
    void testMoneySerialization() throws IOException {
        var money = Money.of(CurrencyUnit.EUR, new BigDecimal("123.45"));
        assertThat(json.write(money)).isEqualToJson("{\"currencyCode\":\"EUR\",\"amount\":\"123.45\"}");
    }

    @Test
    void testMoneyDeserialization() throws IOException {
        var content = "{\"currencyCode\":\"EUR\",\"amount\":\"123.45\"}";
        var expected = Money.of(CurrencyUnit.EUR, new BigDecimal("123.45"));
        assertThat(json.parse(content).getObject()).isEqualTo(expected);
    }
}