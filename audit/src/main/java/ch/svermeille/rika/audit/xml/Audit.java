package ch.svermeille.rika.audit.xml;

import static java.time.Instant.ofEpochMilli;
import static java.time.ZoneOffset.UTC;

import java.time.LocalDateTime;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Sebastien Vermeille
 */
@XmlType(name = "audit")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class Audit implements Comparable<Audit> {

  @XmlAttribute(name = "timestamp")
  private Long executedAt;

  @XmlAttribute(name = "level")
  private String level;

  @XmlElement(name = "audited-action")
  private AuditedAction auditedAction;

  public LocalDateTime getExecutedAt() {
    return ofEpochMilli(this.executedAt)
        .atZone(UTC)
        .toLocalDateTime();
  }

  @Override
  public int compareTo(final Audit audit) {
    return this.getExecutedAt().compareTo(audit.getExecutedAt());
  }
}
