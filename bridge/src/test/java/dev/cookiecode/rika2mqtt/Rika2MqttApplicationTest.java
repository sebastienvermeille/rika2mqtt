package dev.cookiecode.rika2mqtt;

import lombok.Cleanup;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

/**
 * Test class
 *
 * @author Sebastien Vermeille
 */
class Rika2MqttApplicationTest {

    @Test
    void mainShouldInvokeSpringApplicationRunWithReceivedArgs(){

        // GIVEN
        String[] args = new String[] { "foo", "bar" };
        @Cleanup MockedStatic<SpringApplication> mocked = mockStatic(SpringApplication.class);
        mocked.when(() -> SpringApplication.run(Rika2MqttApplication.class, args))
                .thenReturn(mock(ConfigurableApplicationContext.class));

        // WHEN
        Rika2MqttApplication.main(args);

        // THEN
        mocked.verify(() -> SpringApplication.run(Rika2MqttApplication.class, args));
    }

}