package saros.ui.browser;

public abstract class JavascriptFunction {

  protected IBrowserWrapper browser;
  private final String name;

  /** @param name the name of the Javascript function that calls into Java */
  public JavascriptFunction(String name) {
    this.name = name;
  }

  public void setBrowser(IBrowserWrapper browser) {
    this.browser = browser;
  }

  /**
   * This method is to be overridden to supply the Java code
   *
   * @param arguments an array of Object, these are the parameters provided in Javascript
   * @return the value to return back to Javascript
   */
  public abstract Object function(Object[] arguments);

  public String getName() {
    return name;
  }
}
