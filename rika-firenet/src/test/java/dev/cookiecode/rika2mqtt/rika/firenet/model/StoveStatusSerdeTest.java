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
package dev.cookiecode.rika2mqtt.rika.firenet.model;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Test class
 *
 * @author Sebastien Vermeille
 */
@SpringBootTest(
    classes = {
      Gson.class,
    })
class StoveStatusSerdeTest {

  static final String UNDESIRED_STOVE_ID_NAMING = "stoveID";
  static final String DESIRED_STOVE_ID_NAMING = "stoveId";
  @Autowired Gson gson;

  @Test
  void serializationOfStoveStatusToJsonShouldNotPropagateUppercaseId() {
    // GIVEN
    var status = StoveStatus.builder().stoveId(12L).build();

    // WHEN
    var jsonResult = gson.toJson(status);

    // THEN
    assertThat(jsonResult).doesNotContain(UNDESIRED_STOVE_ID_NAMING);
    assertThat(jsonResult).contains(DESIRED_STOVE_ID_NAMING);
  }

  @Test
  void serializationOfRikaFirenetResponseToJsonShouldNotPropagateUppercaseId() {
    // GIVEN
    // note: contains stoveID <-- ID not Id which violates camel case
    String rikaFirenetResponse =
        """
                {
                  "name": "My great stove",
                  "stoveID": 42,
                  "lastSeenMinutes": 0,
                  "lastConfirmedRevision": 1691688314,
                  "oem": "RIKA",
                  "stoveType": "PARO",
                  "sensors": {
                    "inputRoomTemperature": 22.4,
                    "inputFlameTemperature": 23,
                    "inputBakeTemperature": 1024,
                    "statusError": 0,
                    "statusSubError": 0,
                    "statusWarning": 0,
                    "statusService": 0,
                    "outputDischargeMotor": 0,
                    "outputDischargeCurrent": 0,
                    "outputIDFan": 0,
                    "outputIDFanTarget": 0,
                    "outputInsertionMotor": 0,
                    "outputInsertionCurrent": 0,
                    "outputAirFlaps": 50,
                    "outputAirFlapsTargetPosition": 50,
                    "outputBurnBackFlapMagnet": true,
                    "outputGridMotor": false,
                    "outputIgnition": false,
                    "inputUpperTemperatureLimiter": true,
                    "inputPressureSwitch": false,
                    "inputPressureSensor": 0,
                    "inputGridContact": true,
                    "inputDoor": true,
                    "inputCover": true,
                    "inputExternalRequest": true,
                    "inputBurnBackFlapSwitch": true,
                    "inputFlueGasFlapSwitch": true,
                    "inputBoardTemperature": 3.4,
                    "inputCurrentStage": 68,
                    "inputTargetStagePID": 67,
                    "inputCurrentStagePID": 68,
                    "statusMainState": 1,
                    "statusSubState": 3,
                    "statusWifiStrength": -69,
                    "parameterEcoModePossible": false,
                    "parameterFabricationNumber": 1,
                    "parameterStoveTypeNumber": 17,
                    "parameterLanguageNumber": 2,
                    "parameterVersionMainBoard": 228,
                    "parameterVersionTFT": 228,
                    "parameterVersionWiFi": 112,
                    "parameterVersionMainBoardBootLoader": 160,
                    "parameterVersionTFTBootLoader": 150,
                    "parameterVersionWiFiBootLoader": 101,
                    "parameterVersionMainBoardSub": 52003,
                    "parameterVersionTFTSub": 50002,
                    "parameterVersionWiFiSub": 13301,
                    "parameterRuntimePellets": 4528,
                    "parameterRuntimeLogs": 657,
                    "parameterFeedRateTotal": 6263,
                    "parameterFeedRateService": 598,
                    "parameterServiceCountdownKg": 102,
                    "parameterServiceCountdownTime": 0,
                    "parameterIgnitionCount": 1228,
                    "parameterOnOffCycleCount": 63,
                    "parameterFlameSensorOffset": 14,
                    "parameterPressureSensorOffset": 0,
                    "parameterErrorCount0": 93,
                    "parameterErrorCount1": 193,
                    "parameterErrorCount2": 0,
                    "parameterErrorCount3": 47,
                    "parameterErrorCount4": 9,
                    "parameterErrorCount5": 1,
                    "parameterErrorCount6": 0,
                    "parameterErrorCount7": 0,
                    "parameterErrorCount8": 0,
                    "parameterErrorCount9": 3,
                    "parameterErrorCount10": 3,
                    "parameterErrorCount11": 0,
                    "parameterErrorCount12": 3,
                    "parameterErrorCount13": 0,
                    "parameterErrorCount14": 0,
                    "parameterErrorCount15": 0,
                    "parameterErrorCount16": 0,
                    "parameterErrorCount17": 0,
                    "parameterErrorCount18": 1,
                    "parameterErrorCount19": 0,
                    "statusHeatingTimesNotProgrammed": false,
                    "statusFrostStarted": false,
                    "parameterSpiralMotorsTuning": 0,
                    "parameterIDFanTuning": 0,
                    "parameterCleanIntervalBig": 240,
                    "parameterKgTillCleaning": 700,
                    "parameterDebug0": 0,
                    "parameterDebug1": 0,
                    "parameterDebug2": 0,
                    "parameterDebug3": 0,
                    "parameterDebug4": 0
                  },
                  "controls": {
                    "revision": 1691688314,
                    "onOff": true,
                    "operatingMode": 2,
                    "heatingPower": 70,
                    "targetTemperature": 19,
                    "bakeTemperature": 1024,
                    "ecoMode": false,
                    "heatingTimeMon1": "06451000",
                    "heatingTimeMon2": "14002230",
                    "heatingTimeTue1": "06451000",
                    "heatingTimeTue2": "14002230",
                    "heatingTimeWed1": "06451000",
                    "heatingTimeWed2": "14002230",
                    "heatingTimeThu1": "06451000",
                    "heatingTimeThu2": "14002230",
                    "heatingTimeFri1": "06451000",
                    "heatingTimeFri2": "14002230",
                    "heatingTimeSat1": "06451000",
                    "heatingTimeSat2": "14002230",
                    "heatingTimeSun1": "06451000",
                    "heatingTimeSun2": "14002230",
                    "heatingTimesActiveForComfort": false,
                    "setBackTemperature": 13,
                    "convectionFan1Active": false,
                    "convectionFan1Level": 0,
                    "convectionFan1Area": 12,
                    "convectionFan2Active": true,
                    "convectionFan2Level": 0,
                    "convectionFan2Area": 12,
                    "frostProtectionActive": true,
                    "frostProtectionTemperature": 10,
                    "temperatureOffset": -1,
                    "RoomPowerRequest": 3,
                    "debug0": 0,
                    "debug1": 0,
                    "debug2": 0,
                    "debug3": 0,
                    "debug4": 0
                  }
                }
         """;

    var deserializedStatus = gson.fromJson(rikaFirenetResponse, StoveStatus.class);

    // WHEN
    var serializedStatus = gson.toJson(deserializedStatus);

    // THEN
    assertThat(serializedStatus).doesNotContain(UNDESIRED_STOVE_ID_NAMING);
    assertThat(serializedStatus).contains(DESIRED_STOVE_ID_NAMING);
  }
}