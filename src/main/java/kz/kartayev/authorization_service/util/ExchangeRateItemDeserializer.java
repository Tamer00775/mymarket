package kz.kartayev.authorization_service.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import kz.kartayev.authorization_service.entity.ExchangeRates;

import java.io.IOException;

public class ExchangeRateItemDeserializer extends StdDeserializer<ExchangeRates.ExchangeRateItem> {

  public ExchangeRateItemDeserializer() {
    this(null);
  }

  public ExchangeRateItemDeserializer(Class<?> vc) {
    super(vc);
  }

  @Override
  public ExchangeRates.ExchangeRateItem deserialize(JsonParser parser, DeserializationContext deserializationContext)
          throws IOException {
    JsonNode node = parser.readValueAsTree();
    String fullname = node.get("fullname").asText();
    String title = node.get("title").asText();
    String description = node.get("description").asText();
    String quant = node.get("quant").asText();
    String index = node.get("index").asText();
    String change = node.get("change").asText();

    ExchangeRates.ExchangeRateItem item = new ExchangeRates.ExchangeRateItem();
    item.setFullname(fullname);
    item.setTitle(title);
    item.setDescription(description);
    item.setQuant(quant);
    item.setIndex(index);
    item.setChange(change);

    return item;
  }
}
