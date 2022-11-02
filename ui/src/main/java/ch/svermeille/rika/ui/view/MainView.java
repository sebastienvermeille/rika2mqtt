package ch.svermeille.rika.ui.view;

import ch.svermeille.rika.config.event.ConfigurationChangeRequireRestartEvent;
import ch.svermeille.rika.config.event.UserRequestedRestartEvent;
import ch.svermeille.rika.ui.components.github.corner.GithubCorner;
import ch.svermeille.rika.ui.view.about.AboutView;
import ch.svermeille.rika.ui.view.settings.ConfigurationView;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.spring.annotation.UIScope;
import de.codecamp.vaadin.serviceref.ServiceRef;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

/**
 * @author Sebastien Vermeille
 */
@UIScope
public class MainView extends AppLayout {

  public static final String VAADIN_ICON_COLLECTION = "vaadin";
  private final Tabs menu;
  private H1 viewTitle;

  private UI ui;

  private final ServiceRef<ApplicationEventPublisher> publisher;

  public MainView(@Autowired final EventBus eventBus,
      @Autowired final ServiceRef<ApplicationEventPublisher> publisher) {
    setPrimarySection(Section.DRAWER);

    this.publisher = publisher;

    addAttachListener(event -> this.ui = event.getUI());

    // register eventbus
    eventBus.register(this);

    // store this eventbus in session, so that its accessible everywhere
    UI.getCurrent().getSession().setAttribute(EventBus.class, eventBus);


    addToNavbar(true, createHeaderContent());

    this.menu = createMenu();
    addToDrawer(createDrawerContent(this.menu));
  }

  private Component createDrawerContent(final Tabs menu) {
    final var layout = new VerticalLayout();

    // Configure styling for the drawer
    layout.setSizeFull();
    layout.setPadding(false);
    layout.setSpacing(false);
    layout.getThemeList().set("spacing-s", true);
    layout.setAlignItems(FlexComponent.Alignment.STRETCH);

    // Have a drawer header with an application logo
    final HorizontalLayout logoLayout = new HorizontalLayout();
    logoLayout.setId("logo");
    logoLayout.setAlignItems(FlexComponent.Alignment.CENTER);
    logoLayout.add(new Image("images/logo.png", "rika2mqtt logo"));

    // Display the logo and the menu in the drawer
    layout.add(logoLayout, menu);
    return layout;
  }


  private Tabs createMenu() {
    final Tabs tabs = new Tabs();
    tabs.setOrientation(Tabs.Orientation.VERTICAL);
    tabs.addThemeVariants(TabsVariant.LUMO_MINIMAL);
    tabs.setId("tabs");
    tabs.add(createMenuItems());
    return tabs;
  }

  private Component[] createMenuItems() {
    return new Tab[] {
        createTab("Dashboard", new Icon(VAADIN_ICON_COLLECTION, "fire"), DashboardView.class),
        createTab("Activity", new Icon(VAADIN_ICON_COLLECTION, "clock"), ActivityView.class),
        createTab("Settings", new Icon(VAADIN_ICON_COLLECTION, "cogs"), ConfigurationView.class),
        createTab("System", new Icon(VAADIN_ICON_COLLECTION, "desktop"), SystemView.class),
        createTab("About", new Icon(VAADIN_ICON_COLLECTION, "info-circle-o"), AboutView.class),
    };
  }

  private static Tab createTab(final String text, final Icon icon, final Class<? extends Component> navigationTarget) {
    final Tab tab = new Tab();
    tab.add(new RouterLink(text, navigationTarget));
    tab.add(icon);
    ComponentUtil.setData(tab, Class.class, navigationTarget);
    return tab;
  }

  private Component createHeaderContent() {
    final var layout = new HorizontalLayout();

    // Configure styling for the header
    layout.setId("header");
    layout.getThemeList().set("dark", true);
    layout.setWidthFull();
    layout.setSpacing(false);
    layout.setAlignItems(FlexComponent.Alignment.CENTER);

    // Have the drawer toggle button on the left
    layout.add(new DrawerToggle());

    // Placeholder for the title of the current view.
    // The title will be set after navigation.
    this.viewTitle = new H1();
    layout.add(this.viewTitle);

    final var githubForkCorner = new GithubCorner("https://github.com/sebastienvermeille/rika2mqtt");
    layout.add(githubForkCorner);

    return layout;
  }

  @Override
  protected void afterNavigation() {
    super.afterNavigation();

    // Select the tab corresponding to currently shown view
    getTabForComponent(getContent()).ifPresent(this.menu::setSelectedTab);

    // Set the view title in the header
    this.viewTitle.setText(getCurrentPageTitle());
  }

  private Optional<Tab> getTabForComponent(final Component component) {
    return this.menu.getChildren().filter(tab -> ComponentUtil.getData(tab, Class.class).equals(component.getClass()))
        .findFirst().map(Tab.class::cast);
  }

  private String getCurrentPageTitle() {
    return getContent().getClass().getAnnotation(PageTitle.class).value();
  }

  @Subscribe
  public void onRequireRestart(final ConfigurationChangeRequireRestartEvent evt) {
    this.ui.access(() -> {

      final Notification notification = new Notification();
      notification.setDuration(10000);
      notification.addThemeVariants(NotificationVariant.LUMO_CONTRAST);

      final Div statusText = new Div(new Text("Recent changes performed in configuration require a restart. Would you like to restart ?"));

      final Button undoButton = new Button("Restart now");
      undoButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
      undoButton.getElement().getStyle().set("margin-left", "var(--lumo-space-xl)");
      undoButton.addClickListener(event -> {
        this.publisher.get().publishEvent(new UserRequestedRestartEvent("blabla", event));
        notification.close();
      });

      final Button closeButton = new Button(new Icon("lumo", "cross"));
      closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
      closeButton.getElement().setAttribute("aria-label", "Close");
      closeButton.addClickListener(event -> notification.close());

      final HorizontalLayout layout = new HorizontalLayout(statusText, undoButton, closeButton);
      layout.setAlignItems(FlexComponent.Alignment.CENTER);

      notification.add(layout);
      notification.open();
    });

  }
}
