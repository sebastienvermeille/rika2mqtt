package ch.svermeille.rika.ui.view;

import ch.qos.logback.classic.Level;
import ch.svermeille.rika.log.viewer.service.LogViewerService;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import de.codecamp.vaadin.serviceref.ServiceRef;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import lombok.extern.flogger.Flogger;

/**
 * @author Sebastien Vermeille
 */
@Route(value = "system", layout = MainView.class)
@PageTitle("System")
@UIScope
@Flogger
public class SystemView extends HorizontalLayout {

  private final ServiceRef<LogViewerService> logViewerService;
  private final MessageList list = new MessageList();

  private FeederThread thread;

  public SystemView(final ServiceRef<LogViewerService> logViewerService) {
    this.logViewerService = logViewerService;
    setId("system-view");

    loadData();

    add(this.list);
  }

  private void loadData() {
    this.list.setItems(Collections.emptyList());

    final var logs = this.logViewerService.get().getLastLines(20, Set.of(Level.DEBUG));
    final var logEntries = new ArrayList<MessageListItem>();
    for(final var logEntry : logs) {
      logEntries.add(
          new MessageListItem(
              logEntry.getMessage(),
              logEntry.getCreatedAt().map(it -> it.toInstant(ZoneOffset.UTC)).orElse(null),
              logEntry.getLevel().map(level -> level.levelStr).orElse(null)
          )
      );
    }
    this.list.setItems(logEntries);
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
    private final SystemView view;

    public FeederThread(final UI ui, final SystemView view) {
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
            .log("User left the system page.");
        Thread.currentThread().interrupt();
      }
    }
  }
}
