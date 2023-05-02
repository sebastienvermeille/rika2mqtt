/*
 * Copyright (c) 2023 Sebastien Vermeille and contributors.
 *
 * Use of this source code is governed by an MIT
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package ch.svermeille.rika.firenet;

import ch.svermeille.rika.firenet.exception.CouldNotAuthenticateToRikaFirenetException;
import ch.svermeille.rika.firenet.exception.InvalidStoveIdException;
import ch.svermeille.rika.firenet.exception.UnableToRetrieveRikaFirenetDataException;
import ch.svermeille.rika.firenet.model.StoveId;
import ch.svermeille.rika.firenet.model.StoveStatus;
import java.util.List;
import lombok.NonNull;

/**
 * @author Sebastien Vermeille
 */
public interface RikaFirenetService {

  List<StoveId> getStoves();

  StoveStatus getStatus(@NonNull final StoveId stoveId) throws InvalidStoveIdException, CouldNotAuthenticateToRikaFirenetException, UnableToRetrieveRikaFirenetDataException;

  boolean isAuthenticated();

  boolean isValidCredentials(final String email, final String password);
}
