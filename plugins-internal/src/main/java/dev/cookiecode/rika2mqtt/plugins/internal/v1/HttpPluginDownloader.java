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

import dev.cookiecode.rika2mqtt.plugins.internal.v1.exceptions.UnableToDownloadPluginException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.flogger.Flogger;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Flogger
public class HttpPluginDownloader implements PluginDownloader {

  // TODO: if md5 file is present (i.e maven central provide it then we should check integrity
  // TODO: override files / delete before (should not delete files already present in plugins dir
  // if they are not listed (useful for dev purpose)
  @Override
  public void downloadPlugin(@NonNull URL jarUrl, @NonNull String pluginsDir)
      throws UnableToDownloadPluginException {
    try {
      final var httpConn = (HttpURLConnection) jarUrl.openConnection();
      final var responseCode = httpConn.getResponseCode();

      if (responseCode == HTTP_OK) {
        final var inputStream = httpConn.getInputStream();

        var fileName = getFileName(httpConn);
        var saveFilePath = pluginsDir + File.separator + fileName;

        try (var outputStream = new FileOutputStream(saveFilePath)) {
          copyInputStreamToOutputStream(inputStream, outputStream);
        }

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

  private String getFileName(HttpURLConnection httpConn) {
    var disposition = httpConn.getHeaderField("Content-Disposition");
    if (disposition != null) {
      int index = disposition.indexOf("filename=");
      if (index > 0) {
        return disposition.substring(index + 9);
      }
    }
    return httpConn
        .getURL()
        .toString()
        .substring(httpConn.getURL().toString().lastIndexOf("/") + 1);
  }

  private void copyInputStreamToOutputStream(InputStream inputStream, OutputStream outputStream)
      throws IOException {
    byte[] buffer = new byte[4096];
    int bytesRead;
    while ((bytesRead = inputStream.read(buffer)) != -1) {
      outputStream.write(buffer, 0, bytesRead);
    }
  }
}
