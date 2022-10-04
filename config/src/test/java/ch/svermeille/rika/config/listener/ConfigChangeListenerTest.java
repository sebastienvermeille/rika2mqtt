package ch.svermeille.rika.config.listener;


import static ch.svermeille.rika.config.listener.ConfigChangeListener.PROPS_REQUIRE_RESTART;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import top.code2life.config.ConfigurationChangedEvent;

/**
 * Test class
 *
 * @author Sebastien Vermeille
 */
class ConfigChangeListenerTest {

  private ConfigChangeListener listener;

  @BeforeEach
  void setUp(){
    this.listener = new ConfigChangeListener();
  }

  @Test
  void listenerShouldReturnNullGivenConfigurationChangedEventDeclareDiffsForKeysWhichAreNotDeclaredAsRequiringRestart(){
    // GIVEN
    final var event = mock(ConfigurationChangedEvent.class);
    final Map<String, Object> notRequiringRestartDiffs = new HashMap<>();
    notRequiringRestartDiffs.put("some.key.foo", "bar");
    when(event.getDiff()).thenReturn(notRequiringRestartDiffs);

    // WHEN
    var result = listener.onConfigurationChanged(event);

    // THEN
    assertThat(result)
        .withFailMessage("There is no diff for mandatory properties -> no reason to generate an output event ")
        .isNull();
  }

  @Test
  void listenerShouldReturnAConfigurationChangeRequireRestartEventGivenConfigurationChangedEventDeclareDiffsRequiringRestart(){
    // GIVEN
    final var event = mock(ConfigurationChangedEvent.class);
    final Map<String, Object> requiringRestartDiffs = new HashMap<>();
    requiringRestartDiffs.put(PROPS_REQUIRE_RESTART.stream().toList().get(0), "new value");
    when(event.getDiff()).thenReturn(requiringRestartDiffs);

    // WHEN
    var result = listener.onConfigurationChanged(event);

    // THEN
    assertThat(result)
        .withFailMessage("A diff exist for a property which is declared as requiring restart -> an output event has to be sent")
        .isNotNull();
  }
}
