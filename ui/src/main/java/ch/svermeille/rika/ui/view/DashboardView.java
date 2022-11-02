package ch.svermeille.rika.ui.view;

import ch.svermeille.rika.health.service.HealthCheckService;
import ch.svermeille.rika.health.storage.model.HealthCheckResult;
import ch.svermeille.rika.health.storage.model.Status;
import ch.svermeille.rika.ui.time.TimeAgoHelper;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.SerializableBiConsumer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.spring.annotation.UIScope;
import de.codecamp.vaadin.serviceref.ServiceRef;
import lombok.extern.flogger.Flogger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Sebastien Vermeille
 */
@Route(value = "dashboard", layout = MainView.class)
@RouteAlias(value = "", layout = MainView.class)
@PageTitle("Dashboard")
@UIScope
@Flogger
public class DashboardView extends VerticalLayout {

  private transient FeederThread thread;

  private final Grid<HealthCheckResult> grid;

  private final ServiceRef<HealthCheckService> healthCheckService;

  @SuppressWarnings("java:S1144") // Sonar see this constructor as unused but it is used by Vaadin
  private DashboardView(@Autowired final TimeAgoHelper timeAgoHelper, @Autowired final ServiceRef<HealthCheckService> healthCheckService) {
    this.healthCheckService = healthCheckService;
    setId("dashboard-view");

    add(new H2("Health checks"));

    this.grid = new Grid<>(HealthCheckResult.class, false);
    this.grid.setSelectionMode(Grid.SelectionMode.NONE);
    this.grid.addColumn(createCheckStatusRenderer()).setHeader("Name").setAutoWidth(true);
    this.grid.addColumn(HealthCheckResult::getName).setHeader("Operation").setAutoWidth(true);
    this.grid.addColumn(healthCheckResult -> timeAgoHelper.evalAsHumanFriendlyTimeAgo(healthCheckResult.getLastExecuted())).setHeader("Last execution").setAutoWidth(true);
    this.grid.setItems(healthCheckService.get().getHealthCheckResults());
    add(this.grid);
    loadData();

  }

  private void loadData() {
    this.grid.setItems(this.healthCheckService.get().getHealthCheckResults());
  }

  private static final String THEME = "theme";
  private static final SerializableBiConsumer<Span, HealthCheckResult> statusComponentUpdater = (
      span, result) -> {
    boolean isScheduled = Status.SCHEDULED.equals(result.getStatus());
    boolean isInProgress = Status.IN_PROGRESS.equals(result.getStatus());

    if(isScheduled) {
      span.setText("");
      span.getElement().setAttribute(THEME, "badge");
      span.add(createIcon(VaadinIcon.CLOCK, "Scheduled operation"));
    } else if(isInProgress) {
      span.setText("");
      span.getElement().setAttribute(THEME, "badge");
      span.add(createIcon(VaadinIcon.HOURGLASS, "Operation in progress"));
    } else {
      boolean isSuccess = Status.PASS.equals(result.getStatus());

      span.setText("");
      String theme = String.format("badge %s", isSuccess ? "success" : "error");
      span.getElement().setAttribute(THEME, theme);

      if(isSuccess) {
        span.add(createIcon(VaadinIcon.CHECK_CIRCLE, "PASS"));
      } else {
        span.add(createIcon(VaadinIcon.EXCLAMATION_CIRCLE, "FAILURE"));
      }
    }
  };

  private static ComponentRenderer<Span, HealthCheckResult> createCheckStatusRenderer() {
    return new ComponentRenderer<>(Span::new, statusComponentUpdater);
  }

  private static Icon createIcon(final VaadinIcon vaadinIcon, final String toolTip) {
    final Icon icon = vaadinIcon.create();
    icon.setTooltipText(toolTip);
    icon.getStyle().set("padding", "var(--lumo-space-xs");
    return icon;
  }

  @Override
  protected void onAttach(final AttachEvent attachEvent) {
    // Start the data feed thread
    this.thread = new FeederThread(attachEvent.getUI(), this);
    this.thread.start();
  }

  @Override
  protected void onDetach(final DetachEvent detachEvent) {
    // Cleanup
    this.thread.interrupt();
    this.thread = null;
  }

  private static class FeederThread extends Thread {
    private final UI ui;
    private final DashboardView view;

    public FeederThread(final UI ui, final DashboardView view) {
      this.ui = ui;
      this.view = view;
    }

    @Override
    public void run() {
      try {
        // Update the data for a while
        while(true) {
          // Sleep to emulate background work
          Thread.sleep(1000);
          this.ui.access(this.view::loadData);
        }
      } catch(final InterruptedException e) {
        log.atInfo()
            .log("User left the dashboard page.");
        Thread.currentThread().interrupt();
      }
    }
  }
}
