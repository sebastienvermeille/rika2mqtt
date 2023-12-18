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
package dev.cookiecode.rika2mqtt.plugins.example;

import dev.cookiecode.rika2mqtt.plugins.api.v1.StoveStatusExtension;
import dev.cookiecode.rika2mqtt.plugins.api.v1.model.StoveStatus;
import lombok.extern.flogger.Flogger;
import org.pf4j.Extension;

import static dev.cookiecode.rika2mqtt.plugins.example.ExamplePlugin.PLUGIN_NAME;

@Extension
@Flogger
public class ExampleHook implements StoveStatusExtension {

  @Override
  public void onPollStoveStatusSucceed(StoveStatus stoveStatus) {
    log.atInfo()
            .log("%s >> onPollStoveStatusSucceed invoked", PLUGIN_NAME);
  }

  public void stop() {
    log.atInfo()
            .log("%s >> %s stopped", PLUGIN_NAME, ExampleHook.class.getSimpleName());
  }
}
