package ch.svermeille.rika.firenet.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * @author Sebastien Vermeille
 */
@Data
public class Sensors {

  private Double inputRoomTemperature;
  private Integer inputFlameTemperature;
  private Integer inputBakeTemperature;
  private Integer statusError;
  private Integer statusSubError;
  private Integer statusWarning;
  private Integer statusService;
  private Integer outputDischargeMotor;
  private Integer outputDischargeCurrent;

  @SerializedName("outputIDFan")  // for coherence (the rest of the api is using camelCase)
  private Integer outputIdFan;

  @SerializedName("outputIDFanTarget")  // for coherence (the rest of the api is using camelCase)
  private Integer outputIdFanTarget;
  private Integer outputInsertionMotor;
  private Integer outputInsertionCurrent;
  private Integer outputAirFlaps;
  private Integer outputAirFlapsTargetPosition;
  private Boolean outputBurnBackFlapMagnet;
  private Boolean outputGridMotor;
  private Boolean outputIgnition;
  private Boolean inputUpperTemperatureLimiter;
  private Boolean inputPressureSwitch;
  private Integer inputPressureSensor;
  private Boolean inputGridContact;
  private Boolean inputDoor;
  private Boolean inputCover;
  private Boolean inputExternalRequest;
  private Boolean inputBurnBackFlapSwitch;
  private Boolean inputFlueGasFlapSwitch;
  private Double inputBoardTemperature;
  private Integer inputCurrentStage;

  @SerializedName("inputTargetStagePID")  // for coherence (the rest of the api is using camelCase)
  private Integer inputTargetStagePid;

  @SerializedName("inputCurrentStagePID")  // for coherence (the rest of the api is using camelCase)
  private Integer inputCurrentStagePid;
  private Integer statusMainState;
  private Integer statusSubState;
  private Integer statusWifiStrength;
  private Boolean parameterEcoModePossible;
  private Integer parameterFabricationNumber;
  private Integer parameterStoveTypeNumber;
  private Integer parameterLanguageNumber;
  private Integer parameterVersionMainBoard;

  @SerializedName("parameterVersionTFT")  // for coherence (the rest of the api is using camelCase)
  private Integer parameterVersionTft;

  @SerializedName("parameterVersionWiFi")  // for coherence (the rest of the api use Wifi not WiFi)
  private Integer parameterVersionWifi;
  private Integer parameterVersionMainBoardBootLoader;

  @SerializedName("parameterVersionTFTBootLoader")
  // for coherence (the rest of the api is using camelCase)
  private Integer parameterVersionTftBootLoader;

  @SerializedName("parameterVersionWiFiBootLoader")
  // for coherence (the rest of the api is using camelCase)
  private Integer parameterVersionWifiBootLoader;
  private Integer parameterVersionMainBoardSub;

  @SerializedName("parameterVersionTFTSub")
  // for coherence (the rest of the api is using camelCase)
  private Integer parameterVersionTftSub;

  @SerializedName("parameterVersionWiFiSub")
  // for coherence (the rest of the api use Wifi not WiFi)
  private Integer parameterVersionWifiSub;
  private Integer parameterRuntimePellets;
  private Integer parameterRuntimeLogs;
  private Integer parameterFeedRateTotal;
  private Integer parameterFeedRateService;
  private Integer parameterServiceCountdownKg;
  private Integer parameterServiceCountdownTime;
  private Integer parameterIgnitionCount;
  private Integer parameterOnOffCycleCount;
  private Integer parameterFlameSensorOffset;
  private Integer parameterPressureSensorOffset;
  private Integer parameterErrorCount0;
  private Integer parameterErrorCount1;
  private Integer parameterErrorCount2;
  private Integer parameterErrorCount3;
  private Integer parameterErrorCount4;
  private Integer parameterErrorCount5;
  private Integer parameterErrorCount6;
  private Integer parameterErrorCount7;
  private Integer parameterErrorCount8;
  private Integer parameterErrorCount9;
  private Integer parameterErrorCount10;
  private Integer parameterErrorCount11;
  private Integer parameterErrorCount12;
  private Integer parameterErrorCount13;
  private Integer parameterErrorCount14;
  private Integer parameterErrorCount15;
  private Integer parameterErrorCount16;
  private Integer parameterErrorCount17;
  private Integer parameterErrorCount18;
  private Integer parameterErrorCount19;
  private Boolean statusHeatingTimesNotProgrammed;
  private Boolean statusFrostStarted;
  private Integer parameterSpiralMotorsTuning;

  @SerializedName("parameterIDFanTuning")  // for coherence (the rest of the api is using camelCase)
  private Integer parameterIdFanTuning;
  private Integer parameterCleanIntervalBig;
  private Integer parameterKgTillCleaning;
  private Integer parameterDebug0;
  private Integer parameterDebug1;
  private Integer parameterDebug2;
  private Integer parameterDebug3;
  private Integer parameterDebug4;
}
