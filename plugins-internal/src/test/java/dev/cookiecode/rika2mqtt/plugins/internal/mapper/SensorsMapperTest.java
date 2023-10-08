package dev.cookiecode.rika2mqtt.plugins.internal.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import dev.cookiecode.rika2mqtt.rika.firenet.model.Sensors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {SensorsMapperImpl.class})
class SensorsMapperTest {

  @Autowired private SensorsMapper mapper;

  @Test
  void toApiSensorsShouldFillAllSensorsPropertiesGivenACompleteRikaFirenetSensorInstance() {

    // GIVEN
    Integer inputFlameTemperature = 200;
    Integer inputBakeTemperature = 300;
    Integer statusError = 1;
    Integer statusSubError = 2;
    Integer statusWarning = 3;
    Integer statusService = 4;
    Integer outputDischargeMotor = 5;
    Integer outputDischargeCurrent = 6;
    Integer outputIdFan = 7;
    Integer outputIdFanTarget = 8;
    Integer outputInsertionMotor = 9;
    Integer outputInsertionCurrent = 10;
    Integer outputAirFlaps = 11;
    Integer outputAirFlapsTargetPosition = 12;
    Boolean outputBurnBackFlapMagnet = true;
    Boolean outputGridMotor = false;
    Boolean outputIgnition = true;
    Boolean inputUpperTemperatureLimiter = true;
    Boolean inputPressureSwitch = true;
    Integer inputPressureSensor = 13;
    Boolean inputGridContact = true;
    Boolean inputDoor = true;
    Boolean inputCover = true;
    Boolean inputExternalRequest = true;
    Boolean inputBurnBackFlapSwitch = true;
    Integer parameterDebug0 = 44;
    Integer parameterDebug1 = 45;
    Integer parameterDebug2 = 46;
    Integer parameterDebug3 = 47;
    Integer parameterDebug4 = 48;
    Integer parameterErrorCount0 = 30;
    Integer parameterErrorCount1 = 31;
    Integer parameterErrorCount2 = 32;
    Integer parameterErrorCount3 = 33;
    Integer parameterErrorCount4 = 34;
    Integer parameterErrorCount5 = 35;
    Integer parameterErrorCount6 = 36;
    Integer parameterErrorCount7 = 37;
    Integer parameterErrorCount8 = 38;
    Integer parameterErrorCount9 = 39;
    Integer parameterErrorCount10 = 40;
    Integer parameterErrorCount11 = 41;
    Integer parameterErrorCount12 = 42;
    Integer parameterErrorCount13 = 43;
    Integer parameterErrorCount14 = 44;
    Integer parameterErrorCount15 = 45;
    Integer parameterErrorCount16 = 46;
    Integer parameterErrorCount17 = 47;
    Integer parameterErrorCount18 = 48;
    Integer parameterErrorCount19 = 49;
    Boolean inputFlueGasFlapSwitch = true;
    Double inputBoardTemperature = 20.20;
    Integer inputCurrentStage = 12;
    Integer statusMainState = 42;
    Integer statusSubState = 43;
    Integer statusWifiStrength = 100;
    Boolean parameterEcoModePossible = true;
    Integer parameterFabricationNumber = 12323;
    Integer parameterStoveTypeNumber = 46;
    Integer parameterLanguageNumber = 2;
    Integer parameterVersionMainBoard = 5;
    Integer parameterVersionTft = 6;
    Integer parameterVersionWifi = 7;
    Integer parameterVersionMainBoardBootLoader = 8;
    Integer parameterVersionTftBootLoader = 9;
    Integer parameterVersionWifiBootLoader = 10;
    Integer parameterVersionMainBoardSub = 11;
    Integer inputTargetStagePid = 12;
    Integer inputCurrentStagePid = 13;
    Integer parameterVersionTftSub = 14;
    Integer parameterVersionWifiSub = 15;
    Integer parameterRuntimePellets = 16;
    Integer parameterRuntimeLogs = 17;
    Integer parameterFeedRateTotal = 18;
    Integer parameterFeedRateService = 19;
    Integer parameterServiceCountdownKg = 20;
    Integer parameterServiceCountdownTime = 21;
    Integer parameterIgnitionCount = 22;
    Integer parameterOnOffCycleCount = 23;
    Integer parameterFlameSensorOffset = 24;
    Integer parameterPressureSensorOffset = 25;
    Integer parameterKgTillCleaning = 26;
    Integer parameterCleanIntervalBig = 27;
    Integer parameterIdFanTuning = 28;
    Integer parameterSpiralMotorsTuning = 29;
    Boolean statusFrostStarted = true;
    Boolean statusHeatingTimesNotProgrammed = true;
    Double inputRoomTemperature = 22.4;
    Sensors rikaFirenetSensors =
        Sensors.builder()
            .inputRoomTemperature(inputRoomTemperature)
            .inputFlameTemperature(inputFlameTemperature)
            .inputBakeTemperature(inputBakeTemperature)
            .statusError(statusError)
            .statusSubError(statusSubError)
            .statusWarning(statusWarning)
            .statusService(statusService)
            .outputDischargeMotor(outputDischargeMotor)
            .outputDischargeCurrent(outputDischargeCurrent)
            .outputIdFan(outputIdFan)
            .outputIdFanTarget(outputIdFanTarget)
            .outputInsertionMotor(outputInsertionMotor)
            .outputInsertionCurrent(outputInsertionCurrent)
            .outputAirFlaps(outputAirFlaps)
            .outputAirFlapsTargetPosition(outputAirFlapsTargetPosition)
            .outputBurnBackFlapMagnet(outputBurnBackFlapMagnet)
            .outputGridMotor(outputGridMotor)
            .outputIgnition(outputIgnition)
            .inputUpperTemperatureLimiter(inputUpperTemperatureLimiter)
            .inputPressureSwitch(inputPressureSwitch)
            .inputPressureSensor(inputPressureSensor)
            .inputGridContact(inputGridContact)
            .inputDoor(inputDoor)
            .inputCover(inputCover)
            .inputExternalRequest(inputExternalRequest)
            .inputBurnBackFlapSwitch(inputBurnBackFlapSwitch)
            .inputFlueGasFlapSwitch(inputFlueGasFlapSwitch)
            .inputBoardTemperature(inputBoardTemperature)
            .inputCurrentStage(inputCurrentStage)
            .statusMainState(statusMainState)
            .statusSubState(statusSubState)
            .statusWifiStrength(statusWifiStrength)
            .parameterEcoModePossible(parameterEcoModePossible)
            .parameterFabricationNumber(parameterFabricationNumber)
            .parameterStoveTypeNumber(parameterStoveTypeNumber)
            .parameterLanguageNumber(parameterLanguageNumber)
            .parameterVersionMainBoard(parameterVersionMainBoard)
            .parameterVersionTft(parameterVersionTft)
            .parameterVersionWifi(parameterVersionWifi)
            .parameterVersionMainBoardBootLoader(parameterVersionMainBoardBootLoader)
            .parameterVersionTftBootLoader(parameterVersionTftBootLoader)
            .parameterVersionWifiBootLoader(parameterVersionWifiBootLoader)
            .parameterVersionMainBoardSub(parameterVersionMainBoardSub)
            .inputTargetStagePid(inputTargetStagePid)
            .inputCurrentStagePid(inputCurrentStagePid)
            .parameterVersionTftSub(parameterVersionTftSub)
            .parameterVersionWifiSub(parameterVersionWifiSub)
            .parameterRuntimePellets(parameterRuntimePellets)
            .parameterRuntimeLogs(parameterRuntimeLogs)
            .parameterFeedRateTotal(parameterFeedRateTotal)
            .parameterFeedRateService(parameterFeedRateService)
            .parameterServiceCountdownKg(parameterServiceCountdownKg)
            .parameterServiceCountdownTime(parameterServiceCountdownTime)
            .parameterIgnitionCount(parameterIgnitionCount)
            .parameterOnOffCycleCount(parameterOnOffCycleCount)
            .parameterFlameSensorOffset(parameterFlameSensorOffset)
            .parameterPressureSensorOffset(parameterPressureSensorOffset)
            .parameterKgTillCleaning(parameterKgTillCleaning)
            .parameterCleanIntervalBig(parameterCleanIntervalBig)
            .parameterIdFanTuning(parameterIdFanTuning)
            .parameterSpiralMotorsTuning(parameterSpiralMotorsTuning)
            .statusFrostStarted(statusFrostStarted)
            .statusHeatingTimesNotProgrammed(statusHeatingTimesNotProgrammed)
            .parameterErrorCount0(parameterErrorCount0)
            .parameterErrorCount1(parameterErrorCount1)
            .parameterErrorCount2(parameterErrorCount2)
            .parameterErrorCount3(parameterErrorCount3)
            .parameterErrorCount4(parameterErrorCount4)
            .parameterErrorCount5(parameterErrorCount5)
            .parameterErrorCount6(parameterErrorCount6)
            .parameterErrorCount7(parameterErrorCount7)
            .parameterErrorCount8(parameterErrorCount8)
            .parameterErrorCount9(parameterErrorCount9)
            .parameterErrorCount10(parameterErrorCount10)
            .parameterErrorCount11(parameterErrorCount11)
            .parameterErrorCount12(parameterErrorCount12)
            .parameterErrorCount13(parameterErrorCount13)
            .parameterErrorCount14(parameterErrorCount14)
            .parameterErrorCount15(parameterErrorCount15)
            .parameterErrorCount16(parameterErrorCount16)
            .parameterErrorCount17(parameterErrorCount17)
            .parameterErrorCount18(parameterErrorCount18)
            .parameterErrorCount19(parameterErrorCount19)
            .parameterDebug0(parameterDebug0)
            .parameterDebug1(parameterDebug1)
            .parameterDebug2(parameterDebug2)
            .parameterDebug3(parameterDebug3)
            .parameterDebug4(parameterDebug4)
            .build();

    // WHEN
    var apiSensors = mapper.toApiSensors(rikaFirenetSensors);

    // THEN
    assertThat(apiSensors).hasNoNullFieldsOrProperties();
  }

