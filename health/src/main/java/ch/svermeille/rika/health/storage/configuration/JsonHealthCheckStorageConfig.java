package ch.svermeille.rika.health.storage.configuration;

import ch.svermeille.rika.health.storage.LocalDateTimeDeserializer;
import ch.svermeille.rika.health.storage.LocalDateTimeSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.time.LocalDateTime;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Sebastien Vermeille
 */
@Configuration
public class JsonHealthCheckStorageConfig {

  @Bean(name = "gson")
  public Gson gson() {
    return new GsonBuilder()
        .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer())
        .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer())
        .setPrettyPrinting()
        .create();
  }

}
