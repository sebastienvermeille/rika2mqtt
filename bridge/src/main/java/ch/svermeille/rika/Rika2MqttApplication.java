package ch.svermeille.rika;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Sebastien Vermeille
 */
@SpringBootApplication(scanBasePackages = {"ch.svermeille.rika.*"})
public class Rika2MqttApplication {
	public static void main(String[] args) {
		System.setProperty("flogger.backend_factory",
				"com.google.common.flogger.backend.slf4j.Slf4jBackendFactory#getInstance");
		SpringApplication.run(Rika2MqttApplication.class, args);
	}
}
