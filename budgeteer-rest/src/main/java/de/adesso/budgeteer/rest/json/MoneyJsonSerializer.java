package de.adesso.budgeteer.rest.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.joda.money.Money;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class MoneyJsonSerializer extends JsonSerializer<Money> {
    @Override
    public void serialize(Money value, JsonGenerator generator, SerializerProvider serializers) throws IOException {
        generator.writeStartObject();
        generator.writeStringField("currencyCode", value.getCurrencyUnit().getCode());
        generator.writeStringField("amount", value.getAmount().toPlainString());
        generator.writeEndObject();
    }
}
