package ch.svermeille.rika;

import ch.svermeille.rika.audit.logging.AuditLogger;
import ch.svermeille.rika.audit.xml.Actions;
import ch.svermeille.rika.audit.xml.AuditedAction;
import ch.svermeille.rika.config.event.UserRequestedRestartEvent;
import lombok.NonNull;
import lombok.extern.flogger.Flogger;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.EventListener;

@SpringBootApplication(scanBasePackages = {"ch.svermeille.rika.*"})
@Flogger
public class Rika2MqttApplication {

  private static ConfigurableApplicationContext context;

  public static void main(final String[] args) {
    System.setProperty("flogger.backend_factory", "com.google.common.flogger.backend.slf4j.Slf4jBackendFactory#getInstance");
    context = SpringApplication.run(Rika2MqttApplication.class, args);
  }

  public static void restart() {
    final ApplicationArguments args = context.getBean(ApplicationArguments.class);

    final Thread thread = new Thread(() -> {
      context.close();
      context = SpringApplication.run(Rika2MqttApplication.class, args.getSourceArgs());
    });

    thread.setDaemon(false);
    thread.start();
  }

  @EventListener
  public void onUserRequestRestart(@NonNull final UserRequestedRestartEvent event) {
    final var auditLogger = context.getBean(AuditLogger.class);
    log.atInfo().log("Application restart triggered from event %s (cause: %s)", event.getClass().getSimpleName(), event.getMessage());
    auditLogger.audit(AuditedAction.builder()
        .withAction(Actions.USER_RESTART_APPLICATION)
        .build()
    );
    restart();
  }
}
