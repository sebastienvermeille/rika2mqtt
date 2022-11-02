package ch.svermeille.rika.ui.time;

import static java.time.ZoneOffset.UTC;

import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

/**
 * @author Sebastien Vermeille
 */
@Component
public class TimeAgoHelper {

  public String evalAsHumanFriendlyTimeAgo(final LocalDateTime pastMoment) {
    if(pastMoment.isAfter(LocalDateTime.now(UTC))) {
      throw new IllegalArgumentException(String.format("pastMoment has to be in the past. (received: %s)", pastMoment));
    }

    final long timeDifferenceInSeconds = LocalDateTime.now(UTC).toEpochSecond(UTC) - pastMoment.toEpochSecond(UTC);
    if(timeDifferenceInSeconds / TimeGranularity.DECADES.toSeconds() > 0) {
      final var delta = timeDifferenceInSeconds / TimeGranularity.DECADES.toSeconds();
      return String.format("%s decades ago", delta);
    } else if(timeDifferenceInSeconds / TimeGranularity.YEARS.toSeconds() > 0) {
      final var delta = timeDifferenceInSeconds / TimeGranularity.YEARS.toSeconds();
      return String.format("%s years ago", delta);
    } else if(timeDifferenceInSeconds / TimeGranularity.MONTHS.toSeconds() > 0) {
      final var delta = timeDifferenceInSeconds / TimeGranularity.MONTHS.toSeconds();
      return String.format("%s months ago", delta);
    } else if(timeDifferenceInSeconds / TimeGranularity.WEEKS.toSeconds() > 0) {
      final var delta = timeDifferenceInSeconds / TimeGranularity.WEEKS.toSeconds();
      return String.format("%s weeks ago", delta);
    } else if(timeDifferenceInSeconds / TimeGranularity.DAYS.toSeconds() > 0) {
      final var delta = timeDifferenceInSeconds / TimeGranularity.DAYS.toSeconds();
      return String.format("%s days ago", delta);
    } else if(timeDifferenceInSeconds / TimeGranularity.HOURS.toSeconds() > 0) {
      final var delta = timeDifferenceInSeconds / TimeGranularity.HOURS.toSeconds();
      return String.format("%s hours ago", delta);
    } else if(timeDifferenceInSeconds / TimeGranularity.MINUTES.toSeconds() > 0) {
      final var delta = timeDifferenceInSeconds / TimeGranularity.MINUTES.toSeconds();
      return String.format("%s minutes ago", delta);
    } else {
      return "moments ago";
    }
  }

}
