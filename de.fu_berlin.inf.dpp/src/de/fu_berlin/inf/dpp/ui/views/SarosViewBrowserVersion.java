package de.fu_berlin.inf.dpp.ui.views;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Link;
import org.eclipse.ui.part.ViewPart;
import org.picocontainer.annotations.Inject;

import de.fu_berlin.inf.dpp.SarosPluginContext;
import de.fu_berlin.inf.dpp.ui.browser.EclipseBrowserCreator;
import de.fu_berlin.inf.dpp.ui.view_parts.SarosMainPage;

/**
 * This view displays an SWTBrowser. The Browser loads an HTML Page which
 * contains the actual UI including contact list, the Session view, and 1-to-1
 * chat. Please note that this new HTML-based UI is heavily under construction,
 * and is missing a lot of functionality yet.
 * <p/>
 * To use this view, alter the 'plugin.xml' by comment out the SarosView
 * 'view'tag and uncomment the the 'view' tag below which quote to
 * 'de.fu_berlin.inf.dpp.ui.views.SarosViewBrowserVersion'
 * <p/>
 * TODO: Find a way to switch between the different UI versions in runtime. This
 * is not a required feature, but for debugging and comparing useful.
 */
public class SarosViewBrowserVersion extends ViewPart {

    /**
     * The ID of the view as specified in the plugin manifest.
     */
    public static final String ID = "de.fu_berlin.inf.dpp.ui.views.SarosViewBrowserVersion";
    private static final String TROUBLESHOOTING_URL = "http://www.saros-project.org/troubleshooting";
    private static final Logger LOG = Logger
            .getLogger(SarosViewBrowserVersion.class);

    private Browser browser;

    @Inject
    private SarosMainPage sarosMainPage;

    public SarosViewBrowserVersion() {
        SarosPluginContext.initComponent(this);
    }

    @Override
    public void createPartControl(Composite parent) {

        try {
            browser = EclipseBrowserCreator.createBrowser(parent, SWT.NONE,
                    sarosMainPage);

        } catch (SWTError e) {
            // This might happen when there is no standard browser available
            LOG.error("Could not instantiate Browser: ", e);

            // Provide a LinkWidget instead
            Link link = new Link(parent, SWT.BORDER);
            link.setText("Saros couldn't initzialisieren the SWT browser widget to display the HTML UI. Find help here <A>"
                    + TROUBLESHOOTING_URL + "</A>.");
            link.setBounds(10, 10, 140, 40);
            return;
        }

        browser.addProgressListener(new ProgressListener() {

            @Override
            public void completed(ProgressEvent event) {
                LOG.debug(browser.getUrl() + " page has fully loaded");

            }

            @Override
            public void changed(ProgressEvent event) {
                LOG.debug(browser.getUrl() + " page is loading");

            }
        });

    }

    @Override
    public void setFocus() {
        if (browser != null) {
            browser.setFocus();
        }

    }

}