  @Test
  void toApiSensorsShouldFillParametersDebugCollectionExtraFieldProperly() {

    // GIVEN
    Integer inputFlameTemperature = 200;
    Integer inputBakeTemperature = 300;
    Integer statusError = 1;
    Integer statusSubError = 2;
    Integer statusWarning = 3;
    Integer statusService = 4;
    Integer outputDischargeMotor = 5;
    Integer outputDischargeCurrent = 6;
    Integer outputIdFan = 7;
    Integer outputIdFanTarget = 8;
    Integer outputInsertionMotor = 9;
    Integer outputInsertionCurrent = 10;
    Integer outputAirFlaps = 11;
    Integer outputAirFlapsTargetPosition = 12;
    Boolean outputBurnBackFlapMagnet = true;
    Boolean outputGridMotor = false;
    Boolean outputIgnition = true;
    Boolean inputUpperTemperatureLimiter = true;
    Boolean inputPressureSwitch = true;
    Integer inputPressureSensor = 13;
    Boolean inputGridContact = true;
    Boolean inputDoor = true;
    Boolean inputCover = true;
    Boolean inputExternalRequest = true;
    Boolean inputBurnBackFlapSwitch = true;
    Integer parameterDebug0 = 44;
    Integer parameterDebug1 = 45;
    Integer parameterDebug2 = 46;
    Integer parameterDebug3 = 47;
    Integer parameterDebug4 = 48;
    Integer parameterErrorCount0 = 30;
    Integer parameterErrorCount1 = 31;
    Integer parameterErrorCount2 = 32;
    Integer parameterErrorCount3 = 33;
    Integer parameterErrorCount4 = 34;
    Integer parameterErrorCount5 = 35;
    Integer parameterErrorCount6 = 36;
    Integer parameterErrorCount7 = 37;
    Integer parameterErrorCount8 = 38;
    Integer parameterErrorCount9 = 39;
    Integer parameterErrorCount10 = 40;
    Integer parameterErrorCount11 = 41;
    Integer parameterErrorCount12 = 42;
    Integer parameterErrorCount13 = 43;
    Integer parameterErrorCount14 = 44;
    Integer parameterErrorCount15 = 45;
    Integer parameterErrorCount16 = 46;
    Integer parameterErrorCount17 = 47;
    Integer parameterErrorCount18 = 48;
    Integer parameterErrorCount19 = 49;
    Boolean inputFlueGasFlapSwitch = true;
    Double inputBoardTemperature = 20.20;
    Integer inputCurrentStage = 12;
    Integer statusMainState = 42;
    Integer statusSubState = 43;
    Integer statusWifiStrength = 100;
    Boolean parameterEcoModePossible = true;
    Integer parameterFabricationNumber = 12323;
    Integer parameterStoveTypeNumber = 46;
    Integer parameterLanguageNumber = 2;
    Integer parameterVersionMainBoard = 5;
    Integer parameterVersionTft = 6;
    Integer parameterVersionWifi = 7;
    Integer parameterVersionMainBoardBootLoader = 8;
    Integer parameterVersionTftBootLoader = 9;
    Integer parameterVersionWifiBootLoader = 10;
    Integer parameterVersionMainBoardSub = 11;
    Integer inputTargetStagePid = 12;
    Integer inputCurrentStagePid = 13;
    Integer parameterVersionTftSub = 14;
    Integer parameterVersionWifiSub = 15;
    Integer parameterRuntimePellets = 16;
    Integer parameterRuntimeLogs = 17;
    Integer parameterFeedRateTotal = 18;
    Integer parameterFeedRateService = 19;
    Integer parameterServiceCountdownKg = 20;
    Integer parameterServiceCountdownTime = 21;
    Integer parameterIgnitionCount = 22;
    Integer parameterOnOffCycleCount = 23;
    Integer parameterFlameSensorOffset = 24;
    Integer parameterPressureSensorOffset = 25;
    Integer parameterKgTillCleaning = 26;
    Integer parameterCleanIntervalBig = 27;
    Integer parameterIdFanTuning = 28;
    Integer parameterSpiralMotorsTuning = 29;
    Boolean statusFrostStarted = true;
    Boolean statusHeatingTimesNotProgrammed = true;
    Double inputRoomTemperature = 22.4;
    Sensors rikaFirenetSensors =
        Sensors.builder()
            .inputRoomTemperature(inputRoomTemperature)
            .inputFlameTemperature(inputFlameTemperature)
            .inputBakeTemperature(inputBakeTemperature)
            .statusError(statusError)
            .statusSubError(statusSubError)
            .statusWarning(statusWarning)
            .statusService(statusService)
            .outputDischargeMotor(outputDischargeMotor)
            .outputDischargeCurrent(outputDischargeCurrent)
            .outputIdFan(outputIdFan)
            .outputIdFanTarget(outputIdFanTarget)
            .outputInsertionMotor(outputInsertionMotor)
            .outputInsertionCurrent(outputInsertionCurrent)
            .outputAirFlaps(outputAirFlaps)
            .outputAirFlapsTargetPosition(outputAirFlapsTargetPosition)
            .outputBurnBackFlapMagnet(outputBurnBackFlapMagnet)
            .outputGridMotor(outputGridMotor)
            .outputIgnition(outputIgnition)
            .inputUpperTemperatureLimiter(inputUpperTemperatureLimiter)
            .inputPressureSwitch(inputPressureSwitch)
            .inputPressureSensor(inputPressureSensor)
            .inputGridContact(inputGridContact)
            .inputDoor(inputDoor)
            .inputCover(inputCover)
            .inputExternalRequest(inputExternalRequest)
            .inputBurnBackFlapSwitch(inputBurnBackFlapSwitch)
            .inputFlueGasFlapSwitch(inputFlueGasFlapSwitch)
            .inputBoardTemperature(inputBoardTemperature)
            .inputCurrentStage(inputCurrentStage)
            .statusMainState(statusMainState)
            .statusSubState(statusSubState)
            .statusWifiStrength(statusWifiStrength)
            .parameterEcoModePossible(parameterEcoModePossible)
            .parameterFabricationNumber(parameterFabricationNumber)
            .parameterStoveTypeNumber(parameterStoveTypeNumber)
            .parameterLanguageNumber(parameterLanguageNumber)
            .parameterVersionMainBoard(parameterVersionMainBoard)
            .parameterVersionTft(parameterVersionTft)
            .parameterVersionWifi(parameterVersionWifi)
            .parameterVersionMainBoardBootLoader(parameterVersionMainBoardBootLoader)
            .parameterVersionTftBootLoader(parameterVersionTftBootLoader)
            .parameterVersionWifiBootLoader(parameterVersionWifiBootLoader)
            .parameterVersionMainBoardSub(parameterVersionMainBoardSub)
            .inputTargetStagePid(inputTargetStagePid)
            .inputCurrentStagePid(inputCurrentStagePid)
            .parameterVersionTftSub(parameterVersionTftSub)
            .parameterVersionWifiSub(parameterVersionWifiSub)
            .parameterRuntimePellets(parameterRuntimePellets)
            .parameterRuntimeLogs(parameterRuntimeLogs)
            .parameterFeedRateTotal(parameterFeedRateTotal)
            .parameterFeedRateService(parameterFeedRateService)
            .parameterServiceCountdownKg(parameterServiceCountdownKg)
            .parameterServiceCountdownTime(parameterServiceCountdownTime)
            .parameterIgnitionCount(parameterIgnitionCount)
            .parameterOnOffCycleCount(parameterOnOffCycleCount)
            .parameterFlameSensorOffset(parameterFlameSensorOffset)
            .parameterPressureSensorOffset(parameterPressureSensorOffset)
            .parameterKgTillCleaning(parameterKgTillCleaning)
            .parameterCleanIntervalBig(parameterCleanIntervalBig)
            .parameterIdFanTuning(parameterIdFanTuning)
            .parameterSpiralMotorsTuning(parameterSpiralMotorsTuning)
            .statusFrostStarted(statusFrostStarted)
            .statusHeatingTimesNotProgrammed(statusHeatingTimesNotProgrammed)
            .parameterErrorCount0(parameterErrorCount0)
            .parameterErrorCount1(parameterErrorCount1)
            .parameterErrorCount2(parameterErrorCount2)
            .parameterErrorCount3(parameterErrorCount3)
            .parameterErrorCount4(parameterErrorCount4)
            .parameterErrorCount5(parameterErrorCount5)
            .parameterErrorCount6(parameterErrorCount6)
            .parameterErrorCount7(parameterErrorCount7)
            .parameterErrorCount8(parameterErrorCount8)
            .parameterErrorCount9(parameterErrorCount9)
            .parameterErrorCount10(parameterErrorCount10)
            .parameterErrorCount11(parameterErrorCount11)
            .parameterErrorCount12(parameterErrorCount12)
            .parameterErrorCount13(parameterErrorCount13)
            .parameterErrorCount14(parameterErrorCount14)
            .parameterErrorCount15(parameterErrorCount15)
            .parameterErrorCount16(parameterErrorCount16)
            .parameterErrorCount17(parameterErrorCount17)
            .parameterErrorCount18(parameterErrorCount18)
            .parameterErrorCount19(parameterErrorCount19)
            .parameterDebug0(parameterDebug0)
            .parameterDebug1(parameterDebug1)
            .parameterDebug2(parameterDebug2)
            .parameterDebug3(parameterDebug3)
            .parameterDebug4(parameterDebug4)
            .build();

    // WHEN
    var apiSensors = mapper.toApiSensors(rikaFirenetSensors);

    // THEN
    assertThat(apiSensors.getParametersDebug()).isNotEmpty();
    assertThat(apiSensors.getParametersDebug()).hasSize(5);
    assertThat(apiSensors.getParametersDebug().get(0).getValue())
        .isEqualTo(rikaFirenetSensors.getParameterDebug0());
    assertThat(apiSensors.getParametersDebug().get(1).getValue())
        .isEqualTo(rikaFirenetSensors.getParameterDebug1());
    assertThat(apiSensors.getParametersDebug().get(2).getValue())
        .isEqualTo(rikaFirenetSensors.getParameterDebug2());
    assertThat(apiSensors.getParametersDebug().get(3).getValue())
        .isEqualTo(rikaFirenetSensors.getParameterDebug3());
    assertThat(apiSensors.getParametersDebug().get(4).getValue())
        .isEqualTo(rikaFirenetSensors.getParameterDebug4());
  }

