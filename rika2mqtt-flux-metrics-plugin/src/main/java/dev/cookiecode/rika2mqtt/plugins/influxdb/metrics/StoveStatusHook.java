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

import static dev.cookiecode.rika2mqtt.plugins.influxdb.metrics.reflection.ReflectionUtils.*;
import static java.lang.Boolean.TRUE;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.TimeUnit.HOURS;

import dev.cookiecode.rika2mqtt.plugins.api.v1.StoveStatusExtension;
import dev.cookiecode.rika2mqtt.plugins.api.v1.model.Controls;
import dev.cookiecode.rika2mqtt.plugins.api.v1.model.Sensors;
import dev.cookiecode.rika2mqtt.plugins.api.v1.model.StoveStatus;
import java.util.Optional;
import java.util.stream.IntStream;
import kamon.Kamon;
import lombok.NonNull;
import lombok.extern.flogger.Flogger;
import org.pf4j.Extension;

@Extension
@Flogger
public class StoveStatusHook implements StoveStatusExtension {

  // Tags used for influx storage (ease filtering)
  private static final String STOVE_ID = "STOVE_ID";
  private static final String STOVE_NAME = "STOVE_NAME";
  private static final String FAN_ID = "FAN_ID";
  private static final String DAY_OF_WEEK = "DAY_OF_WEEK";
  private static final String TIME_RANGE_INDEX = "TIME_RANGE_INDEX";
  private static final String ERROR_NUMBER = "ERROR_NUMBER";
  private static final String DEBUG_NUMBER = "DEBUG_NUMBER";

  @Override
  public void onPollStoveStatusSucceed(StoveStatus stoveStatus) {
    log.atInfo().atMostEvery(1, HOURS).log(
        "Stove status is being continuously forwarded to Influx");

    exportSensorsMetrics(stoveStatus);
    exportControlsMetrics(stoveStatus);

    exportProperty(stoveStatus, "lastSeenMinutes", Long.class);
    exportProperty(stoveStatus, "lastConfirmedRevision", Long.class);
  }

