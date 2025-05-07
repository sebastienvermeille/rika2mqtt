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

import static dev.cookiecode.rika2mqtt.plugins.internal.v1.PluginUrlsProvider.*;
import static java.net.URI.create;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;

/**
 * Test class
 *
 * @author Sebastien Vermeille
 */
@ExtendWith(MockitoExtension.class)
public class PluginUrlsProviderTest {
  @InjectMocks private PluginUrlsProvider pluginUrlsProvider;

  @Mock private Environment environment;

  @Test
  void getDeclaredPluginsShouldReturnAnEmptyListGivenOnlyASpaceIsProvided() {

    // GIVEN
    final var space = " ";
    doReturn(space).when(environment).getProperty(PLUGINS_ENV_VAR_NAME);

    // WHEN
    final var urls = pluginUrlsProvider.getDeclaredPlugins();

    // THEN
    assertThat(urls).isEmpty();
  }

  @Test
  void getDeclaredPluginsShouldReturnAnEmptyListGivenEnvIsNotSet() {

    // GIVEN
    doReturn(null).when(environment).getProperty(PLUGINS_ENV_VAR_NAME);

    // WHEN
    final var urls = pluginUrlsProvider.getDeclaredPlugins();

    // THEN
    assertThat(System.getenv(PLUGINS_ENV_VAR_NAME)).isNull();
    assertThat(urls).isEmpty();
  }

  @Test
  void getDeclaredPluginsShouldReturnAUrlObjectWrapping2UrlsGivenTwoUrlsWereProvided()
      throws Exception {

    // GIVEN
    final var pluginAUrl = "http://plugin-a.jar";
    final var pluginBUrl = "http://plugin-b.jar";

    final var pluginsUrls = String.format("%s%s%s", pluginAUrl, PLUGINS_SEPARATOR, pluginBUrl);
    doReturn(pluginsUrls).when(environment).getProperty(PLUGINS_ENV_VAR_NAME);

    // WHEN
    final var urls = pluginUrlsProvider.getDeclaredPlugins();

    // THEN
    assertThat(urls)
        .isNotEmpty()
        .hasSize(2)
        .containsExactly(create(pluginAUrl).toURL(), create(pluginBUrl).toURL());
  }
}
