package ch.svermeille.rika.health.service.api.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * @author Sebastien Vermeille
 */
@Data
public class LastVersionResponseResponse {
  private List<Document> docs = new ArrayList<>();
}
