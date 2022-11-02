package ch.svermeille.rika.health.service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.time.Duration;

/**
 * @author Sebastien Vermeille
 * @implNote https://stackoverflow.com/a/3584332
 */
public class PingUtils {
  public static boolean pingHost(final String host, final int port, final Duration timeout) {
    try(final Socket socket = new Socket()) {
      socket.connect(new InetSocketAddress(host, port), (int) timeout.toMillis());
      return true;
    } catch(final IOException e) {
      return false; // Either timeout or unreachable or failed DNS lookup.
    }
  }

  /**
   * Pings a HTTP URL. This effectively sends a HEAD request and returns <code>true</code> if the response code is in
   * the 200-399 range.
   *
   * @param url The HTTP URL to be pinged.
   * @param timeout The timeout in millis for both the connection timeout and the response read timeout. Note that
   * the total timeout is effectively two times the given timeout.
   * @return <code>true</code> if the given HTTP URL has returned response code 200-399 on a HEAD request within the
   * given timeout, otherwise <code>false</code>.
   */
  public static boolean pingUrl(String url, final Duration timeout) {
    url = url.replaceFirst("^https", "http"); // Otherwise an exception may be thrown on invalid SSL certificates.

    try {
      final HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
      connection.setConnectTimeout((int) timeout.toMillis());
      connection.setReadTimeout((int) timeout.toMillis());
      connection.setRequestMethod("HEAD");
      final int responseCode = connection.getResponseCode();
      return (200 <= responseCode && responseCode <= 399);
    } catch(final IOException exception) {
      return false;
    }
  }
}
