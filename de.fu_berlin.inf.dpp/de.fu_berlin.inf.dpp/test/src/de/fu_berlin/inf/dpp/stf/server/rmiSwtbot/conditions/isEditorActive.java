package de.fu_berlin.inf.dpp.stf.server.rmiSwtbot.conditions;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.waits.DefaultCondition;

import de.fu_berlin.inf.dpp.stf.server.rmiSwtbot.eclipse.noExportedObjects.EditorPart;

public class isEditorActive extends DefaultCondition {

    private EditorPart editor;
    private String fileName;

    isEditorActive(EditorPart editor, String fileName) {
        this.fileName = fileName;
        this.editor = editor;
    }

    public String getFailureMessage() {

        return null;
    }

    public boolean test() throws Exception {
        try {
            SWTBotEditor e = editor.getEditor(fileName);
            return e.isActive();
        } catch (WidgetNotFoundException e) {
            return false;
        }

    }

}
