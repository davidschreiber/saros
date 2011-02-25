package de.fu_berlin.inf.dpp.stf.server.rmiSarosSWTBot.finder.remoteWidgets;

import java.rmi.RemoteException;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotList;

public class STFBotListImp extends AbstractRmoteWidget implements STFBotList {

    private static transient STFBotListImp listImp;

    private SWTBotList widget;

    /**
     * {@link STFBotListImp} is a singleton, but inheritance is possible.
     */
    public static STFBotListImp getInstance() {
        if (listImp != null)
            return listImp;
        listImp = new STFBotListImp();
        return listImp;
    }

    public STFBotList setWidget(SWTBotList list) {
        this.widget = list;
        return this;
    }

    /**************************************************************
     * 
     * exported functions
     * 
     **************************************************************/

    public STFBotMenu contextMenu(String text) throws RemoteException {
        return stfBotMenu.setWidget(widget.contextMenu(text));
    }

    /**********************************************
     * 
     * actions
     * 
     **********************************************/

    public void select(String item) throws RemoteException {
        widget.select(item);
    }

    public void select(int... indices) throws RemoteException {
        widget.select(indices);
    }

    public void select(int index) throws RemoteException {
        widget.select(index);
    }

    public void select(String... items) throws RemoteException {
        widget.select(items);
    }

    public void selectionCount() throws RemoteException {
        widget.selectionCount();
    }

    public void unselect() throws RemoteException {
        widget.unselect();
    }

    public void setFocus() throws RemoteException {
        widget.setFocus();
    }

    /**********************************************
     * 
     * states
     * 
     **********************************************/
    public String itemAt(int index) throws RemoteException {
        return widget.itemAt(index);
    }

    public int itemCount() throws RemoteException {
        return widget.itemCount();
    }

    public int indexOf(String item) throws RemoteException {
        return widget.indexOf(item);
    }

    public String[] getItems() throws RemoteException {
        return widget.getItems();
    }

    public boolean isEnabled() throws RemoteException {
        return widget.isEnabled();
    }

    public boolean isVisible() throws RemoteException {
        return widget.isVisible();
    }

    public boolean isActive() throws RemoteException {
        return widget.isActive();
    }

    public String getText() throws RemoteException {
        return widget.getText();
    }

    public String getToolTipText() throws RemoteException {
        return widget.getText();
    }

}
