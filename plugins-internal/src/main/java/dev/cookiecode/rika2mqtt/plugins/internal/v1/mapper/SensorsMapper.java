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
package dev.cookiecode.rika2mqtt.plugins.internal.v1.mapper;

import static org.mapstruct.ReportingPolicy.IGNORE;

import dev.cookiecode.rika2mqtt.plugins.api.v1.model.ParameterDebug;
import dev.cookiecode.rika2mqtt.plugins.api.v1.model.ParameterErrorCount;
import dev.cookiecode.rika2mqtt.plugins.api.v1.model.Sensors;
import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(unmappedTargetPolicy = IGNORE) // ignore as we are using a map
public interface SensorsMapper {

  Sensors toApiSensors(@NonNull dev.cookiecode.rika2mqtt.rika.firenet.model.Sensors sensors);

  @AfterMapping
  default void afterMapping(
      dev.cookiecode.rika2mqtt.rika.firenet.model.Sensors source,
      @MappingTarget Sensors.SensorsBuilder target) {

    // wrap debug properties as a collection
    List<ParameterDebug> debugList = new ArrayList<>();
    debugList.add(ParameterDebug.builder().number(0).value(source.getParameterDebug0()).build());
    debugList.add(ParameterDebug.builder().number(1).value(source.getParameterDebug1()).build());
    debugList.add(ParameterDebug.builder().number(2).value(source.getParameterDebug2()).build());
    debugList.add(ParameterDebug.builder().number(3).value(source.getParameterDebug3()).build());
    debugList.add(ParameterDebug.builder().number(4).value(source.getParameterDebug4()).build());
    target.parametersDebug(debugList);

    // wrap error count properties as a collection
    List<ParameterErrorCount> errorCountList = new ArrayList<>();
    errorCountList.add(
        ParameterErrorCount.builder().number(0).value(source.getParameterErrorCount0()).build());
    errorCountList.add(
        ParameterErrorCount.builder().number(1).value(source.getParameterErrorCount1()).build());
    errorCountList.add(
        ParameterErrorCount.builder().number(2).value(source.getParameterErrorCount2()).build());
    errorCountList.add(
        ParameterErrorCount.builder().number(3).value(source.getParameterErrorCount3()).build());
    errorCountList.add(
        ParameterErrorCount.builder().number(4).value(source.getParameterErrorCount4()).build());
    errorCountList.add(
        ParameterErrorCount.builder().number(5).value(source.getParameterErrorCount5()).build());
    errorCountList.add(
        ParameterErrorCount.builder().number(6).value(source.getParameterErrorCount6()).build());
    errorCountList.add(
        ParameterErrorCount.builder().number(7).value(source.getParameterErrorCount7()).build());
    errorCountList.add(
        ParameterErrorCount.builder().number(8).value(source.getParameterErrorCount8()).build());
    errorCountList.add(
        ParameterErrorCount.builder().number(9).value(source.getParameterErrorCount9()).build());
    errorCountList.add(
        ParameterErrorCount.builder().number(10).value(source.getParameterErrorCount10()).build());
    errorCountList.add(
        ParameterErrorCount.builder().number(11).value(source.getParameterErrorCount11()).build());
    errorCountList.add(
        ParameterErrorCount.builder().number(12).value(source.getParameterErrorCount12()).build());
    errorCountList.add(
        ParameterErrorCount.builder().number(13).value(source.getParameterErrorCount13()).build());
    errorCountList.add(
        ParameterErrorCount.builder().number(14).value(source.getParameterErrorCount14()).build());
    errorCountList.add(
        ParameterErrorCount.builder().number(15).value(source.getParameterErrorCount15()).build());
    errorCountList.add(
        ParameterErrorCount.builder().number(16).value(source.getParameterErrorCount16()).build());
    errorCountList.add(
        ParameterErrorCount.builder().number(17).value(source.getParameterErrorCount17()).build());
    errorCountList.add(
        ParameterErrorCount.builder().number(18).value(source.getParameterErrorCount18()).build());
    errorCountList.add(
        ParameterErrorCount.builder().number(19).value(source.getParameterErrorCount19()).build());
    target.parametersErrorCount(errorCountList);
  }
}
