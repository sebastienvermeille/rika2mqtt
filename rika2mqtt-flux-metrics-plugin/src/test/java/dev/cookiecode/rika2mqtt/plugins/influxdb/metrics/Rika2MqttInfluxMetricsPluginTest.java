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
package dev.cookiecode.rika2mqtt.plugins.influxdb.metrics;

import static org.mockito.Mockito.*;

import com.typesafe.config.Config;
import dev.cookiecode.rika2mqtt.plugins.api.PluginContext;
import kamon.Kamon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/** Test class */
@ExtendWith(MockitoExtension.class)
class Rika2MqttInfluxMetricsPluginTest {

  private Rika2MqttInfluxMetricsPlugin plugin;

  private PluginContext pluginContext;

  @BeforeEach
  public void beforeEach() {
    //    pluginContext = mock(PluginContext.class);
    //    plugin = spy(new Rika2MqttInfluxMetricsPlugin(pluginContext));
    plugin = spy(new Rika2MqttInfluxMetricsPlugin());
  }

  @Mock private Config config;

  @Test
  void startShouldInitKamon() {
    // GIVEN
    doReturn(config).when(plugin).loadConfig();

    try (var mockedKamon = mockStatic(Kamon.class)) {
      // WHEN
      plugin.start();

      // THEN
      mockedKamon.verify(Kamon::init, times(1));
    }
  }

  @Test
  void startShouldLoadKamonModules() {
    // GIVEN
    doReturn(config).when(plugin).loadConfig();

    try (var mockedKamon = mockStatic(Kamon.class)) {
      // WHEN
      plugin.start();

      // THEN
      mockedKamon.verify(Kamon::loadModules, times(1));
    }
  }

  @Test
  void stopShouldStopKamon() {
    // GIVEN
    try (var mockedKamon = mockStatic(Kamon.class)) {
      // WHEN
      plugin.stop();

      // THEN
      mockedKamon.verify(Kamon::stop, times(1));
    }
  }
}