  @Test
  void toApiSensorsShouldFillParametersErrorCountCollectionExtraFieldProperly() {

    // GIVEN
    Integer inputFlameTemperature = 200;
    Integer inputBakeTemperature = 300;
    Integer statusError = 1;
    Integer statusSubError = 2;
    Integer statusWarning = 3;
    Integer statusService = 4;
    Integer outputDischargeMotor = 5;
    Integer outputDischargeCurrent = 6;
    Integer outputIdFan = 7;
    Integer outputIdFanTarget = 8;
    Integer outputInsertionMotor = 9;
    Integer outputInsertionCurrent = 10;
    Integer outputAirFlaps = 11;
    Integer outputAirFlapsTargetPosition = 12;
    Boolean outputBurnBackFlapMagnet = true;
    Boolean outputGridMotor = false;
    Boolean outputIgnition = true;
    Boolean inputUpperTemperatureLimiter = true;
    Boolean inputPressureSwitch = true;
    Integer inputPressureSensor = 13;
    Boolean inputGridContact = true;
    Boolean inputDoor = true;
    Boolean inputCover = true;
    Boolean inputExternalRequest = true;
    Boolean inputBurnBackFlapSwitch = true;
    Integer parameterDebug0 = 44;
    Integer parameterDebug1 = 45;
    Integer parameterDebug2 = 46;
    Integer parameterDebug3 = 47;
    Integer parameterDebug4 = 48;
    Integer parameterErrorCount0 = 30;
    Integer parameterErrorCount1 = 31;
    Integer parameterErrorCount2 = 32;
    Integer parameterErrorCount3 = 33;
    Integer parameterErrorCount4 = 34;
    Integer parameterErrorCount5 = 35;
    Integer parameterErrorCount6 = 36;
    Integer parameterErrorCount7 = 37;
    Integer parameterErrorCount8 = 38;
    Integer parameterErrorCount9 = 39;
    Integer parameterErrorCount10 = 40;
    Integer parameterErrorCount11 = 41;
    Integer parameterErrorCount12 = 42;
    Integer parameterErrorCount13 = 43;
    Integer parameterErrorCount14 = 44;
    Integer parameterErrorCount15 = 45;
    Integer parameterErrorCount16 = 46;
    Integer parameterErrorCount17 = 47;
    Integer parameterErrorCount18 = 48;
    Integer parameterErrorCount19 = 49;
    Boolean inputFlueGasFlapSwitch = true;
    Double inputBoardTemperature = 20.20;
    Integer inputCurrentStage = 12;
    Integer statusMainState = 42;
    Integer statusSubState = 43;
    Integer statusWifiStrength = 100;
    Boolean parameterEcoModePossible = true;
    Integer parameterFabricationNumber = 12323;
    Integer parameterStoveTypeNumber = 46;
    Integer parameterLanguageNumber = 2;
    Integer parameterVersionMainBoard = 5;
    Integer parameterVersionTft = 6;
    Integer parameterVersionWifi = 7;
    Integer parameterVersionMainBoardBootLoader = 8;
    Integer parameterVersionTftBootLoader = 9;
    Integer parameterVersionWifiBootLoader = 10;
    Integer parameterVersionMainBoardSub = 11;
    Integer inputTargetStagePid = 12;
    Integer inputCurrentStagePid = 13;
    Integer parameterVersionTftSub = 14;
    Integer parameterVersionWifiSub = 15;
    Integer parameterRuntimePellets = 16;
    Integer parameterRuntimeLogs = 17;
    Integer parameterFeedRateTotal = 18;
    Integer parameterFeedRateService = 19;
    Integer parameterServiceCountdownKg = 20;
    Integer parameterServiceCountdownTime = 21;
    Integer parameterIgnitionCount = 22;
    Integer parameterOnOffCycleCount = 23;
    Integer parameterFlameSensorOffset = 24;
    Integer parameterPressureSensorOffset = 25;
    Integer parameterKgTillCleaning = 26;
    Integer parameterCleanIntervalBig = 27;
    Integer parameterIdFanTuning = 28;
    Integer parameterSpiralMotorsTuning = 29;
    Boolean statusFrostStarted = true;
    Boolean statusHeatingTimesNotProgrammed = true;
    Double inputRoomTemperature = 22.4;
    Sensors rikaFirenetSensors =
        Sensors.builder()
            .inputRoomTemperature(inputRoomTemperature)
            .inputFlameTemperature(inputFlameTemperature)
            .inputBakeTemperature(inputBakeTemperature)
            .statusError(statusError)
            .statusSubError(statusSubError)
            .statusWarning(statusWarning)
            .statusService(statusService)
            .outputDischargeMotor(outputDischargeMotor)
            .outputDischargeCurrent(outputDischargeCurrent)
            .outputIdFan(outputIdFan)
            .outputIdFanTarget(outputIdFanTarget)
            .outputInsertionMotor(outputInsertionMotor)
            .outputInsertionCurrent(outputInsertionCurrent)
            .outputAirFlaps(outputAirFlaps)
            .outputAirFlapsTargetPosition(outputAirFlapsTargetPosition)
            .outputBurnBackFlapMagnet(outputBurnBackFlapMagnet)
            .outputGridMotor(outputGridMotor)
            .outputIgnition(outputIgnition)
            .inputUpperTemperatureLimiter(inputUpperTemperatureLimiter)
            .inputPressureSwitch(inputPressureSwitch)
            .inputPressureSensor(inputPressureSensor)
            .inputGridContact(inputGridContact)
            .inputDoor(inputDoor)
            .inputCover(inputCover)
            .inputExternalRequest(inputExternalRequest)
            .inputBurnBackFlapSwitch(inputBurnBackFlapSwitch)
            .inputFlueGasFlapSwitch(inputFlueGasFlapSwitch)
            .inputBoardTemperature(inputBoardTemperature)
            .inputCurrentStage(inputCurrentStage)
            .statusMainState(statusMainState)
            .statusSubState(statusSubState)
            .statusWifiStrength(statusWifiStrength)
            .parameterEcoModePossible(parameterEcoModePossible)
            .parameterFabricationNumber(parameterFabricationNumber)
            .parameterStoveTypeNumber(parameterStoveTypeNumber)
            .parameterLanguageNumber(parameterLanguageNumber)
            .parameterVersionMainBoard(parameterVersionMainBoard)
            .parameterVersionTft(parameterVersionTft)
            .parameterVersionWifi(parameterVersionWifi)
            .parameterVersionMainBoardBootLoader(parameterVersionMainBoardBootLoader)
            .parameterVersionTftBootLoader(parameterVersionTftBootLoader)
            .parameterVersionWifiBootLoader(parameterVersionWifiBootLoader)
            .parameterVersionMainBoardSub(parameterVersionMainBoardSub)
            .inputTargetStagePid(inputTargetStagePid)
            .inputCurrentStagePid(inputCurrentStagePid)
            .parameterVersionTftSub(parameterVersionTftSub)
            .parameterVersionWifiSub(parameterVersionWifiSub)
            .parameterRuntimePellets(parameterRuntimePellets)
            .parameterRuntimeLogs(parameterRuntimeLogs)
            .parameterFeedRateTotal(parameterFeedRateTotal)
            .parameterFeedRateService(parameterFeedRateService)
            .parameterServiceCountdownKg(parameterServiceCountdownKg)
            .parameterServiceCountdownTime(parameterServiceCountdownTime)
            .parameterIgnitionCount(parameterIgnitionCount)
            .parameterOnOffCycleCount(parameterOnOffCycleCount)
            .parameterFlameSensorOffset(parameterFlameSensorOffset)
            .parameterPressureSensorOffset(parameterPressureSensorOffset)
            .parameterKgTillCleaning(parameterKgTillCleaning)
            .parameterCleanIntervalBig(parameterCleanIntervalBig)
            .parameterIdFanTuning(parameterIdFanTuning)
            .parameterSpiralMotorsTuning(parameterSpiralMotorsTuning)
            .statusFrostStarted(statusFrostStarted)
            .statusHeatingTimesNotProgrammed(statusHeatingTimesNotProgrammed)
            .parameterErrorCount0(parameterErrorCount0)
            .parameterErrorCount1(parameterErrorCount1)
            .parameterErrorCount2(parameterErrorCount2)
            .parameterErrorCount3(parameterErrorCount3)
            .parameterErrorCount4(parameterErrorCount4)
            .parameterErrorCount5(parameterErrorCount5)
            .parameterErrorCount6(parameterErrorCount6)
            .parameterErrorCount7(parameterErrorCount7)
            .parameterErrorCount8(parameterErrorCount8)
            .parameterErrorCount9(parameterErrorCount9)
            .parameterErrorCount10(parameterErrorCount10)
            .parameterErrorCount11(parameterErrorCount11)
            .parameterErrorCount12(parameterErrorCount12)
            .parameterErrorCount13(parameterErrorCount13)
            .parameterErrorCount14(parameterErrorCount14)
            .parameterErrorCount15(parameterErrorCount15)
            .parameterErrorCount16(parameterErrorCount16)
            .parameterErrorCount17(parameterErrorCount17)
            .parameterErrorCount18(parameterErrorCount18)
            .parameterErrorCount19(parameterErrorCount19)
            .parameterDebug0(parameterDebug0)
            .parameterDebug1(parameterDebug1)
            .parameterDebug2(parameterDebug2)
            .parameterDebug3(parameterDebug3)
            .parameterDebug4(parameterDebug4)
            .build();

    // WHEN
    var apiSensors = mapper.toApiSensors(rikaFirenetSensors);

    // THEN
    assertThat(apiSensors.getParametersErrorCount()).isNotEmpty();
    assertThat(apiSensors.getParametersErrorCount()).hasSize(20);
    assertThat(apiSensors.getParametersErrorCount().get(0).getValue())
        .isEqualTo(rikaFirenetSensors.getParameterErrorCount0());
    assertThat(apiSensors.getParametersErrorCount().get(1).getValue())
        .isEqualTo(rikaFirenetSensors.getParameterErrorCount1());
    assertThat(apiSensors.getParametersErrorCount().get(2).getValue())
        .isEqualTo(rikaFirenetSensors.getParameterErrorCount2());
    assertThat(apiSensors.getParametersErrorCount().get(3).getValue())
        .isEqualTo(rikaFirenetSensors.getParameterErrorCount3());
    assertThat(apiSensors.getParametersErrorCount().get(4).getValue())
        .isEqualTo(rikaFirenetSensors.getParameterErrorCount4());
    assertThat(apiSensors.getParametersErrorCount().get(5).getValue())
        .isEqualTo(rikaFirenetSensors.getParameterErrorCount5());
    assertThat(apiSensors.getParametersErrorCount().get(6).getValue())
        .isEqualTo(rikaFirenetSensors.getParameterErrorCount6());
    assertThat(apiSensors.getParametersErrorCount().get(7).getValue())
        .isEqualTo(rikaFirenetSensors.getParameterErrorCount7());
    assertThat(apiSensors.getParametersErrorCount().get(8).getValue())
        .isEqualTo(rikaFirenetSensors.getParameterErrorCount8());
    assertThat(apiSensors.getParametersErrorCount().get(9).getValue())
        .isEqualTo(rikaFirenetSensors.getParameterErrorCount9());
    assertThat(apiSensors.getParametersErrorCount().get(10).getValue())
        .isEqualTo(rikaFirenetSensors.getParameterErrorCount10());
    assertThat(apiSensors.getParametersErrorCount().get(11).getValue())
        .isEqualTo(rikaFirenetSensors.getParameterErrorCount11());
    assertThat(apiSensors.getParametersErrorCount().get(12).getValue())
        .isEqualTo(rikaFirenetSensors.getParameterErrorCount12());
    assertThat(apiSensors.getParametersErrorCount().get(13).getValue())
        .isEqualTo(rikaFirenetSensors.getParameterErrorCount13());
    assertThat(apiSensors.getParametersErrorCount().get(14).getValue())
        .isEqualTo(rikaFirenetSensors.getParameterErrorCount14());
    assertThat(apiSensors.getParametersErrorCount().get(15).getValue())
        .isEqualTo(rikaFirenetSensors.getParameterErrorCount15());
    assertThat(apiSensors.getParametersErrorCount().get(16).getValue())
        .isEqualTo(rikaFirenetSensors.getParameterErrorCount16());
    assertThat(apiSensors.getParametersErrorCount().get(17).getValue())
        .isEqualTo(rikaFirenetSensors.getParameterErrorCount17());
    assertThat(apiSensors.getParametersErrorCount().get(18).getValue())
        .isEqualTo(rikaFirenetSensors.getParameterErrorCount18());
    assertThat(apiSensors.getParametersErrorCount().get(19).getValue())
        .isEqualTo(rikaFirenetSensors.getParameterErrorCount19());
  }
}
