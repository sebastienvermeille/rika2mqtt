package ch.svermeille.rika.ui.service;

import ch.svermeille.rika.config.event.ConfigurationChangeRequireRestartEvent;
import com.google.common.eventbus.EventBus;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * @author Sebastien Vermeille
 */
@Service
@RequiredArgsConstructor
public class NotificationService {

  private final EventBus eventBus;

  @PostConstruct
  void setUp() {
    this.eventBus.register(this);
  }

  @PreDestroy
  void destroy() {
    this.eventBus.unregister(this);
  }

  @EventListener
  public void onConfigurationChanged(final ConfigurationChangeRequireRestartEvent event) {
    this.eventBus.post(event);
  }
}
