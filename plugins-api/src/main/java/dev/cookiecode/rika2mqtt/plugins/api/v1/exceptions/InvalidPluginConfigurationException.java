package dev.cookiecode.rika2mqtt.plugins.api.v1.exceptions;

public class InvalidPluginConfigurationException extends PluginException {
  public InvalidPluginConfigurationException(String message) {
    super(message);
  }

  public InvalidPluginConfigurationException(String message, Throwable cause) {
    super(message, cause);
  }
}
