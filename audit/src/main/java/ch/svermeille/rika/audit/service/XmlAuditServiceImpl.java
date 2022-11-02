package ch.svermeille.rika.audit.service;

import static java.lang.String.format;
import static java.nio.file.Files.isDirectory;
import static java.nio.file.Paths.get;
import static java.util.Collections.reverse;

import ch.svermeille.rika.audit.service.serialization.StreamingUnmarshaller;
import ch.svermeille.rika.audit.xml.Audit;
import ch.svermeille.rika.audit.xml.AuditedAction;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.flogger.Flogger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author Sebastien Vermeille
 */
@Service
@Flogger
public class XmlAuditServiceImpl implements AuditService {

  static final String AUDIT_XML_FILE = "audit.xml";
  private static final String EXTENSION_XML_GZ = ".xml.gz";
  public static final String AUDIT_ENTITY_START = "<audits>";
  public static final String AUDIT_ENTITY_END = "</audits>";
  private static final String AUDIT_LOG_FILE_NAME_PREFIX = "audit";

  @Value("${audit.logsPath}")
  @Getter
  private String logsDir;

  InputStream getAuditInputStream(final int pageNumber) throws IOException {
    final var fileName = getAuditFiles().get(pageNumber - 1);
    final var auditLogInputStream = getInputStream(fileName);

    final var beginInputStream = new ByteArrayInputStream(AUDIT_ENTITY_START.getBytes());
    final var endInputStream = new ByteArrayInputStream(AUDIT_ENTITY_END.getBytes());

    final var intermediateStream = new SequenceInputStream(beginInputStream, auditLogInputStream);
    return new SequenceInputStream(intermediateStream, endInputStream);
  }

  private InputStream getInputStream(@NonNull final String fileName) throws IOException {
    if(fileName.equals(AUDIT_XML_FILE)) {
      return new FileInputStream(this.getLogsDir() + AUDIT_XML_FILE);
    } else if(fileName.endsWith(EXTENSION_XML_GZ)) {
      return new GZIPInputStream(new FileInputStream(this.getLogsDir() + fileName));
    } else {
      throw new IllegalArgumentException(
          format("Could not retrieve any file named %s in %s",
              fileName,
              this.getLogsDir()
          )
      );
    }
  }

  public List<String> getAuditFiles() throws IOException {
    try(final var pathStream = Files.list(get(this.getLogsDir()))) {
      final var files = pathStream
          .filter(file -> !isDirectory(file))
          .map(Path::getFileName)
          .map(Path::toString)
          .sorted() // should be sorted reverse way also warning: audit.xml should be the first
          .toList();

      final var filteredFiles = files.stream()
          .filter(path -> path.startsWith(AUDIT_LOG_FILE_NAME_PREFIX))
          .toList();

      final var auditXmlFile = filteredFiles.get(filteredFiles.size() - 1);

      final var sortedFiles = new ArrayList<>(filteredFiles.subList(0, filteredFiles.size() - 1));
      sortedFiles.add(auditXmlFile);
      reverse(sortedFiles);
      return sortedFiles;
    }
  }

  @Override
  public int getPageCount() {
    try {
      return getAuditFiles().size();
    } catch(final IOException e) {
      log.atSevere()
          .withCause(e)
          .log("Could not retrieve amount of audited actions page.");
      return 0;
    }
  }

  public Stream<Audit> getAuditedActions(final int pageNumber) {
    checkPageNumber(pageNumber);

    final Map<Class<?>, String> map = new HashMap<>();
    map.put(Audit.class, "audit");
    map.put(AuditedAction.class, "audited-action");

    try(final var unmarshaller = new StreamingUnmarshaller(map)) {
      unmarshaller.open(getAuditInputStream(pageNumber), 1);
      final Stream.Builder<Audit> builder = Stream.builder();
      while(unmarshaller.hasNext()) {
        final var audit = unmarshaller.next(Audit.class);
        builder.add(audit);
      }
      return builder.build();
    } catch(final Exception e) {
      throw new RuntimeException(e);
    }
  }

  private void checkPageNumber(final int pageNumber) {
    if(getPageCount() + 1 < pageNumber) {
      throw new IllegalArgumentException("Requested page index exceeds existing pages");
    }
  }

  @Override
  public Stream<Audit> getReversedAuditedActions(int pageNumber) {
    if(pageNumber < 1) {
      pageNumber = 1;
    }
    return getAuditedActions(pageNumber).sorted(Collections.reverseOrder());
  }
}
