package dev.cookiecode.rika2mqtt.plugins.api.v1.exceptions;

public class PluginException extends Exception {

  public PluginException(String message) {
    super(message);
  }

  public PluginException(String message, Throwable cause) {
    super(message, cause);
  }
}
