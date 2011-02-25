package de.fu_berlin.inf.dpp.stf.server.rmiSarosSWTBot.sarosFinder.remoteComponents.views.sarosViews;

import java.rmi.RemoteException;

import de.fu_berlin.inf.dpp.stf.server.rmiSarosSWTBot.sarosFinder.remoteComponents.SarosComponentImp;

public class RSViewImp extends SarosComponentImp implements RSView {
    // public static RemoteScreenViewObjectImp classVariable;

    private static transient RSViewImp self;

    /**
     * {@link ChatViewImp} is a singleton, but inheritance is possible.
     */
    public static RSViewImp getInstance() {
        if (self != null)
            return self;
        self = new RSViewImp();
        return self;
    }

    /**************************************************************
     * 
     * exported functions
     * 
     **************************************************************/

    /**********************************************
     * 
     * actions
     * 
     **********************************************/
    public void clickTBChangeModeOfImageSource() throws RemoteException {
        preCondition();
        bot().view(VIEW_REMOTE_SCREEN)
            .toolbarButton(TB_CHANGE_MODE_IMAGE_SOURCE)
            .click();
    }

    public void clickTBStopRunningSession() throws RemoteException {
        preCondition();
        bot().view(VIEW_REMOTE_SCREEN).toolbarButton(TB_STOP_RUNNING_SESSION)
            .click();
    }

    public void clickTBResume() throws RemoteException {
        preCondition();
        bot().view(VIEW_REMOTE_SCREEN).toolbarButton(TB_RESUME)
            .click();
    }

    public void clickTBPause() throws RemoteException {
        preCondition();
        bot().view(VIEW_REMOTE_SCREEN).toolbarButton(TB_PAUSE)
            .click();
    }

    public void confirmShellIncomingScreensharingSession(String YesOrNot)
        throws RemoteException {
        bot().shell(SHELL_INCOMING_SCREENSHARING_SESSION).confirm(YesOrNot);
    }

    /**********************************************
     * 
     * Waits until
     * 
     **********************************************/
    public void waitUntilRemoteScreenViewIsActive() throws RemoteException {
        bot().view(VIEW_REMOTE_SCREEN).waitUntilIsActive();
    }

    /**************************************************************
     * 
     * inner functions
     * 
     **************************************************************/

    private void preCondition() throws RemoteException {
        bot().openViewById(VIEW_REMOTE_SCREEN_ID);
        bot().view(VIEW_REMOTE_SCREEN).show();

    }
}
