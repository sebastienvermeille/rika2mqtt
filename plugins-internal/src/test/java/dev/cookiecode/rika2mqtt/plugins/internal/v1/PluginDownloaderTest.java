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

import static dev.cookiecode.rika2mqtt.plugins.internal.v1.PluginDownloader.*;
import static java.net.URI.create;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;

/**
 * Test class
 *
 * @author Sebastien Vermeille
 */
@ExtendWith(MockitoExtension.class)
class PluginDownloaderTest {

  @TempDir public Path pluginDir;

  @InjectMocks @Spy private PluginDownloader pluginDownloader;

  @Mock private Environment environment;

  @Test
  void getDeclaredPluginsShouldReturnAnEmptyListGivenOnlyASpaceIsProvided() {

    // GIVEN
    final var space = " ";
    doReturn(space).when(environment).getProperty(PLUGINS_ENV_VAR_NAME);

    // WHEN
    final var urls = pluginDownloader.getDeclaredPlugins();

    // THEN
    assertThat(urls).isEmpty();
  }

  @Test
  void getDeclaredPluginsShouldReturnAnEmptyListGivenNothingIsProvided() {

    // GIVEN
    final var empty = "";
    doReturn(empty).when(environment).getProperty(PLUGINS_ENV_VAR_NAME);

    // WHEN
    final var urls = pluginDownloader.getDeclaredPlugins();

    // THEN
    assertThat(urls).isEmpty();
  }

  @Test
  void getDeclaredPluginsShouldReturnAnEmptyListGivenEnvIsNotSet() {

    // GIVEN
    doReturn(null).when(environment).getProperty(PLUGINS_ENV_VAR_NAME);

    // WHEN
    final var urls = pluginDownloader.getDeclaredPlugins();

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
    final var urls = pluginDownloader.getDeclaredPlugins();

    // THEN
    assertThat(urls)
        .isNotEmpty()
        .hasSize(2)
        .containsExactly(create(pluginAUrl).toURL(), create(pluginBUrl).toURL());
  }

  @Test
  void getPluginsDirShouldReturnDefaultPluginsDirGivenEnvIsNotSet() {
    // GIVEN
    doReturn(null).when(environment).getProperty(PLUGINS_DIR_ENV_VAR_NAME);

    // WHEN
    final var pluginsDir = pluginDownloader.getPluginsDir();

    // THEN
    assertThat(pluginsDir).isEqualTo(DEFAULT_PLUGINS_DIR);
  }

  @Test
  void getPluginsDirShouldReturnPluginsDirProvidedValueDirGivenEnvIsSet() {
    // GIVEN
    final var somedir = "somedir";
    doReturn(somedir).when(environment).getProperty(PLUGINS_DIR_ENV_VAR_NAME);

    // WHEN
    final var pluginsDir = pluginDownloader.getPluginsDir();

    // THEN
    assertThat(pluginsDir).isEqualTo(somedir);
  }

  @Test
  void synchronizeShouldInvokeDownloadPlugin2TimesGivenThereAreTwoPluginsToDownload()
      throws Exception {
    // GIVEN
    final var pluginAUrl = "http://plugin-a.jar";
    final var pluginBUrl = "http://plugin-b.jar";

    final var pluginsUrls = String.format("%s%s%s", pluginAUrl, PLUGINS_SEPARATOR, pluginBUrl);
    doReturn(pluginsUrls).when(environment).getProperty(PLUGINS_ENV_VAR_NAME);

    final var pluginDirPath = pluginDir.toAbsolutePath().toString();
    doReturn(pluginDirPath).when(environment).getProperty(PLUGINS_DIR_ENV_VAR_NAME);

    doNothing().when(pluginDownloader).downloadPlugin(any(), anyString());

    // WHEN
    pluginDownloader.synchronize();

    // THEN
    verify(pluginDownloader, times(2)).downloadPlugin(any(URL.class), anyString());
  }

  @Test
  void downloadPluginShouldDownloadTheFileGivenItExists() throws Exception {
    // GIVEN
    final var pluginUrl = "https://repo.maven.apache.org/maven2/dev/cookiecode/maven-metadata.xml";

    // WHEN
    pluginDownloader.downloadPlugin(
        create(pluginUrl).toURL(), pluginDir.toAbsolutePath().toString());

    // THEN
    final var expectedFile = Paths.get(pluginDir.toAbsolutePath().toString(), "maven-metadata.xml");
    assertThat(Files.exists(expectedFile)).as("File has been downloaded").isTrue();
  }
}
