package ch.svermeille.rika.ui.view.settings;

import ch.svermeille.rika.config.ConfigurationService;
import ch.svermeille.rika.firenet.configuration.RikaFirenetConfigProperties;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabVariant;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.spring.annotation.UIScope;
import java.time.Duration;
import java.util.HashMap;

/**
 * @author Sebastien Vermeille
 */
@UIScope
public class RikaConfigurationTab extends Div {

  public static Tab getTabTitle() {
    final var tab = new Tab(VaadinIcon.FIRE.create(), new Span("RIKA"));
    tab.addThemeVariants(TabVariant.LUMO_ICON_ON_TOP);
    return tab;
  }

  public RikaConfigurationTab(final RikaFirenetConfigProperties rikaFirenetConfigProperties,
      final ConfigurationService configurationService) {
    super();


    final var emailField = new EmailField("Rika-firenet email");
    emailField.setValue(rikaFirenetConfigProperties.getEmail());
    emailField.setRequiredIndicatorVisible(true);
    final var passwordField = new PasswordField("Rika-firenet password");
    passwordField.setRequiredIndicatorVisible(true);
    passwordField.setValue(rikaFirenetConfigProperties.getPassword());

    final var keepAliveTimeoutField = new NumberField("Keep alive interval");
    keepAliveTimeoutField.setMin(30);
    keepAliveTimeoutField.setMax(120);
    keepAliveTimeoutField.setStep(1);
    keepAliveTimeoutField.setAutocorrect(true);
    keepAliveTimeoutField.setHasControls(true);
    keepAliveTimeoutField.setValue((double) rikaFirenetConfigProperties.getKeepAliveTimeout().toSeconds());

    final var binder = new Binder<>(RikaFirenetConfigProperties.class);
    binder.forField(emailField)
        .asRequired()
        .bind(
            RikaFirenetConfigProperties::getEmail,
            RikaFirenetConfigProperties::setEmail
        );

    binder.forField(passwordField)
        .asRequired()
        .bind(RikaFirenetConfigProperties::getPassword,
            RikaFirenetConfigProperties::setPassword
        );

    binder.forField(keepAliveTimeoutField)
        .asRequired()
        .bind(rikaFirenetConfigProperties1 -> (double) rikaFirenetConfigProperties.getKeepAliveTimeout().toSeconds(),
            (rikaFirenetConfigProperties2, keepAliveTimeout) -> rikaFirenetConfigProperties.setKeepAliveTimeout(Duration.ofSeconds(keepAliveTimeout.longValue())));

    binder.readBean(rikaFirenetConfigProperties);

    final var formLayout = new FormLayout();
    formLayout.add(emailField, passwordField, keepAliveTimeoutField);
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
        binder.writeBean(rikaFirenetConfigProperties);

        final HashMap<String, Object> newValues = new HashMap<>();
        newValues.put("rika.email", rikaFirenetConfigProperties.getEmail());
        newValues.put("rika.password", rikaFirenetConfigProperties.getPassword());
        newValues.put("rika.keepAliveTimeout", rikaFirenetConfigProperties.getKeepAliveTimeout().toString());
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
