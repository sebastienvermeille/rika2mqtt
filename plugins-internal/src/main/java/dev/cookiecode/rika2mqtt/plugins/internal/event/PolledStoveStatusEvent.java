package dev.cookiecode.rika2mqtt.plugins.internal.event;

import dev.cookiecode.rika2mqtt.plugins.api.model.StoveStatus;
import lombok.*;

@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
@Builder
public class PolledStoveStatusEvent implements Rika2MqttPluginEvent {
  private final StoveStatus stoveStatus;
}
