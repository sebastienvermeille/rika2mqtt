package ch.svermeille.rika.health.storage;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Sebastien Vermeille
 */
public class LocalDateTimeSerializer implements JsonSerializer<LocalDateTime> {

  static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

  @Override
  public JsonElement serialize(final LocalDateTime moment, final Type srcType, final JsonSerializationContext context) {
    return new JsonPrimitive(FORMATTER.format(moment));
  }
}
