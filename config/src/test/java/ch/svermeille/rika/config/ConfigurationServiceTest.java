package ch.svermeille.rika.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Sebastien Vermeille
 */
@SpringBootTest(classes = {
    ConfigurationService.class
})
class ConfigurationServiceTest {

  @Autowired
  private ConfigurationService configurationService;

  @Test
  void retrieveConfigurationsRequiringRestartWhenChangedShouldNotBeEmpty() {
    // GIVEN

    // WHEN
    final var props = this.configurationService.retrieveConfigurationsRequiringRestartWhenChanged();

    // THEN
    assertThat(props)
        .withFailMessage("There are some mandatory properties in this project no way that it's empty there.")
        .isNotEmpty();
  }
}
