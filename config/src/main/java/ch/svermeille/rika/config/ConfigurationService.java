package ch.svermeille.rika.config;

import static org.reflections.scanners.Scanners.FieldsAnnotated;
import static org.reflections.util.ClasspathHelper.forPackage;

import ch.svermeille.rika.shared.RequireRestartWhenChanged;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.flogger.Flogger;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

/**
 * @author Sebastien Vermeille
 */
@Service
@Flogger
@RequiredArgsConstructor
public class ConfigurationService implements InitializingBean {

  private final ApplicationArguments applicationArguments;

  @Getter
  private Set<String> propertiesRequiringRestartWhenChanged = new HashSet<>();

  private String getApplicationConfigurationFilePath() {
    return this.applicationArguments.getOptionValues("spring.config.location").get(0)
        .replace("file://", "") + "application.yml";
  }

  @SneakyThrows
  public void save(final Map<String, Object> properties) {

    final var options = new DumperOptions();
    options.setIndent(2);
    options.setPrettyFlow(true);
    options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
    final Yaml yaml = new Yaml(options);
    final InputStream inputStream = new FileInputStream(getApplicationConfigurationFilePath());
    final Map<String, Object> configProperties = yaml.load(inputStream);

    properties.forEach((propKey, propValue) -> {
      final var propsParts = propKey.split("\\.");

      var currentPropsBranch = configProperties;
      for(var i = 0; i < propsParts.length; i++) {
        final var currPropsPart = propsParts[i];
        final var foundBranch = currentPropsBranch.get(currPropsPart);
        if(foundBranch instanceof LinkedHashMap<?, ?>) {
          currentPropsBranch = (LinkedHashMap<String, Object>) foundBranch;
          if(i == propsParts.length - 2) {
            // that's it
            currentPropsBranch.put(propsParts[i + 1], propValue);
            break;
          }
        }
      }
    });

    try(final var writer = new FileWriter(getApplicationConfigurationFilePath())) {
      yaml.dump(configProperties, writer);
    }
  }

  @Override
  public void afterPropertiesSet() {
    this.propertiesRequiringRestartWhenChanged = retrieveConfigurationsRequiringRestartWhenChanged();
  }

  Set<String> retrieveConfigurationsRequiringRestartWhenChanged() {

    final var reflections = new Reflections(new ConfigurationBuilder()
        .setUrls(
            forPackage("ch.svermeille.rika")
        )
        .setScanners(FieldsAnnotated)
    );

    final var fields = reflections.getFieldsAnnotatedWith(RequireRestartWhenChanged.class);
    final var properties = new HashSet<String>();
    for(final var field : fields) {
      final var annotation = field.getDeclaredAnnotation(RequireRestartWhenChanged.class);
      final var classAnnotation = field.getDeclaringClass().getAnnotation(ConfigurationProperties.class);

      // check if a prefix is defined
      Optional<String> prefix = Optional.empty();
      if(classAnnotation != null && !classAnnotation.prefix().isBlank()) {
        prefix = Optional.ofNullable(classAnnotation.prefix());
      }

      if(annotation.value()) {
        var fieldName = annotation.propertyPath().isBlank() ? field.getName() : annotation.propertyPath();

        if(prefix.isPresent()) {
          fieldName = prefix.get() + "." + fieldName;
        }
        properties.add(fieldName);
      }

    }

    return properties;
  }
}