  private void exportSensorsMetrics(@NonNull final StoveStatus stoveStatus) {

    exportProperty(stoveStatus, "sensors.inputRoomTemperature", Double.class);
    exportProperty(stoveStatus, "sensors.inputFlameTemperature", Integer.class);
    exportProperty(stoveStatus, "sensors.inputBakeTemperature", Integer.class);
    exportProperty(stoveStatus, "sensors.statusError", Integer.class);
    exportProperty(stoveStatus, "sensors.statusSubError", Integer.class);
    exportProperty(stoveStatus, "sensors.statusWarning", Integer.class);
    exportProperty(stoveStatus, "sensors.statusService", Integer.class);
    exportProperty(stoveStatus, "sensors.outputDischargeMotor", Integer.class);
    exportProperty(stoveStatus, "sensors.outputDischargeCurrent", Integer.class);
    exportProperty(stoveStatus, "sensors.outputIdFan", Integer.class);
    exportProperty(stoveStatus, "sensors.outputIdFanTarget", Integer.class);
    exportProperty(stoveStatus, "sensors.outputInsertionMotor", Integer.class);
    exportProperty(stoveStatus, "sensors.outputInsertionCurrent", Integer.class);
    exportProperty(stoveStatus, "sensors.outputAirFlaps", Integer.class);
    exportProperty(stoveStatus, "sensors.outputAirFlapsTargetPosition", Integer.class);
    exportProperty(stoveStatus, "sensors.outputBurnBackFlapMagnet", Boolean.class);
    exportProperty(stoveStatus, "sensors.outputGridMotor", Boolean.class);
    exportProperty(stoveStatus, "sensors.outputIgnition", Boolean.class);
    exportProperty(stoveStatus, "sensors.inputUpperTemperatureLimiter", Boolean.class);
    exportProperty(stoveStatus, "sensors.inputPressureSwitch", Boolean.class);
    exportProperty(stoveStatus, "sensors.inputPressureSensor", Integer.class);
    exportProperty(stoveStatus, "sensors.inputGridContact", Boolean.class);
    exportProperty(stoveStatus, "sensors.inputDoor", Boolean.class);
    exportProperty(stoveStatus, "sensors.inputCover", Boolean.class);
    exportProperty(stoveStatus, "sensors.inputExternalRequest", Boolean.class);
    exportProperty(stoveStatus, "sensors.inputBurnBackFlapSwitch", Boolean.class);
    exportProperty(stoveStatus, "sensors.inputFlueGasFlapSwitch", Boolean.class);
    exportProperty(stoveStatus, "sensors.inputBoardTemperature", Double.class);
    exportProperty(stoveStatus, "sensors.inputCurrentStage", Integer.class);
    exportProperty(stoveStatus, "sensors.inputTargetStagePid", Integer.class);
    exportProperty(stoveStatus, "sensors.inputCurrentStagePid", Integer.class);

    exportProperty(stoveStatus, "sensors.statusMainState", Integer.class);
    exportProperty(stoveStatus, "sensors.statusSubState", Integer.class);
    exportProperty(stoveStatus, "sensors.statusWifiStrength", Integer.class);
    exportProperty(stoveStatus, "sensors.parameterEcoModePossible", Boolean.class);
    exportProperty(stoveStatus, "sensors.parameterFabricationNumber", Integer.class);
    exportProperty(stoveStatus, "sensors.parameterStoveTypeNumber", Integer.class);
    exportProperty(stoveStatus, "sensors.parameterLanguageNumber", Integer.class);
    exportProperty(stoveStatus, "sensors.parameterVersionMainBoard", Integer.class);

    exportProperty(stoveStatus, "sensors.parameterVersionTft", Integer.class);
    exportProperty(stoveStatus, "sensors.parameterVersionWifi", Integer.class);
    exportProperty(stoveStatus, "sensors.parameterVersionMainBoardBootLoader", Integer.class);
    exportProperty(stoveStatus, "sensors.parameterVersionTftBootLoader", Integer.class);
    exportProperty(stoveStatus, "sensors.parameterVersionWifiBootLoader", Integer.class);
    exportProperty(stoveStatus, "sensors.parameterVersionMainBoardSub", Integer.class);
    exportProperty(stoveStatus, "sensors.parameterVersionTftSub", Integer.class);
    exportProperty(stoveStatus, "sensors.parameterVersionWifiSub", Integer.class);

    exportProperty(stoveStatus, "sensors.parameterRuntimePellets", Integer.class);
    exportProperty(stoveStatus, "sensors.parameterRuntimeLogs", Integer.class);
    exportProperty(stoveStatus, "sensors.parameterFeedRateTotal", Integer.class);
    exportProperty(stoveStatus, "sensors.parameterFeedRateService", Integer.class);
    exportProperty(stoveStatus, "sensors.parameterServiceCountdownKg", Integer.class);
    exportProperty(stoveStatus, "sensors.parameterServiceCountdownTime", Integer.class);
    exportProperty(stoveStatus, "sensors.parameterIgnitionCount", Integer.class);
    exportProperty(stoveStatus, "sensors.parameterOnOffCycleCount", Integer.class);
    exportProperty(stoveStatus, "sensors.parameterFlameSensorOffset", Integer.class);
    exportProperty(stoveStatus, "sensors.parameterPressureSensorOffset", Integer.class);

    exportProperty(stoveStatus, "sensors.statusHeatingTimesNotProgrammed", Boolean.class);
    exportProperty(stoveStatus, "sensors.statusFrostStarted", Boolean.class);
    exportProperty(stoveStatus, "sensors.parameterSpiralMotorsTuning", Integer.class);
    exportProperty(stoveStatus, "sensors.parameterIdFanTuning", Integer.class);
    exportProperty(stoveStatus, "sensors.parameterCleanIntervalBig", Integer.class);
    exportProperty(stoveStatus, "sensors.parameterKgTillCleaning", Integer.class);

    // following metrics are requiring special treatment
    stoveStatus
            .getSensors()
            .getParametersErrorCount()
            .forEach(
                    parameterErrorCount ->
                            Kamon.gauge("sensors.parameterErrorCount")
                                    .withTag(STOVE_ID, stoveStatus.getStoveId())
                                    .withTag(STOVE_NAME, stoveStatus.getName())
                                    .withTag(ERROR_NUMBER, parameterErrorCount.getNumber()) // extra tag
                                    .update(parameterErrorCount.getValue()));

    stoveStatus
            .getSensors()
            .getParametersDebug()
            .forEach(
                    parameterDebug -> {
                      Kamon.gauge("sensors.parameterDebug")
                              .withTag(STOVE_ID, stoveStatus.getStoveId())
                              .withTag(STOVE_NAME, stoveStatus.getName())
                              .withTag(DEBUG_NUMBER, parameterDebug.getNumber()) // extra tag
                              .update(parameterDebug.getValue());
                    });
  }

