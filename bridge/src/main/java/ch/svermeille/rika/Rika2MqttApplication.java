package ch.svermeille.rika;

import ch.svermeille.rika.config.event.ConfigurationChangeRequireRestartEvent;
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

	public static void main(String[] args) {
		context = SpringApplication.run(Rika2MqttApplication.class, args);
	}

	public static void restart() {
		ApplicationArguments args = context.getBean(ApplicationArguments.class);

		Thread thread = new Thread(() -> {
			context.close();
			context = SpringApplication.run(Rika2MqttApplication.class, args.getSourceArgs());
		});

		thread.setDaemon(false);
		thread.start();
	}

	@EventListener
	public void handleReturnedEvent(ConfigurationChangeRequireRestartEvent event) {
		log.atInfo().log("Application restart triggered from event %s (cause: %s)", event.getClass().getSimpleName(), event.getMessage());
		restart();
	}
}
