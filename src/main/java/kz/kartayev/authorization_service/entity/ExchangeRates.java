package kz.kartayev.authorization_service.entity;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JacksonXmlRootElement(localName = "rates")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRates {

  @JacksonXmlProperty(localName = "generator")
  private String generator;

  @JacksonXmlProperty(localName = "title")
  private String title;

  @JacksonXmlProperty(localName = "link")
  private String link;

  @JacksonXmlProperty(localName = "description")
  private String description;

  @JacksonXmlProperty(localName = "copyright")
  private String copyright;

  @JacksonXmlProperty(localName = "date")
  private String date;

  @JacksonXmlProperty(localName = "item")
  private List<ExchangeRateItem> items;


  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class ExchangeRateItem {

    @JacksonXmlProperty(localName = "fullname")
    private String fullname;

    @JacksonXmlProperty(localName = "title")
    private String title;

    @JacksonXmlProperty(localName = "description")
    private String description;

    @JacksonXmlProperty(localName = "quant")
    private String quant;

    @JacksonXmlProperty(localName = "index")
    private String index;

    @JacksonXmlProperty(localName = "change")
    private String change;
  }

}
