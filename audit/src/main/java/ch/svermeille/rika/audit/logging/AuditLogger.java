package ch.svermeille.rika.audit.logging;

import static java.lang.Boolean.TRUE;
import static javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT;
import static javax.xml.bind.Marshaller.JAXB_FRAGMENT;

import ch.svermeille.rika.audit.xml.AuditedAction;
import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import lombok.NonNull;
import lombok.extern.flogger.Flogger;
import org.springframework.stereotype.Service;

/**
 * @author Sebastien Vermeille
 */
@Service
@Flogger
public class AuditLogger {

  public void audit(@NonNull final AuditedAction auditedAction) {
    try {
      final var jaxbContext = JAXBContext.newInstance(AuditedAction.class);
      final var jaxbMarshaller = jaxbContext.createMarshaller();
      jaxbMarshaller.setProperty(JAXB_FORMATTED_OUTPUT, TRUE);
      jaxbMarshaller.setProperty(JAXB_FRAGMENT, TRUE);
      final var writer = new StringWriter();
      jaxbMarshaller.marshal(auditedAction, writer);
      final String xmlString = writer.toString();

      log.at(auditedAction.getAction().getLevel())
          .log(xmlString);
    } catch(final JAXBException e) {
      log.atSevere()
          .withCause(e)
          .log("Could not serialize {}", auditedAction);
    }
  }

}