  private void exportControlsMetrics(@NonNull StoveStatus stoveStatus) {
    exportProperty(stoveStatus, "controls.revision", Long.class);
    exportProperty(stoveStatus, "controls.on", Boolean.class);
    exportProperty(stoveStatus, "controls.operatingMode", Integer.class);
    exportProperty(stoveStatus, "controls.heatingPower", Integer.class);
    exportProperty(stoveStatus, "controls.targetTemperature", Integer.class);
    exportProperty(stoveStatus, "controls.bakeTemperature", Integer.class);
    exportProperty(stoveStatus, "controls.ecoMode", Boolean.class);
    exportProperty(stoveStatus, "controls.heatingTimesActiveForComfort", Boolean.class);
    exportProperty(stoveStatus, "controls.setBackTemperature", Integer.class);
    exportProperty(stoveStatus, "controls.frostProtectionActive", Boolean.class);
    exportProperty(stoveStatus, "controls.frostProtectionTemperature", Integer.class);
    exportProperty(stoveStatus, "controls.temperatureOffset", Double.class);
    exportProperty(stoveStatus, "controls.roomPowerRequest", Integer.class);

    // following metrics are requiring special treatment
    stoveStatus
        .getControls()
        .getFans()
        .forEach(
            convectionFan -> {
              Kamon.gauge("controls.convectionFan.area")
                  .withTag(STOVE_ID, stoveStatus.getStoveId())
                  .withTag(STOVE_NAME, stoveStatus.getName())
                  .withTag(FAN_ID, convectionFan.getIdentifier()) // extra tag
                  .update(convectionFan.getArea());

              Kamon.gauge("controls.convectionFan.level")
                  .withTag(STOVE_ID, stoveStatus.getStoveId())
                  .withTag(STOVE_NAME, stoveStatus.getName())
                  .withTag(FAN_ID, convectionFan.getIdentifier()) // extra tag
                  .update(convectionFan.getLevel());

              Kamon.gauge("controls.convectionFan.active")
                  .withTag(STOVE_ID, stoveStatus.getStoveId())
                  .withTag(STOVE_NAME, stoveStatus.getName())
                  .withTag(FAN_ID, convectionFan.getIdentifier()) // extra tag
                  .update(convectionFan.isActive() ? 1 : 0);
            });

    stoveStatus
        .getControls()
        .getHeatingTimes()
        .forEach(
            (dayOfWeek, timeRanges) ->
                IntStream.range(0, timeRanges.size())
                    .forEach(
                        index -> {
                          final var timeRange = timeRanges.get(index);
                          // from
                          Kamon.gauge("controls.heatingTime.from.hours")
                              .withTag(STOVE_ID, stoveStatus.getStoveId())
                              .withTag(STOVE_NAME, stoveStatus.getName())
                              .withTag(DAY_OF_WEEK, dayOfWeek.name()) // extra tag
                              .withTag(TIME_RANGE_INDEX, index)
                              .update(timeRange.getFrom().getHours());
                          Kamon.gauge("controls.heatingTime.from.minutes")
                              .withTag(STOVE_ID, stoveStatus.getStoveId())
                              .withTag(STOVE_NAME, stoveStatus.getName())
                              .withTag(DAY_OF_WEEK, dayOfWeek.name()) // extra tag
                              .withTag(TIME_RANGE_INDEX, index)
                              .update(timeRange.getFrom().getMinutes());
                          Kamon.gauge("controls.heatingTime.from.decimal")
                              .withTag(STOVE_ID, stoveStatus.getStoveId())
                              .withTag(STOVE_NAME, stoveStatus.getName())
                              .withTag(DAY_OF_WEEK, dayOfWeek.name()) // extra tag
                              .withTag(TIME_RANGE_INDEX, index)
                              .update(timeRange.getFrom().asDecimal());

                          // to
                          Kamon.gauge("controls.heatingTime.to.hours")
                              .withTag(STOVE_ID, stoveStatus.getStoveId())
                              .withTag(STOVE_NAME, stoveStatus.getName())
                              .withTag(DAY_OF_WEEK, dayOfWeek.name()) // extra tag
                              .withTag(TIME_RANGE_INDEX, index)
                              .update(timeRange.getTo().getHours());
                          Kamon.gauge("controls.heatingTime.to.minutes")
                              .withTag(STOVE_ID, stoveStatus.getStoveId())
                              .withTag(STOVE_NAME, stoveStatus.getName())
                              .withTag(DAY_OF_WEEK, dayOfWeek.name()) // extra tag
                              .withTag(TIME_RANGE_INDEX, index)
                              .update(timeRange.getTo().getMinutes());

                          Kamon.gauge("controls.heatingTime.to.decimal")
                              .withTag(STOVE_ID, stoveStatus.getStoveId())
                              .withTag(STOVE_NAME, stoveStatus.getName())
                              .withTag(DAY_OF_WEEK, dayOfWeek.name()) // extra tag
                              .withTag(TIME_RANGE_INDEX, index)
                              .update(timeRange.getTo().asDecimal());
                        }));

    final var controlsDebugs = stoveStatus.getControls().getDebugs();
    IntStream.range(0, controlsDebugs.size())
        .forEach(
            index -> {
              final var debugValue = controlsDebugs.get(index);
              Kamon.gauge("controls.debug")
                  .withTag(STOVE_ID, stoveStatus.getStoveId())
                  .withTag(STOVE_NAME, stoveStatus.getName())
                  .withTag(DEBUG_NUMBER, index) // extra tag
                  .update(debugValue);
            });
  }

