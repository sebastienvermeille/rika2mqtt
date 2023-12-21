package dev.cookiecode.rika2mqtt.plugins.api.v1;

import static java.util.Optional.ofNullable;

import java.util.Map;
import java.util.Optional;
import lombok.*;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class PluginConfiguration {

  private Map<String, String> parameters; // paramName, value

  public String getParameter(@NonNull String parameter) {
    return getOptionalParameter(parameter).orElseThrow();
  }

  public Optional<String> getOptionalParameter(@NonNull String parameter) {
    return ofNullable(parameters.get(parameter));
  }
}
