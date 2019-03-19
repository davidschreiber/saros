package saros.ui.browser_functions;

import org.picocontainer.Startable;
import org.picocontainer.annotations.Inject;
import saros.HTMLUIContextFactory;
import saros.ui.browser.JavascriptFunction;
import saros.ui.ide_embedding.BrowserCreator;

/**
 * Browser functions that inherit from {@link SelfRegisteringJavascriptFunction} (and are added to
 * the dependency injection context, see {@link HTMLUIContextFactory}) will be automatically added
 * to the {@link BrowserCreator}, i.e. they will be injected to any created browser widget and can
 * be called from the JavaScript context.
 */
abstract class SelfRegisteringJavascriptFunction extends JavascriptFunction implements Startable {

  @Inject private BrowserCreator browserCreator;

  public SelfRegisteringJavascriptFunction(String name) {
    super(name);
  }

  @Override
  public void start() {
    browserCreator.addBrowserFunction(this);
  }

  @Override
  public void stop() {
    // do nothing
  }
}