  private void exportProperty(
      @NonNull StoveStatus stoveStatus, @NonNull final String propertyName, Class<?> returnType) {
    if (returnType == Double.class) {
      getDoublePropertyValue(stoveStatus, propertyName)
          .ifPresentOrElse(
              value ->
                  Kamon.gauge(propertyName)
                      .withTag(STOVE_ID, stoveStatus.getStoveId())
                      .withTag(STOVE_NAME, stoveStatus.getName())
                      .update(value),
              () ->
                  log.atWarning().log(
                      "Could not export property %s, it could not be retrieved.", propertyName));
    } else if (returnType == Integer.class) {
      getIntegerPropertyValue(stoveStatus, propertyName)
          .ifPresentOrElse(
              value ->
                  Kamon.gauge(propertyName)
                      .withTag(STOVE_ID, stoveStatus.getStoveId())
                      .withTag(STOVE_NAME, stoveStatus.getName())
                      .update(value),
              () ->
                  log.atWarning().log(
                      "Could not export property %s, it could not be retrieved.", propertyName));
      ;
    } else if (returnType == Long.class) {
      getLongPropertyValue(stoveStatus, propertyName)
          .ifPresentOrElse(
              value ->
                  Kamon.gauge(propertyName)
                      .withTag(STOVE_ID, stoveStatus.getStoveId())
                      .withTag(STOVE_NAME, stoveStatus.getName())
                      .update(value),
              () ->
                  log.atWarning().log(
                      "Could not export property %s, it could not be retrieved.", propertyName));
      ;
    } else if (returnType == Boolean.class) {
      getBooleanPropertyValue(stoveStatus, propertyName)
          .map(value -> value == TRUE ? 1 : 0)
          .map(
              value ->
                  Kamon.gauge(propertyName)
                      .withTag(STOVE_ID, stoveStatus.getStoveId())
                      .withTag(STOVE_NAME, stoveStatus.getName())
                      .update(value));
    } else {
      log.atSevere().log(
          "Could not extract property %s because it's return type %s is unsupported.",
          propertyName, returnType);
    }
  }

  private Optional<String> getPropertyValue(
      @NonNull StoveStatus stoveStatus, @NonNull final String propertyName) {
    if (propertyName.startsWith("sensors.")) {
      try {
        final var shortName = propertyName.replace("sensors.", "");
        final var getterMethod = getPropertyGetterMethod(Sensors.class, shortName);

        var result = getterMethod.invoke(stoveStatus.getSensors());
        return ofNullable(result.toString());
      } catch (Exception ex) {
        log.atSevere().withCause(ex).log("Could not get property %s", propertyName);
        return Optional.empty();
      }
    } else if (propertyName.startsWith("controls.")) {
      try {
        final var shortName = propertyName.replace("controls.", "");
        var getterMethod = getPropertyGetterMethod(Controls.class, shortName);
        return ofNullable(getterMethod.invoke(stoveStatus.getControls())).map(Object::toString);
      } catch (Exception ex) {
        log.atSevere().withCause(ex).log("Could not get property %s", propertyName);
        return Optional.empty();
      }
    } else {
      try {
        final var shortName = propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
        final var getterMethod = getPropertyGetterMethod(StoveStatus.class, shortName);

        var result = getterMethod.invoke(stoveStatus);
        return ofNullable(result.toString());
      } catch (Exception ex) {
        log.atSevere().withCause(ex).log("Could not get property %s", propertyName);
        return Optional.empty();
      }
    }
  }

  private Optional<Double> getDoublePropertyValue(
      @NonNull StoveStatus stoveStatus, @NonNull final String propertyName) {
    return getPropertyValue(stoveStatus, propertyName).map(Double::valueOf);
  }

  private Optional<Integer> getIntegerPropertyValue(
      @NonNull StoveStatus stoveStatus, @NonNull final String propertyName) {
    return getPropertyValue(stoveStatus, propertyName).map(Integer::valueOf);
  }

  private Optional<Long> getLongPropertyValue(
      @NonNull StoveStatus stoveStatus, @NonNull final String propertyName) {
    return getPropertyValue(stoveStatus, propertyName).map(Long::valueOf);
  }

  private Optional<Boolean> getBooleanPropertyValue(
      @NonNull StoveStatus stoveStatus, @NonNull final String propertyName) {
    return getPropertyValue(stoveStatus, propertyName).map(Boolean::valueOf);
  }
}
