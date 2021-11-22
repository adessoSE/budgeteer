package de.adesso.budgeteer.rest.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.TextNode;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.math.BigDecimal;

@JsonComponent
public class MoneyJsonDeserializer extends JsonDeserializer<Money> {
    @Override
    public Money deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        var node = parser.getCodec().readTree(parser);
        var currencyCode = ((TextNode) node.get("currencyCode")).textValue();
        var amount = ((TextNode) node.get("amount")).textValue();
        return Money.of(CurrencyUnit.of(currencyCode), new BigDecimal(amount));
    }
}
