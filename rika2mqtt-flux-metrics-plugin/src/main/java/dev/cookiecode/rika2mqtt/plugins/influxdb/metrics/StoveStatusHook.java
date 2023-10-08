package dev.cookiecode.rika2mqtt.plugins.influxdb.metrics;

import static java.util.Optional.ofNullable;

import dev.cookiecode.rika2mqtt.plugins.api.StoveStatusExtension;
import dev.cookiecode.rika2mqtt.plugins.api.model.StoveStatus;
import kamon.Kamon;
import lombok.NonNull;
import org.pf4j.Extension;

@Extension
public class StoveStatusHook implements StoveStatusExtension {

  private static final String STOVE_ID = "STOVE_ID";
  private static final String STOVE_NAME = "STOVE_NAME";
  private static final String ERROR_NUMBER = "ERROR_NUMBER";
  private static final String DEBUG_NUMBER = "DEBUG_NUMBER";

  private static final int MAX_ERROR_NUMBER = 18;
  private static final int MAX_DEBUG_NUMBER = 4;

  @Override
  public void onPollStoveStatusSucceed(StoveStatus stoveStatus) {
    System.out.println(
        "STOVE STATUS POLLED HOOKED INSIDE PLUGIN WOOHOO!"); // TODO: add flogger support

    //    Kamon
    //      .gauge("LAST_SEEN_MINUTES", "Last time the stove communicated with rika-firenet
    // servers.") // TODO: should we store it that way or with date?
    //      .withTag(STOVE_ID, stoveStatus.getStoveId())
    //      .withTag(STOVE_NAME, stoveStatus.getName())
    //      .update(stoveStatus.getLastSeenMinutes());

    // TODO: last confirmed revision is a timestamp how should we deal with that ? a boolean the
    // date it happened ? can we ?
    //    Kamon
    //      .gauge()

    System.out.println("TATATATA");

    exportSensorsMetrics(stoveStatus);
  }


  private void getPropertyValue(
      @NonNull StoveStatus stoveStatus, @NonNull final String propertyName) {
    try {
      Class<?> clazz = StoveStatus.class;

      // Get a reference to the append() method
      var getterMethodName =
          "get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
      var getterMethod = clazz.getMethod(getterMethodName);

      var result = getterMethod.invoke(stoveStatus);
      System.out.println(result);
      System.out.println(result);
      System.out.println(result);
      System.out.println(result);
    } catch (Exception ex) {
      ex.printStackTrace(); // TODO: refactor
    }
  }

