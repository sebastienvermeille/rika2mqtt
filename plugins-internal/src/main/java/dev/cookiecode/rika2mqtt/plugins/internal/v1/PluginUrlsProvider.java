/*
 * The MIT License
 * Copyright © 2022 Sebastien Vermeille
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package dev.cookiecode.rika2mqtt.plugins.internal.v1;

import static java.net.URI.create;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.flogger.Flogger;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Flogger
public class PluginUrlsProvider {

  static final String PLUGINS_ENV_VAR_NAME = "PLUGINS";
  static final String PLUGINS_SEPARATOR = ";";
  static final String EMPTY_STRING = "";

  private final Environment environment;

  public List<URL> getDeclaredPlugins() {
    final var concatenatedString =
        Optional.ofNullable(environment.getProperty(PLUGINS_ENV_VAR_NAME)).orElse(EMPTY_STRING);
    return Arrays.stream(concatenatedString.split(PLUGINS_SEPARATOR))
        .map(String::trim)
        .filter(urlStr -> !urlStr.isEmpty())
        .map(this::createUrl)
        .filter(Objects::nonNull)
        .toList();
  }

  private URL createUrl(String pluginUrlStr) {
    try {
      return create(pluginUrlStr).toURL();
    } catch (MalformedURLException e) {
      log.atSevere().withCause(e).log("Ignore the following url: %s", pluginUrlStr);
      return null;
    }
  }
}
