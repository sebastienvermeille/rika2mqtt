package ch.svermeille.rika.ui.view.settings;

import ch.svermeille.rika.config.ConfigurationService;
import ch.svermeille.rika.firenet.configuration.RikaFirenetConfigProperties;
import ch.svermeille.rika.mqtt.configuration.MqttConfigProperties;
import ch.svermeille.rika.ui.view.MainView;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * A Designer generated component for the my-view template.
 *
 * Designer will add and remove fields with @Id mappings but
 * does not overwrite or otherwise change this file.
 */
//@Tag("configuration-form-view")
@UIScope
@Route(value = "settings", layout = MainView.class)
@PageTitle("Settings")
public class ConfigurationView extends VerticalLayout {

  /**
   * Creates a new ConfigurationView.
   */
  public ConfigurationView(@Autowired final RikaFirenetConfigProperties rikaFirenetConfigProperties,
      @Autowired final MqttConfigProperties mqttConfigProperties,
      @Autowired final ConfigurationService configurationService) {
    final TabSheet tabSheet = new TabSheet();
    tabSheet.add(RikaConfigurationTab.getTabTitle(), new RikaConfigurationTab(rikaFirenetConfigProperties, configurationService));
    tabSheet.add(MqttConfigurationTab.getTabTitle(), new MqttConfigurationTab(mqttConfigProperties, configurationService));
    add(tabSheet);
  }

}
