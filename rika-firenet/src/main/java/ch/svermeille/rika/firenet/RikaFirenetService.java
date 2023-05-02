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
