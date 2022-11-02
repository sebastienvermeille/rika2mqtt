package ch.svermeille.rika.health.storage;

import ch.svermeille.rika.health.storage.model.HealthCheckResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.Synchronized;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

/**
 * @author Sebastien Vermeille
 */
@Service
@RequiredArgsConstructor
public class JsonHeathCheckStorageServiceImpl implements HeathCheckStorageService, InitializingBean {

  public static final String HEALTH_CHECK_FILE = "logs/health-checks.json";

  @SuppressWarnings("unused") // used by lombok
  private final Lock readWriteLock = new Lock();

  private final Gson gson;

  @Override
  public void afterPropertiesSet() throws Exception {
    ensureHealthCheckStorageFileIsPresent();
  }

  @Override
  @SneakyThrows
  @Synchronized("readWriteLock")
  public List<HealthCheckResult> getSavedResults() {
    final Type listType = new TypeToken<List<HealthCheckResult>>() {
    }.getType();

    final Reader reader = new FileReader(HEALTH_CHECK_FILE);
    List<HealthCheckResult> checkResults = this.gson.fromJson(reader, listType);
    if(checkResults == null) {
      checkResults = new ArrayList<>();
    }
    return checkResults;
  }

  @Override
  @Synchronized("readWriteLock")
  public void saveHealthCheckResult(final HealthCheckResult checkResult) {

    final var savedResults = getSavedResults();

    final List<HealthCheckResult> newChecks = new ArrayList<>();
    for(final HealthCheckResult current : savedResults) {
      if(!checkResult.getName().equals(current.getName())) {
        newChecks.add(current); // preserve old values
      }
    }
    newChecks.add(checkResult);

    try(final Writer writer = new FileWriter(HEALTH_CHECK_FILE)) {
      this.gson.toJson(newChecks.stream().sorted().toList(), writer);
    } catch(final IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void ensureHealthCheckStorageFileIsPresent() throws IOException {
    final var f = new File(HEALTH_CHECK_FILE);
    if(!f.exists()) {
      f.createNewFile();
    }
  }

}
