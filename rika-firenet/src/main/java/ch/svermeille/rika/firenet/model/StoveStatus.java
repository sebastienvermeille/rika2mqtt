/*
 * Copyright (c) 2023 Sebastien Vermeille and contributors.
 *
 * Use of this source code is governed by an MIT
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package ch.svermeille.rika.firenet.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * @author Sebastien Vermeille
 */
@Data
public class StoveStatus {

  private String name;

  @SerializedName("stoveID")  // for coherence (the rest of the api is using camelCase)
  private Long stoveId;

  private Long lastSeenMinutes;
  private Long lastConfirmedRevision;
  private String oem;
  private String stoveType;
  private Sensors sensors;
  private Controls controls;

}
