package ch.svermeille.rika.config.listener;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import ch.svermeille.rika.audit.logging.AuditLogger;
import ch.svermeille.rika.config.ConfigurationService;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import top.code2life.config.ConfigurationChangedEvent;

/**
 * Test class
 *
 * @author Sebastien Vermeille
 */
class ConfigChangeListenerTest {

  @Mock
  private AuditLogger auditLogger;

  @Mock
  private ConfigurationService configurationService;

  @InjectMocks
  private ConfigChangeListener configChangeListener;

  @BeforeEach
  void setUp() {
    openMocks(this);
  }

  @Test
  void listenerShouldReturnNullGivenConfigurationChangedEventDeclareDiffsForKeysWhichAreNotDeclaredAsRequiringRestart() {
    // GIVEN
    final var event = mock(ConfigurationChangedEvent.class);
    final Map<String, Object> notRequiringRestartDiffs = new HashMap<>();
    notRequiringRestartDiffs.put("some.key.foo", "bar");
    when(event.getDiff()).thenReturn(notRequiringRestartDiffs);

    // WHEN
    final var result = this.configChangeListener.onConfigurationChanged(event);

    // THEN
    assertThat(result)
        .withFailMessage("There is no diff for mandatory properties -> no reason to generate an output event ")
        .isNull();
  }

  @Test
  void listenerShouldReturnAConfigurationChangeRequireRestartEventGivenConfigurationChangedEventDeclareDiffsRequiringRestart() {
    // GIVEN
    final var propRequireRestart = "some.prop.requiring.restart";
    when(this.configurationService.getPropertiesRequiringRestartWhenChanged()).thenReturn(Set.of(propRequireRestart));


    final var event = mock(ConfigurationChangedEvent.class);
    final Map<String, Object> requiringRestartDiffs = new HashMap<>();
    requiringRestartDiffs.put(propRequireRestart, "new value");
    when(event.getDiff()).thenReturn(requiringRestartDiffs);

    // WHEN
    final var result = this.configChangeListener.onConfigurationChanged(event);

    // THEN
    assertThat(result)
        .withFailMessage("A diff exist for a property which is declared as requiring restart -> an output event has to be sent")
        .isNotNull();
  }
}
