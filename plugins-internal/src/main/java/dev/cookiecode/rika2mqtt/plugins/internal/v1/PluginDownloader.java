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

import static java.net.HttpURLConnection.HTTP_OK;
import static java.util.stream.Collectors.toList;

import dev.cookiecode.rika2mqtt.plugins.internal.v1.exceptions.UnableToDownloadPluginException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import lombok.NonNull;
import lombok.extern.flogger.Flogger;
import org.springframework.stereotype.Service;

@Service
@Flogger
public class PluginDownloader {

  private static final String PLUGINS_ENV_VAR_NAME = "PLUGINS";
  private static final String PLUGINS_DIR_ENV_VAR_NAME = "PLUGINS_DIR";
  private static final String PLUGINS_SEPARATOR = ";";

  /**
   * sync plugins dir with PLUGINS environment variable. each plugin url has to be provided in
   * PLUGINS=http://some.jar;http://another.jar
   */
  public void synchronize() {

    // TODO: if md5 file is present (i.e maven central provide it then we should check integrity
    // TODO: override files / delete before (should not delete files already present in plugins dir
    // if they are not listed (useful for dev purpose)
    var pluginsUrls = getDeclaredPlugins();
    var pluginsDir = getPluginsDir();
    for (var pluginUrl : pluginsUrls) {
      try {
        log.atInfo().log("Fetch plugin %s", pluginUrl);
        downloadPlugin(pluginUrl, pluginsDir);
      } catch (UnableToDownloadPluginException e) {
        log.atSevere().withCause(e).log(e.getMessage());
      }
    }
    log.atInfo().log("Plugins synchronization: done.");
  }

  private String getPluginsDir() {
    return Optional.ofNullable(System.getenv(PLUGINS_DIR_ENV_VAR_NAME)).orElse("plugins");
  }

  private List<URL> getDeclaredPlugins() {
    final var concatenatedString =
        Optional.ofNullable(System.getenv(PLUGINS_ENV_VAR_NAME)).orElse("");
    return Arrays.stream(concatenatedString.split(PLUGINS_SEPARATOR))
        .map(String::trim) // Trim each URL string
        .filter(urlStr -> !urlStr.isEmpty()) // Filter out empty or null strings
        .map(
            pluginUrlStr -> {
              try {
                return new URL(pluginUrlStr);
              } catch (MalformedURLException e) {
                log.atSevere().withCause(e).log("Ignore the following url: %s", pluginUrlStr);
                return null;
              }
            })
        .filter(Objects::nonNull) // Remove the null URLs from the list
        .collect(toList());
  }

  public void downloadPlugin(@NonNull URL jarUrl, @NonNull String pluginsDir)
      throws UnableToDownloadPluginException {
    try {
      final var httpConn = (HttpURLConnection) jarUrl.openConnection();
      final var responseCode = httpConn.getResponseCode();

      // Check for HTTP response code 200 (successful connection)
      if (responseCode == HTTP_OK) {
        // Get input stream from the connection
        final var inputStream = httpConn.getInputStream();

        // Create a FileOutputStream to write the downloaded file
        var fileName = "";
        final var disposition = httpConn.getHeaderField("Content-Disposition");
        if (disposition != null) {
          int index = disposition.indexOf("filename=");
          if (index > 0) {
            fileName = disposition.substring(index + 9);
          }
        } else {
          fileName = jarUrl.toString().substring(jarUrl.toString().lastIndexOf("/") + 1);
        }
        final var saveFilePath = pluginsDir + File.separator + fileName;

        final var outputStream = new FileOutputStream(saveFilePath);

        // Read bytes from the input stream and write to the output stream
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
          outputStream.write(buffer, 0, bytesRead);
        }

        // Close streams
        outputStream.close();
        inputStream.close();

        log.atInfo().log("Plugin downloaded to: %s.", saveFilePath);
      } else {
        throw new UnableToDownloadPluginException(
            String.format("No file to download. Server replied with HTTP code: %s", responseCode));
      }
      httpConn.disconnect();
    } catch (MalformedURLException e) {
      throw new UnableToDownloadPluginException(
          String.format("Could not download the plugin, url is malformed: %s", jarUrl), e);
    } catch (IOException e) {
      throw new UnableToDownloadPluginException(
          String.format("Could not download the plugin %s, io error.", jarUrl), e);
    }
  }
}
