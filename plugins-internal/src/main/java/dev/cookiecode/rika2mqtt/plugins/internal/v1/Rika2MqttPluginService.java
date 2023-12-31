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

import dev.cookiecode.rika2mqtt.plugins.api.v1.StoveStatusExtension;
import dev.cookiecode.rika2mqtt.plugins.internal.v1.event.PolledStoveStatusEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.flogger.Flogger;
import org.pf4j.PluginManager;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * Service responsible to orchestrate the whole plugins lifecycle: loading, start etc.
 *
 * @author Sebastien Vermeille
 */
@Service
@RequiredArgsConstructor
@Flogger
public class Rika2MqttPluginService {

  private final PluginManager pluginManager;
  private final PluginDownloader pluginDownloader;

  public void start() {
    log.atInfo().log("Fetch plugins ...");
    pluginDownloader.synchronize();

    log.atInfo().log("Plugin manager starting ...");
    pluginManager.loadPlugins();
    pluginManager.startPlugins();
  }

  @EventListener
  public void handlePolledStoveStatusEvent(PolledStoveStatusEvent event) {
    var extensions = pluginManager.getExtensions(StoveStatusExtension.class);

    if (extensions.isEmpty()) {
      log.atFinest().log(
          "None of the %s plugin(s) registered a hook for extension %s, not forwarding stove status.",
          pluginManager.getPlugins().size(), StoveStatusExtension.class.getSimpleName());
    } else {
      extensions.forEach(
          stoveStatusExtension ->
              stoveStatusExtension.onPollStoveStatusSucceed(event.getStoveStatus()));
    }
  }
}
