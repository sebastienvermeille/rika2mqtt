package ch.svermeille.rika.audit.xml;

import static lombok.AccessLevel.PACKAGE;

import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

/**
 * @author Sebastien Vermeille
 */
@Getter
@EqualsAndHashCode
@ToString
@XmlRootElement(name = "audited-action")
@NoArgsConstructor(access = PACKAGE)
public class AuditedAction {

  @XmlAttribute(name = "action")
  private Actions action;

  @XmlElement(name = "metadata")
  private Map<String, Object> props;

  private AuditedAction(final Builder builder) {
    this.action = builder.action;
    this.props = builder.props;
  }

  public static IAction builder() {
    return new Builder();
  }


  public interface IBuild {
    IBuild withProps(Map<String, Object> val);

    AuditedAction build();
  }

  public interface IProps {
    IBuild withProps(Map<String, Object> val);
  }

  public interface IAction {
    IBuild withAction(Actions val);
  }

  public static final class Builder implements IProps, IAction, IBuild {
    private Map<String, Object> props = new HashMap<>();
    private Actions action;

    private Builder() {
    }

    @Override
    public IBuild withProps(@NonNull final Map<String, Object> val) {
      this.props = val;
      return this;
    }

    @Override
    public IBuild withAction(final Actions val) {
      this.action = val;
      return this;
    }

    @Override
    public AuditedAction build() {
      return new AuditedAction(this);
    }
  }
}
