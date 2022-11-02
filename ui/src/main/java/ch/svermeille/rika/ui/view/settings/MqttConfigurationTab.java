package ch.svermeille.rika.ui.view.settings;

import ch.svermeille.rika.config.ConfigurationService;
import ch.svermeille.rika.mqtt.configuration.MqttConfigProperties;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabVariant;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import java.util.HashMap;

/**
 * @author Sebastien Vermeille
 */
public class MqttConfigurationTab extends Div {

  public static Tab getTabTitle() {
    final var tab = new Tab(VaadinIcon.PAPERPLANE.create(), new Span("MQTT"));
    tab.addThemeVariants(TabVariant.LUMO_ICON_ON_TOP);
    return tab;
  }

  public MqttConfigurationTab(final MqttConfigProperties mqttConfigProperties,
      final ConfigurationService configurationService) {
    super();

    final var hostField = new TextField("Mqtt host");
    hostField.setValue(mqttConfigProperties.getHost());
    final var userField = new TextField("Mqtt user");
    userField.setValue(mqttConfigProperties.getUser());
    userField.setRequired(false);
    final var passwordField = new PasswordField("Mqtt password");
    passwordField.setValue(mqttConfigProperties.getPassword());

    final var binder = new Binder<>(MqttConfigProperties.class);
    binder.forField(hostField)
        .asRequired()
        .bind(
            MqttConfigProperties::getHost,
            MqttConfigProperties::setHost
        );

    binder.forField(userField)
        .bind(MqttConfigProperties::getUser,
            MqttConfigProperties::setUser
        );

    binder.forField(passwordField)
        .asRequired()
        .bind(MqttConfigProperties::getPassword,
            MqttConfigProperties::setPassword
        );


    binder.readBean(mqttConfigProperties);

    final var formLayout = new FormLayout();
    formLayout.add(hostField, userField, passwordField);
    formLayout.setResponsiveSteps(
        // Use one column by default
        new FormLayout.ResponsiveStep("0", 1),
        // Use two columns, if layout's width exceeds 500px
        new FormLayout.ResponsiveStep("500px", 2));
    add(formLayout);

    final var saveButton = new Button("Save");
    saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
        ButtonVariant.LUMO_SUCCESS);

    saveButton.addClickListener(buttonClickEvent -> {
      try {
        binder.writeBean(mqttConfigProperties);

        final HashMap<String, Object> newValues = new HashMap<>();
        newValues.put("mqtt.host", mqttConfigProperties.getHost());
        newValues.put("mqtt.user", mqttConfigProperties.getUser());
        newValues.put("mqtt.password", mqttConfigProperties.getPassword());
        configurationService.save(newValues);
        // save to backend
      } catch(final ValidationException e) {
//        notifyValidationException(e);
      }
    });

    final var testButton = new Button("Test");
    testButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY,
        ButtonVariant.LUMO_SUCCESS);

    final var buttonLayout = new HorizontalLayout(saveButton, testButton);

    add(buttonLayout);

  }
}
