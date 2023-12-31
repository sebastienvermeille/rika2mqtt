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
package dev.cookiecode.rika2mqtt.plugins.api.v1.annotations;

import dev.cookiecode.rika2mqtt.plugins.api.v1.model.plugins.OptionalPluginConfigurationParameter;
import dev.cookiecode.rika2mqtt.plugins.api.v1.model.plugins.PluginConfigurationParameter;
import dev.cookiecode.rika2mqtt.plugins.api.v1.model.plugins.RequiredPluginConfigurationParameter;
import java.util.List;

/**
 * Plugins that require external configuration should implement this interface. This way, they
 * benefit from Rika2Mqtt configuration check at startup time. The bridge will verify that the
 * required parameters are provided and then only start the plugin.
 *
 * @author Sebastien Vermeille
 */
public interface ConfigurablePlugin {

  /**
   * Declare parameters supported by a plugin. This allows to define some mandatory parameters,
   * specify their types and default values. At startup when loading plugins Rika2Mqtt will check
   * for availability of required parameters and display log errors in case of failure.
   *
   * <p>These parameters can be build using fluent coding via the following builders: {@link
   * RequiredPluginConfigurationParameter#builder()}, {@link
   * OptionalPluginConfigurationParameter#builder()}
   *
   * @return a list of plugin configuration parameters
   */
  List<PluginConfigurationParameter> declarePluginConfigurationParameters();
}
