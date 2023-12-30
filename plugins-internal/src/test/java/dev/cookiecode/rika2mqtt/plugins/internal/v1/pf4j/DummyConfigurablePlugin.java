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
package dev.cookiecode.rika2mqtt.plugins.internal.v1.pf4j;

import dev.cookiecode.rika2mqtt.plugins.api.v1.Rika2MqttPlugin;
import dev.cookiecode.rika2mqtt.plugins.api.v1.annotations.ConfigurablePlugin;
import dev.cookiecode.rika2mqtt.plugins.api.v1.model.plugins.OptionalPluginConfigurationParameter;
import dev.cookiecode.rika2mqtt.plugins.api.v1.model.plugins.PluginConfigurationParameter;
import dev.cookiecode.rika2mqtt.plugins.api.v1.model.plugins.RequiredPluginConfigurationParameter;
import java.util.List;

/**
 * Test class An example of plugin not implementing ConfigurablePlugin used for testing a behaviour
 */
public class DummyConfigurablePlugin extends Rika2MqttPlugin implements ConfigurablePlugin {
  @Override
  public List<PluginConfigurationParameter> declarePluginConfigurationParameters() {
    return List.of(
        OptionalPluginConfigurationParameter.builder()
            .withParameterName("language")
            .withDescription("define the language to use")
            .withValueType(String.class)
            .withDefaultValue("en")
            .withExample("en,fr,de")
            .build(),
        RequiredPluginConfigurationParameter.builder()
            .withParameterName("password")
            .withDescription("some password")
            .withValueType(String.class)
            .build());
  }
}
