package dev.cookiecode.rika2mqtt.plugins.internal.v1;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;

import static dev.cookiecode.rika2mqtt.plugins.internal.v1.PluginUrlsProvider.*;
import static java.net.URI.create;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;


/**
 * Test class
 *
 * @author Sebastien Vermeille
 */
@ExtendWith(MockitoExtension.class)
public class PluginUrlsProviderTest {
    @InjectMocks
    private PluginUrlsProvider pluginUrlsProvider;

    @Mock
    private Environment environment;


    @Test
    void getDeclaredPluginsShouldReturnAnEmptyListGivenOnlyASpaceIsProvided() {

        // GIVEN
        final var space = " ";
        doReturn(space).when(environment).getProperty(PLUGINS_ENV_VAR_NAME);

        // WHEN
        final var urls = pluginUrlsProvider.getDeclaredPlugins();

        // THEN
        assertThat(urls).isEmpty();
    }

    @Test
    void getDeclaredPluginsShouldReturnAnEmptyListGivenEnvIsNotSet() {

        // GIVEN
        doReturn(null).when(environment).getProperty(PLUGINS_ENV_VAR_NAME);

        // WHEN
        final var urls = pluginUrlsProvider.getDeclaredPlugins();

        // THEN
        assertThat(System.getenv(PLUGINS_ENV_VAR_NAME)).isNull();
        assertThat(urls).isEmpty();
    }

    @Test
    void getDeclaredPluginsShouldReturnAUrlObjectWrapping2UrlsGivenTwoUrlsWereProvided()
            throws Exception {

        // GIVEN
        final var pluginAUrl = "http://plugin-a.jar";
        final var pluginBUrl = "http://plugin-b.jar";

        final var pluginsUrls = String.format("%s%s%s", pluginAUrl, PLUGINS_SEPARATOR, pluginBUrl);
        doReturn(pluginsUrls).when(environment).getProperty(PLUGINS_ENV_VAR_NAME);

        // WHEN
        final var urls = pluginUrlsProvider.getDeclaredPlugins();

        // THEN
        assertThat(urls)
                .isNotEmpty()
                .hasSize(2)
                .containsExactly(create(pluginAUrl).toURL(), create(pluginBUrl).toURL());
    }
}
