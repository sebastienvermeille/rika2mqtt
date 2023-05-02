package ch.svermeille.rika.firenet.exception;

/**
 * @author Sebastien Vermeille
 */
public class RikaFirenetException extends Exception {

  RikaFirenetException(String message) {
    super(message);
  }

  RikaFirenetException(String message, Throwable cause) {
    super(message, cause);
  }
}
