package ch.svermeille.rika.audit.logging.template;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.LayoutBase;

/**
 * @author Sebastien Vermeille
 */
public class AuditXmlLayout extends LayoutBase<ILoggingEvent> {

  private static final int DEFAULT_SIZE = 256;
  private static final int UPPER_LIMIT = 2048;
  private static final String CONTENT_TYPE_TEXT_XML = "text/xml";

  private StringBuilder buf = new StringBuilder(DEFAULT_SIZE);

  /**
   * Formats a {@link ILoggingEvent} in conformity with the log4j.dtd.
   */
  @Override
  public String doLayout(final ILoggingEvent event) {

    // Reset working buffer. If the buffer is too large, then we need a new
    // one in order to avoid the penalty of creating a large array.
    if(this.buf.capacity() > UPPER_LIMIT) {
      this.buf = new StringBuilder(DEFAULT_SIZE);
    } else {
      this.buf.setLength(0);
    }

    // We yield to the \r\n heresy.

    this.buf.append("<audit");
    this.buf.append(" timestamp=\"");
    this.buf.append(event.getTimeStamp());
    this.buf.append("\" level=\"");
    this.buf.append(event.getLevel());
    this.buf.append("\"");
    this.buf.append(">\r\n");
    this.buf.append("    ");
    this.buf.append(event.getFormattedMessage());

    this.buf.append("\r\n</audit>\r\n");

    return this.buf.toString();
  }

  @Override
  public String getContentType() {
    return CONTENT_TYPE_TEXT_XML;
  }

}
