package ch.svermeille.rika.health.service;

import static java.lang.String.format;

import ch.svermeille.rika.health.service.api.MavenCentralSearchApi;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

/**
 * @author Sebastien Vermeille
 */
@Service
@RequiredArgsConstructor
public class MavenCentralSearchService {
  private final MavenCentralSearchApi mavenCentralSearchApi;

  @SneakyThrows
  public String getLatestVersion(@NonNull final String groupId, @NonNull final String artifactId) {
    final var querySearch = format("g:%s+AND+a:%s", groupId, artifactId);
    final var query = this.mavenCentralSearchApi.getLastVersions(querySearch);
    final var response = query.execute();

    if(response.isSuccessful() && response.body() != null && response.body().getResponse() != null) {
      return response.body().getResponse().getDocs().get(0).getLatestVersion();
    } else {
      throw new IllegalStateException("Could not retrieve latest version from maven central. Something is broken here");
    }
  }
}
