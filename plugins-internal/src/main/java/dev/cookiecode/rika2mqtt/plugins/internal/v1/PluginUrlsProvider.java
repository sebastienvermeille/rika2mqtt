package dev.cookiecode.rika2mqtt.plugins.internal.v1;

import lombok.RequiredArgsConstructor;
import lombok.extern.flogger.Flogger;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import static java.net.URI.create;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Flogger
public class PluginUrlsProvider {

    static final String PLUGINS_ENV_VAR_NAME = "PLUGINS";
    static final String PLUGINS_SEPARATOR = ";";

    private final Environment environment;

    public List<URL> getDeclaredPlugins() {
        final var concatenatedString =
                Optional.ofNullable(environment.getProperty(PLUGINS_ENV_VAR_NAME)).orElse("");
        return Arrays.stream(concatenatedString.split(PLUGINS_SEPARATOR))
                .map(String::trim)
                .filter(urlStr -> !urlStr.isEmpty())
                .map(this::createUrl)
                .filter(Objects::nonNull)
                .toList();
    }

    private URL createUrl(String pluginUrlStr) {
        try {
            return create(pluginUrlStr).toURL();
        } catch (MalformedURLException e) {
            log.atSevere().withCause(e).log("Ignore the following url: %s", pluginUrlStr);
            return null;
        }
    }
}
