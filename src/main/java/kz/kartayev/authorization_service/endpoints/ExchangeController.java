package kz.kartayev.authorization_service.endpoints;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.swagger.annotations.ApiModel;
import kz.kartayev.authorization_service.entity.ExchangeRates;
import kz.kartayev.authorization_service.util.ExchangeRateItemDeserializer;
import kz.kartayev.authorization_service.util.XmlConvertter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/open-api")
@ApiModel(description = "Неактуально")
public class ExchangeController {
  @Value("exchange")
  private String url;

  @GetMapping
  public ExchangeRates getAll() {
    LocalDate localDate = LocalDate.now();
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    String currentDate = localDate.format(dateTimeFormatter);
    StringBuilder str = new StringBuilder("https://nationalbank.kz/rss/get_rates.cfm?fdate=").append(currentDate);

    RestTemplate restTemplate = new RestTemplate();

    List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
    messageConverters.add(new StringHttpMessageConverter());
    messageConverters.add(new MappingJackson2XmlHttpMessageConverter());

    ObjectMapper objectMapper = new XmlMapper();
    SimpleModule module = new SimpleModule();
    module.addDeserializer(ExchangeRates.ExchangeRateItem.class, new ExchangeRateItemDeserializer());
    objectMapper.registerModule(module);

    MappingJackson2XmlHttpMessageConverter converter = new MappingJackson2XmlHttpMessageConverter(objectMapper);
    messageConverters.add(converter);

    restTemplate.setMessageConverters(messageConverters);

    ResponseEntity<String> response = restTemplate.getForEntity(str.toString(), String.class);
    String xmlResponse = response.getBody();

    try {
      ExchangeRates exchangeRates = XmlConvertter.parseXml(xmlResponse, ExchangeRates.class);
      return exchangeRates;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

}
