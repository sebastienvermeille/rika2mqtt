package ch.svermeille.rika.health.storage;

import static ch.svermeille.rika.health.storage.LocalDateTimeSerializer.FORMATTER;
import static java.time.LocalDateTime.parse;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;

/**
 * @author Sebastien Vermeille
 */
public class LocalDateTimeDeserializer implements JsonDeserializer<LocalDateTime> {
  @Override
  public LocalDateTime deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
      throws JsonParseException {
    return parse(json.getAsString(), FORMATTER);
  }
}
