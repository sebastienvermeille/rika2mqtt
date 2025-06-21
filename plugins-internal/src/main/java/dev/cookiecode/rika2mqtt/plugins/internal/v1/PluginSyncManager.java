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

import dev.cookiecode.rika2mqtt.plugins.api.Beta;
import dev.cookiecode.rika2mqtt.plugins.internal.v1.exceptions.UnableToDownloadPluginException;
import java.net.URL;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.flogger.Flogger;
import org.springframework.stereotype.Service;

/**
 * Service class responsible to download rika2mqtt plugins and synchronize them at startup.
 *
 * @author Sebastien Vermeille
 */
@Beta
@Service
@Flogger
@RequiredArgsConstructor
public class PluginSyncManager {

  private final HttpPluginDownloader pluginDownloader;

  /**
   * sync plugins dir with PLUGINS environment variable. each plugin url has to be provided in
   * PLUGINS=http://some.jar;http://another.jar
   */
  public void synchronize(@NonNull String pluginsDir, @NonNull List<URL> pluginsUrls) {
    for (var pluginUrl : pluginsUrls) {
      try {
        log.atInfo().log("Fetch plugin %s", pluginUrl);
        pluginDownloader.downloadPlugin(pluginUrl, pluginsDir);
      } catch (UnableToDownloadPluginException e) {
        log.atSevere().withCause(e).log(e.getMessage());
      }
    }
    log.atInfo().log("Plugins synchronization: done.");
  }
}
