package ch.svermeille.rika.audit.xml;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * @author Sebastien Vermeille
 */
public class LocalDateTimeAdapter extends XmlAdapter<String, LocalDateTime> {
  private final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  @Override
  public String marshal(final LocalDateTime dateTime) {
    return dateTime.format(this.dateFormat);
  }

  @Override
  public LocalDateTime unmarshal(final String dateTime) {
    return LocalDateTime.parse(dateTime, this.dateFormat);
  }
}
