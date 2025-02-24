package dev.cookiecode.rika2mqtt.rika.firenet.api;

import java.util.ArrayList;
import java.util.List;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * @see https://stackoverflow.com/a/34884863
 */
public class LocalCookieJar implements CookieJar {

  private List<Cookie> cookies;

  @Override
  public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
    this.cookies = cookies;
  }

  @Override
  public List<Cookie> loadForRequest(HttpUrl url) {
    if (cookies != null) return cookies;
    return new ArrayList<>();
  }
}
