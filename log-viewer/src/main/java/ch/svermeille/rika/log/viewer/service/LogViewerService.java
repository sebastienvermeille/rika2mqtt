package ch.svermeille.rika.log.viewer.service;

import ch.qos.logback.classic.Level;
import ch.svermeille.rika.log.viewer.model.LogEntry;
import java.util.List;
import java.util.Set;

/**
 * @author Sebastien Vermeille
 */
public interface LogViewerService {

  List<LogEntry> getLastLines(final int amount, Set<Level> excludeLevels);
}
