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

import com.google.common.annotations.VisibleForTesting;
import dev.cookiecode.rika2mqtt.plugins.api.v1.StoveErrorExtension;
import dev.cookiecode.rika2mqtt.plugins.api.v1.StoveStatusExtension;
import dev.cookiecode.rika2mqtt.plugins.internal.v1.event.PolledStoveStatusEvent;
import dev.cookiecode.rika2mqtt.plugins.internal.v1.event.StoveErrorEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.flogger.Flogger;
import org.pf4j.PluginManager;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service responsible to orchestrate the whole plugins lifecycle: loading, start etc.
 *
 * @author Sebastien Vermeille
 */
@Service
@RequiredArgsConstructor
@Flogger
public class Rika2MqttPluginService {

  static final String PLUGINS_DIR_ENV_VAR_NAME = "PLUGINS_DIR";
  static final String DEFAULT_PLUGINS_DIR = "plugins";

  private final PluginManager pluginManager;
  private final PluginSyncManager pluginSyncManager;
  private final Environment environment;
  private final PluginUrlsProvider pluginUrlsProvider;

  public void start() {
    log.atInfo().log("Fetch plugins ...");
    final var pluginsDir = getPluginsDir();
    final var pluginsUrls = pluginUrlsProvider.getDeclaredPlugins();
    pluginSyncManager.synchronize(pluginsDir, pluginsUrls);

    log.atInfo().log("Plugin manager starting ...");
    pluginManager.loadPlugins();
    pluginManager.startPlugins();
  }

  @VisibleForTesting
  String getPluginsDir() {
    return Optional.ofNullable(environment.getProperty(PLUGINS_DIR_ENV_VAR_NAME))
            .orElse(DEFAULT_PLUGINS_DIR);
  }

  @EventListener
  public void handlePolledStoveStatusEvent(PolledStoveStatusEvent event) {
    var extensions = pluginManager.getExtensions(StoveStatusExtension.class);

    if (extensions.isEmpty()) {
      log.atFinest().log(
          "None of the %s plugin(s) registered a hook for extension %s, not forwarding stove status.",
          pluginManager.getPlugins().size(), StoveStatusExtension.class.getSimpleName());
    } else {
      extensions.forEach(extension -> extension.onPollStoveStatusSucceed(event.getStoveStatus()));
    }
  }

  @EventListener
  public void handleStoveErrorEvent(StoveErrorEvent event) {
    var extensions = pluginManager.getExtensions(StoveErrorExtension.class);

    if (extensions.isEmpty()) {
      log.atFinest().log(
          "None of the %s plugin(s) registered a hook for extension %s, not forwarding stove error.",
          pluginManager.getPlugins().size(), StoveErrorExtension.class.getSimpleName());
    } else {
      extensions.forEach(extension -> extension.onStoveError(event.getStoveError()));
    }
  }
}
