package dev.cookiecode.rika2mqtt.plugins.internal.v1.event;

import dev.cookiecode.rika2mqtt.plugins.api.v1.model.StoveStatus;
import lombok.*;

@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
@Builder
public class PolledStoveStatusEvent implements Rika2MqttPluginEvent {
  private final StoveStatus stoveStatus;
}
