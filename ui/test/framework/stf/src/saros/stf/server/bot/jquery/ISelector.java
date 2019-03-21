package saros.stf.server.bot.jquery;

import org.apache.commons.lang.StringUtils;

public interface ISelector {

  /**
   * Instances of this class simply use the given string as their selector.
   *
   * @author bkahlert
   */
  public class Selector implements ISelector {
    private String expr;

    public Selector(String expr) {
      this.expr = expr;
    }

    @Override
    public String toString() {
      return this.expr;
    }

    @Override
    public String getStatement() {
      return "$('" + this.escapeJS(expr) + "')";
    }

    private String escapeJS(String jsCode) {
      return jsCode
          .replace("\n", "<br>")
          .replace("&#xD;", "")
          .replace("\r", "")
          .replace("\"", "\\\"")
          .replace("'", "\\'");
    }
  }

  /**
   * Instances of this class select the element with a certain ID.
   *
   * @author bkahlert
   */
  public static class IdSelector extends Selector {
    private String id;

    public IdSelector(String id) {
      super("#" + id);
      this.id = id;
    }

    public String getId() {
      return id;
    }
  }

  /**
   * Instances of this class select all elements with a certain name.
   *
   * @author bkahlert
   */
  public static class NameSelector extends Selector {
    private String name;

    public NameSelector(String name) {
      super("*[name=" + name + "]");
      this.name = name;
    }

    public String getName() {
      return name;
    }
  }

  /**
   * Instances of this class match all the provided {@link ISelector}s.
   *
   * @author bkahlert
   */
  public static class OrSelector extends Selector {
    public OrSelector(ISelector... selectors) {
      super(StringUtils.join(selectors, ","));
    }
  }

  /**
   * Instances of this class select all elements with a certain ID or name.
   *
   * @return
   */
  public static class FieldSelector extends OrSelector {

    public FieldSelector(String idOrName) {
      super(new IdSelector(idOrName), new NameSelector(idOrName));
    }
  }

  public static class ContainsTextSelector extends Selector {

    public ContainsTextSelector(String text) {
      super(":contains('" + text + "')");
    }
  }

  public static class CssClassSelector extends Selector {
    public CssClassSelector(String cssClass) {
      super("." + cssClass);
    }
  }

  @Override
  public String toString();

  public String getStatement();
}
