package saros.intellij.eventhandler.editor.document;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.picocontainer.Startable;
import saros.activities.SPath;
import saros.intellij.editor.EditorManager;
import saros.intellij.eventhandler.DisableableHandler;
import saros.intellij.filesystem.VirtualFileConverter;
import saros.session.ISarosSession;

/** Parent class containing utility methods when working with document listeners. */
public abstract class AbstractLocalDocumentModificationHandler
    implements DisableableHandler, Startable {

  private static final Logger LOG =
      Logger.getLogger(AbstractLocalDocumentModificationHandler.class);

  protected final EditorManager editorManager;
  private final ISarosSession sarosSession;

  private boolean enabled;
  private boolean disposed;

  /**
   * Sets the internal listener state flag to be disabled by default. The default state is set to
   * false as the document listener is only registered to the local IntelliJ instance when {@link
   * #setEnabled(boolean)} is called with <code>true</code>.
   *
   * @param editorManager the EditorManager instance
   */
  AbstractLocalDocumentModificationHandler(
      EditorManager editorManager, ISarosSession sarosSession) {

    this.editorManager = editorManager;
    this.sarosSession = sarosSession;

    this.enabled = false;
    this.disposed = false;
  }

  @Override
  public void start() {
    setEnabled(true);
  }

  @Override
  public void stop() {
    disposed = true;
    setEnabled(false);
  }

  /**
   * Enables or disables the given DocumentListener by registering or unregistering it from the
   * local Intellij instance. Also updates the local <code>enabled</code> flag.
   *
   * @param enabled the new state of the listener
   * @param documentListener the listener whose state to change
   */
  public void setEnabled(boolean enabled, @NotNull DocumentListener documentListener) {
    assert !disposed || !enabled : "disposed listeners must not be enabled";

    if (!this.enabled && enabled) {
      LOG.debug("Started listening for document events");

      EditorFactory.getInstance().getEventMulticaster().addDocumentListener(documentListener);

      this.enabled = true;

    } else if (this.enabled && !enabled) {

      LOG.debug("Stopped listening for document events");

      EditorFactory.getInstance().getEventMulticaster().removeDocumentListener(documentListener);

      this.enabled = false;
    }
  }

  /**
   * Returns whether the handler is enabled.
   *
   * @return whether the handle is enabled
   */
  public boolean isEnabled() {
    return enabled;
  }

  /**
   * Returns the SPath for the given document.
   *
   * @param document the document to get an SPath for
   * @return the SPath for the given document or <code>null</code> if no VirtualFile for the
   *     document could be found, the found VirtualFile could not be converted to an SPath or the
   *     resources represented by the given document is not shared
   * @see VirtualFileConverter#convertToSPath(VirtualFile)
   */
  final SPath getSPath(@NotNull Document document) {
    SPath path = editorManager.getFileForOpenEditor(document);

    if (path != null) {
      return path;
    }

    VirtualFile virtualFile = FileDocumentManager.getInstance().getFile(document);

    if (virtualFile == null) {
      if (LOG.isTraceEnabled()) {
        LOG.trace(
            "Ignoring event for document "
                + document
                + " - document is not known to the editor pool and a "
                + "VirtualFile for the document could not be found");
      }

      return null;
    }

    path = VirtualFileConverter.convertToSPath(virtualFile);

    if (path == null || !sarosSession.isShared(path.getResource())) {
      if (LOG.isTraceEnabled()) {
        LOG.trace("Ignoring Event for document " + document + " - document is not shared");
      }

      return null;
    }

    return path;
  }
}
