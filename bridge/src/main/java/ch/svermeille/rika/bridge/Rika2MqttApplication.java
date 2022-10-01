package ch.svermeille.rika.bridge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"ch.svermeille.rika.*"})
public class Rika2MqttApplication {

	public static void main(String[] args) {
		SpringApplication.run(Rika2MqttApplication.class, args);
	}

}
