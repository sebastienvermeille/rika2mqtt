package ch.svermeille.rika.log.viewer.service;

import ch.qos.logback.classic.Level;
import ch.svermeille.rika.log.viewer.model.LogEntry;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.io.input.ReversedLinesFileReader;
import org.springframework.stereotype.Service;

/**
 * @author Sebastien Vermeille
 */
@Service
public class LogViewerServiceImpl implements LogViewerService {

  static final String LOG_FILE_NAME = "./logs/rika2mqtt.log";

  @Override
  public List<LogEntry> getLastLines(final int amount, final Set<Level> excludeLevels) {
    try {
      final ReversedLinesFileReader fileReader = new ReversedLinesFileReader(new File(LOG_FILE_NAME), Charset.forName("UTF-8"));
      final AtomicInteger wholeLogCounter = new AtomicInteger();
      final List<LogEntry> results = new ArrayList<>();
      var tooLong = false;
      final var startedAt = Instant.now();
      while(wholeLogCounter.get() < amount && !tooLong) {
        final String line = fileReader.readLine();
        if(line == null) {
          break;
        }
        final var logEntry = new LogEntry(line);
        if(!logEntry.isCompanionLog()) {
          logEntry.getLevel().ifPresent(lvl -> {
            if(!excludeLevels.contains(lvl) && !logEntry.getMessage().trim().isEmpty()) {
              results.add(logEntry);
              wholeLogCounter.getAndIncrement();
            }
          });
        }


        if(startedAt.plusSeconds(10).isBefore(Instant.now())) {
          tooLong = true;
        }
      }
      return results;
    } catch(final IOException e) {
      throw new RuntimeException(e);
    }
  }
}
