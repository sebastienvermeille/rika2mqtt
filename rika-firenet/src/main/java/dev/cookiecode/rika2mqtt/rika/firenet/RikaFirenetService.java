/*
 * Copyright (c) 2023 Sebastien Vermeille and contributors.
 *
 * Use of this source code is governed by an MIT
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package dev.cookiecode.rika2mqtt.rika.firenet;

import dev.cookiecode.rika2mqtt.rika.firenet.configuration.RikaFirenetConfiguration;
import dev.cookiecode.rika2mqtt.rika.firenet.exception.CouldNotAuthenticateToRikaFirenetException;
import dev.cookiecode.rika2mqtt.rika.firenet.exception.InvalidStoveIdException;
import dev.cookiecode.rika2mqtt.rika.firenet.exception.OutdatedRevisionException;
import dev.cookiecode.rika2mqtt.rika.firenet.exception.UnableToControlRikaFirenetException;
import dev.cookiecode.rika2mqtt.rika.firenet.exception.UnableToRetrieveRikaFirenetDataException;
import dev.cookiecode.rika2mqtt.rika.firenet.model.StoveId;
import dev.cookiecode.rika2mqtt.rika.firenet.model.StoveStatus;
import dev.cookiecode.rika2mqtt.rika.firenet.model.UpdatableControls.Fields;
import java.util.List;
import java.util.Map;
import lombok.NonNull;

/**
 * @author Sebastien Vermeille
 */
public interface RikaFirenetService {

  /**
   * Get stoves ids associated to the rika account defined in {@link RikaFirenetConfiguration}
   *
   * @return list of stove ids
   */
  List<StoveId> getStoves();

  /**
   * Get status of a stove
   *
   * @param stoveId the id of the stove
   * @return the complete status of a stove
   * @throws InvalidStoveIdException                    when the given stoveId is not valid
   * @throws CouldNotAuthenticateToRikaFirenetException when could not auth to rika firenet
   * @throws UnableToRetrieveRikaFirenetDataException
   */
  StoveStatus getStatus(@NonNull final StoveId stoveId)
      throws InvalidStoveIdException, CouldNotAuthenticateToRikaFirenetException, UnableToRetrieveRikaFirenetDataException;

  /**
   * Indicate if the auth with rika-firenet is done or not.
   *
   * @return true when authenticated successfully to rika.
   */
  boolean isAuthenticated();

  /**
   * Attempt to perform a login to rika-firenet using given credentials.
   *
   * @param email    rika account email (username)
   * @param password rika account password
   * @return true when the given credentials are valid.
   */
  boolean isValidCredentials(final String email, final String password);

  /**
   * Update stove controls (such as target temperature, on/off, heating mode, multiair, etc.)
   *
   * @param stoveId Id of the stove to perform update on
   * @param fields  You can provide any of these {@link Fields} and rika2mqtt will manage to do the rest.
   * @throws UnableToControlRikaFirenetException
   */
  void updateControls(@NonNull final StoveId stoveId, Map<String, String> fields)
      throws UnableToControlRikaFirenetException, InvalidStoveIdException, OutdatedRevisionException;
}
