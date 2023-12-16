package kz.kartayev.authorization_service.util;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class XmlConvertter {
  public static <T> T parseXml(String xml, Class<T> responseType) throws Exception {
    XmlMapper xmlMapper = new XmlMapper();
    return xmlMapper.readValue(xml, responseType);
  }
}

