package ch.svermeille.rika.audit.service;


import static ch.svermeille.rika.audit.xml.Actions.CHANGED_CONFIGURATION;
import static ch.svermeille.rika.audit.xml.Actions.USER_RESTART_APPLICATION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.MockitoAnnotations.openMocks;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class
 *
 * @author Sebastien Vermeille
 */
class XmlAuditServiceImplTest {

  private XmlAuditServiceImpl auditService;

  @BeforeEach
  void setUp() {
    openMocks(this);
    this.auditService = spy(new XmlAuditServiceImpl());
  }

  @Test
  void getAuditedActionsShouldReturn2ElementsGivenTheAuditXmlFile() throws Exception {
    // GIVEN
    final var testStream = getClass().getResourceAsStream("/logs/audit.xml");
    final var auditInputStream = mockInputStream(testStream);
    final var pageNumber = 1;
    doReturn(List.of("/logs/audit.xml")).when(this.auditService).getAuditFiles();
    doReturn(auditInputStream).when(this.auditService).getAuditInputStream(pageNumber);

    // WHEN
    final var actualResults = this.auditService.getAuditedActions(pageNumber).toList();

    // THEN
    assertThat(actualResults).isNotEmpty();
    assertThat(actualResults.get(0).getAuditedAction().getAction()).isEqualTo(CHANGED_CONFIGURATION);
    assertThat(actualResults.get(1).getAuditedAction().getAction()).isEqualTo(USER_RESTART_APPLICATION);
  }

  InputStream mockInputStream(final InputStream auditLogInputStream) {
    final var beginInputStream = new ByteArrayInputStream("<audits>".getBytes());
    final var endInputStream = new ByteArrayInputStream("</audits>".getBytes());
    final var intermediateStream = new SequenceInputStream(beginInputStream, auditLogInputStream);
    return new SequenceInputStream(intermediateStream, endInputStream);
  }
}
