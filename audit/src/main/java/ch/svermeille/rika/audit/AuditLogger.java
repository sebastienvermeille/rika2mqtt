package ch.svermeille.rika.audit;

import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import lombok.NonNull;
import lombok.extern.flogger.Flogger;
import org.springframework.stereotype.Service;

/**
 * @author Sebastien Vermeille
 */
@Service
@Flogger
public class AuditLogger {

  public void audit(@NonNull AuditedAction auditedAction){
    try {
      final var jaxbContext = JAXBContext.newInstance(AuditedAction.class);
      final var jaxbMarshaller = jaxbContext.createMarshaller();
      jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
      jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
      StringWriter sw = new StringWriter();
      jaxbMarshaller.marshal(auditedAction, sw);
      String xmlString = sw.toString();

      log
          .at(auditedAction.getAction().getLevel())
          .log(xmlString);

    } catch(JAXBException e) {
      log.atSevere()
          .withCause(e)
          .log("Could not serialize {}", auditedAction);
    }
  }

}
