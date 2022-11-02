package ch.svermeille.rika.ui.view.about;

import static java.time.ZoneOffset.UTC;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

import ch.svermeille.rika.ui.view.MainView;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;

/**
 * @author Sebastien Vermeille
 */
@Route(value = "about", layout = MainView.class)
@PageTitle("About")
@UIScope
public class AboutView extends VerticalLayout {

  public AboutView(@Autowired final BuildProperties buildProperties) {
    setId("about-view");

    add(new H2("Deployment information"));
    final Grid<InfoTuple> grid = new Grid<>(InfoTuple.class, false);
    grid.setSelectionMode(Grid.SelectionMode.NONE);
    grid.addColumn(InfoTuple::getKey).setHeader("Property").setAutoWidth(true);
    grid.addColumn(InfoTuple::getValue).setHeader("Value").setAutoWidth(true);
    final var infoTuples = new ArrayList<InfoTuple>();
    infoTuples.add(InfoTuple.builder()
        .key("Rika2Mqtt Version")
        .value(buildProperties.getVersion())
        .build());

    infoTuples.add(
        InfoTuple.builder()
            .key("Build date")
            .value(buildProperties.getTime().atZone(UTC).format(ISO_LOCAL_DATE_TIME))
            .build()
    );

    infoTuples.add(
        InfoTuple.builder()
            .key("Wrapped by docker")
            .value(isRunningInsideDocker() ? "YES" : "NO")
            .build()
    );

    infoTuples.add(
        InfoTuple.builder()
            .key("Operating system")
            .value(System.getProperty("os.name") + " " + System.getProperty("os.version"))
            .build()
    );

    infoTuples.add(
        InfoTuple.builder()
            .key("Java version")
            .value(System.getProperty("java.specification.version") + "(" + System.getProperty("java.runtime.version") + ")")
            .build()
    );

    infoTuples.add(
        InfoTuple.builder()
            .key("JVM implementation")
            .value(System.getProperty("java.vm.name"))
            .build()
    );

    grid.setItems(infoTuples);
    add(grid);
  }

  public boolean isRunningInsideDocker() {
    try(final Stream<String> stream =
            Files.lines(Paths.get("/proc/1/cgroup"))) {
      return stream.anyMatch(line -> line.contains("/docker"));
    } catch(final IOException e) {
      return false;
    }
  }
}