  private void exportSensorsMetrics(@NonNull final StoveStatus stoveStatus) {

    System.out.println(stoveStatus.toString());

    ofNullable(stoveStatus.getSensors().getParameterRuntimePellets())
        .ifPresent(
            value ->
                Kamon.gauge("parameterRuntimePellets")
                    .withTag(STOVE_ID, stoveStatus.getStoveId())
                    .withTag(STOVE_NAME, stoveStatus.getName())
                    .update(value));

    ofNullable(stoveStatus.getSensors().getParameterRuntimeLogs())
        .ifPresent(
            value ->
                Kamon.gauge("parameterRuntimeLogs")
                    .withTag(STOVE_ID, stoveStatus.getStoveId())
                    .withTag(STOVE_NAME, stoveStatus.getName())
                    .update(value));

    ofNullable(stoveStatus.getSensors().getParameterFeedRateTotal())
        .ifPresent(
            value ->
                Kamon.gauge("parameterFeedRateTotal")
                    .withTag(STOVE_ID, stoveStatus.getStoveId())
                    .withTag(STOVE_NAME, stoveStatus.getName())
                    .update(value));

    ofNullable(stoveStatus.getSensors().getParameterFeedRateService())
        .ifPresent(
            value ->
                Kamon.gauge("parameterFeedRateService")
                    .withTag(STOVE_ID, stoveStatus.getStoveId())
                    .withTag(STOVE_NAME, stoveStatus.getName())
                    .update(value));

    ofNullable(stoveStatus.getSensors().getParameterServiceCountdownKg())
        .ifPresent(
            value ->
                // TODO: maybe should be reverse (think about)
                Kamon.gauge("parameterServiceCountdownKg")
                    .withTag(STOVE_ID, stoveStatus.getStoveId())
                    .withTag(STOVE_NAME, stoveStatus.getName())
                    .update(value));

    ofNullable(stoveStatus.getSensors().getParameterServiceCountdownTime())
        .ifPresent(
            value ->
                Kamon.gauge("parameterServiceCountdownTime")
                    .withTag(STOVE_ID, stoveStatus.getStoveId())
                    .withTag(STOVE_NAME, stoveStatus.getName())
                    .update(value));

    ofNullable(stoveStatus.getSensors().getParameterIgnitionCount())
        .ifPresent(
            value ->
                Kamon.gauge("parameterIgnitionCount")
                    .withTag(STOVE_ID, stoveStatus.getStoveId())
                    .withTag(STOVE_NAME, stoveStatus.getName())
                    .update(value));

    ofNullable(stoveStatus.getSensors().getParameterOnOffCycleCount())
        .ifPresent(
            value ->
                Kamon.gauge("parameterOnOffCycleCount")
                    .withTag(STOVE_ID, stoveStatus.getStoveId())
                    .withTag(STOVE_NAME, stoveStatus.getName())
                    .update(value));

    ofNullable(stoveStatus.getSensors().getParameterFlameSensorOffset())
        .ifPresent(
            value ->
                Kamon.gauge("parameterFlameSensorOffset")
                    .withTag(STOVE_ID, stoveStatus.getStoveId())
                    .withTag(STOVE_NAME, stoveStatus.getName())
                    .update(value));

    ofNullable(stoveStatus.getSensors().getParameterPressureSensorOffset())
        .ifPresent(
            value ->
                Kamon.gauge("parameterPressureSensorOffset")
                    .withTag(STOVE_ID, stoveStatus.getStoveId())
                    .withTag(STOVE_NAME, stoveStatus.getName())
                    .update(value));

    // TODO: as these values rarely changes see how they get stored (not that we store one value
    // each minute that would consume disk for no value

    ofNullable(stoveStatus.getSensors().getParameterSpiralMotorsTuning())
        .ifPresent(
            value ->
                Kamon.gauge("parameterSpiralMotorsTuning")
                    .withTag(STOVE_ID, stoveStatus.getStoveId())
                    .withTag(STOVE_NAME, stoveStatus.getName())
                    .update(value));

    ofNullable(stoveStatus.getSensors().getParameterIdFanTuning())
        .ifPresent(
            value ->
                Kamon.gauge("parameterIdFanTuning")
                    .withTag(STOVE_ID, stoveStatus.getStoveId())
                    .withTag(STOVE_NAME, stoveStatus.getName())
                    .update(value));

    ofNullable(stoveStatus.getSensors().getParameterCleanIntervalBig())
        .ifPresent(
            value ->
                Kamon.gauge("parameterCleanIntervalBig")
                    .withTag(STOVE_ID, stoveStatus.getStoveId())
                    .withTag(STOVE_NAME, stoveStatus.getName())
                    .update(value));

    ofNullable(stoveStatus.getSensors().getParameterKgTillCleaning())
        .ifPresent(
            value ->
                Kamon.gauge(
                        "parameterKgTillCleaning",
                        "Amount of Kg of pellets to burn before the stove will warn about cleaning it.")
                    .withTag(STOVE_ID, stoveStatus.getStoveId())
                    .withTag(STOVE_NAME, stoveStatus.getName())
                    .update(value));

    // misc metrics
    stoveStatus.getSensors().getParametersErrorCount()
            .forEach(
                    parameterErrorCount -> {
                      Kamon.gauge("parameterErrorCount")
                              .withTag(STOVE_ID, stoveStatus.getStoveId())
                              .withTag(STOVE_NAME, stoveStatus.getName())
                              .withTag(ERROR_NUMBER, parameterErrorCount.getNumber())
                              .update(parameterErrorCount.getValue());
                    }
            );

    stoveStatus.getSensors().getParametersDebug()
            .forEach(
                    parameterDebug -> {
                      Kamon.gauge("parameterDebug")
                              .withTag(STOVE_ID, stoveStatus.getStoveId())
                              .withTag(STOVE_NAME, stoveStatus.getName())
                              .withTag(DEBUG_NUMBER, parameterDebug.getNumber())
                              .update(parameterDebug.getValue());
                    }
            );

    ofNullable(stoveStatus.getControls().getTargetTemperature())
        .ifPresent(
            value ->
                Kamon.gauge("controlsTargetTemperature", "Target temperature defined by the user.")
                    .withTag(STOVE_ID, stoveStatus.getStoveId())
                    .withTag(STOVE_NAME, stoveStatus.getName())
                    .update(value));
  }
}
