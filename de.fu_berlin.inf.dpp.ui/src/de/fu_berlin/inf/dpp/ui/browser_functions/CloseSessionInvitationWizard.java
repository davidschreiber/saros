package de.fu_berlin.inf.dpp.ui.browser_functions;

import de.fu_berlin.inf.ag_se.browser.functions.JavascriptFunction;
import de.fu_berlin.inf.dpp.ui.ide_embedding.DialogManager;
import de.fu_berlin.inf.dpp.ui.webpages.SessionWizardPage;

/**
 * Offers a via Javascript invokable method to close an open
 * {@link SessionWizardPage} Dialog.
 * <p>
 * JS-signature: "void __java_closeStartSessionWizard();"
 */
public class CloseSessionInvitationWizard extends JavascriptFunction {
    private DialogManager dialogManager;
    public static final String JS_NAME = "closeStartSessionWizard";

    public CloseSessionInvitationWizard(DialogManager dialogManager) {
        super(NameCreator.getConventionName(JS_NAME));
        this.dialogManager = dialogManager;
    }

    @Override
    public Object function(Object[] arguments) {
        this.dialogManager.closeDialogWindow(SessionWizardPage.WEB_PAGE);
        return null;
    }

}