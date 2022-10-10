package ch.svermeille.rika.audit;

import static lombok.AccessLevel.PACKAGE;

import java.util.Map;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
    action = builder.action;
    props = builder.props;
  }

  public static IAction builder() {
    return new Builder();
  }


  public interface IBuild {
    AuditedAction build();
  }

  public interface IProps {
    IBuild withProps(Map<String, Object> val);
  }

  public interface IAction {
    IProps withAction(Actions val);
  }

  public static final class Builder implements IProps, IAction, IBuild {
    private Map<String, Object> props;
    private Actions action;

    private Builder() {
    }

    @Override
    public IBuild withProps(final Map<String, Object> val) {
      props = val;
      return this;
    }

    @Override
    public IProps withAction(final Actions val) {
      action = val;
      return this;
    }

    public AuditedAction build() {
      return new AuditedAction(this);
    }
  }
}
