package ch.svermeille.rika.audit.service;

import ch.svermeille.rika.audit.xml.Audit;
import java.util.stream.Stream;

/**
 * @author Sebastien Vermeille
 */
public interface AuditService {

  Stream<Audit> getReversedAuditedActions(int pageNumber);

  int getPageCount();

}
