package ch.svermeille.rika.ui.view;

import static com.vaadin.flow.component.orderedlayout.Scroller.ScrollDirection.VERTICAL;

import ch.svermeille.rika.audit.service.AuditService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import de.codecamp.vaadin.serviceref.ServiceRef;
import de.nils_bauer.PureTimeline;
import de.nils_bauer.PureTimelineItem;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Sebastien Vermeille
 */
@Route(value = "activity", layout = MainView.class)
@PageTitle("Activity")
@UIScope
public class ActivityView extends HorizontalLayout {

  private final ServiceRef<AuditService> auditService;
  private final PureTimeline timeline;

  private final VerticalLayout verticalLayout;
  private int currentPage;

  @SuppressWarnings("java:S1144") // Sonar see this constructor as unused but it is used by Vaadin
  private ActivityView(@Autowired final ServiceRef<AuditService> auditService) {
    this.auditService = auditService;
    setId("activity-view");

    this.timeline = new PureTimeline();

    this.verticalLayout = new VerticalLayout();
    this.verticalLayout.setWidthFull();
    this.verticalLayout.setHeightFull();
    this.verticalLayout.add(this.timeline);

    final var scroller = new Scroller();
    scroller.setWidthFull();
    scroller.setHeightFull();
    scroller.setContent(this.verticalLayout);
    scroller.setScrollDirection(VERTICAL);
    add(scroller);

    this.currentPage = 0;
    loadNextPage();
  }

  private void loadNextPage() {
    this.currentPage++;
    final var activityStream = this.auditService.get().getReversedAuditedActions(this.currentPage);
    activityStream.forEach(audit -> this.timeline.add(
        new PureTimelineItem(
            audit.getLevel(),
            new H3(audit.getAuditedAction().getAction().name()),
            new Paragraph(String.format("""
                    %s %n
                    %s
                    """,
                audit.getExecutedAt(),
                audit.getAuditedAction().getProps().keySet()))
        )
    ));

    final var loadedActivities = this.timeline.getChildren().count();
    // is that enough or a few more would be good ? :)
    if(loadedActivities < 10) {
      // try to load more
      this.currentPage++;
      final var activityStreama = this.auditService.get().getReversedAuditedActions(this.currentPage);
      activityStreama.forEach(audit -> this.timeline.add(
          new PureTimelineItem(
              audit.getLevel(),
              new H3(audit.getAuditedAction().getAction().name()),
              new Paragraph(String.format("""
                      %s %n
                      %s
                      """,
                  audit.getExecutedAt(),
                  audit.getAuditedAction().getProps().keySet()))
          )
      ));
    }

    if(this.currentPage < this.auditService.get().getPageCount()) {
      final var btn = new Button("Load more");
      btn.addClickListener(event -> {
        loadNextPage();
        this.verticalLayout.remove(btn);
      });
      btn.setWidthFull();
      this.verticalLayout.add(btn);
    }

  }
}
