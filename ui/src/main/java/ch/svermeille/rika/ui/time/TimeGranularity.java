package ch.svermeille.rika.ui.time;

import java.util.concurrent.TimeUnit;

/**
 * @author Sebastien Vermeille
 */
public enum TimeGranularity {
  SECONDS {
    @Override
    public long toSeconds() {
      return TimeUnit.SECONDS.toSeconds(1);
    }
  }, MINUTES {
    @Override
    public long toSeconds() {
      return TimeUnit.MINUTES.toSeconds(1);
    }
  }, HOURS {
    @Override
    public long toSeconds() {
      return TimeUnit.HOURS.toSeconds(1);
    }
  }, DAYS {
    @Override
    public long toSeconds() {
      return TimeUnit.DAYS.toSeconds(1);
    }
  }, WEEKS {
    @Override
    public long toSeconds() {
      return TimeUnit.DAYS.toSeconds(7);
    }
  }, MONTHS {
    @Override
    public long toSeconds() {
      return TimeUnit.DAYS.toSeconds(30);
    }
  }, YEARS {
    @Override
    public long toSeconds() {
      return TimeUnit.DAYS.toSeconds(365);
    }
  }, DECADES {
    @Override
    public long toSeconds() {
      return TimeUnit.DAYS.toSeconds(365 * 10);
    }
  };

  public abstract long toSeconds();
}
