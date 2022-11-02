package ch.svermeille.rika.health.service.api.dto;

import lombok.Data;

/**
 * @author Sebastien Vermeille
 */
@Data
public class Document {
  private String id;
  private String g;
  private String a;
  private String latestVersion;
  private String repositoryId;
  private String p;
  private String timestamp;
}
