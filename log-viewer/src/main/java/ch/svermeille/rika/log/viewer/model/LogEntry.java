package ch.svermeille.rika.log.viewer.model;

import ch.qos.logback.classic.Level;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * @author Sebastien Vermeille
 */
@Getter
@EqualsAndHashCode
@ToString
public class LogEntry {
  private static final String SPACE = " ";
  private static final String EMPTY = "";
  private final String raw;

  private final Level level;

  private final LocalDateTime createdAt;

  private final String message;

  private final boolean companionLog;

  public LogEntry(final String rawLine) {
    this.raw = rawLine;

    this.companionLog = isCompanionLog(rawLine);
    if(this.companionLog) {
      this.message = rawLine;
      this.createdAt = null;
      this.level = null;
    } else {
      final var timeStr = this.raw.substring(0, 19);
      this.createdAt = LocalDateTime.parse(timeStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);

      final var levelStr = this.raw.substring(20, 25).replace(SPACE, EMPTY);
      this.level = Level.valueOf(levelStr);

      this.message = this.raw.substring(this.raw.indexOf(this.level.levelStr) + this.level.levelStr.length());
    }


  }

  private boolean isCompanionLog(final String rawLine) {
    // if too short to contain time
    if(rawLine.length() < 19) {
      return true;
    } else {
      return rawLine.charAt(10) != 'T' || rawLine.charAt(13) != ':' || rawLine.charAt(16) != ':';
    }
  }

  public Optional<LocalDateTime> getCreatedAt() {
    return Optional.ofNullable(this.createdAt);
  }

  public Optional<Level> getLevel() {
    return Optional.ofNullable(this.level);
  }
}
