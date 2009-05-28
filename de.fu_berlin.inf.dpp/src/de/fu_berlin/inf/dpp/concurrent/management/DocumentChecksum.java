package de.fu_berlin.inf.dpp.concurrent.management;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;

/**
 * This Class represents a checksum of an document. It contains the path, the
 * length and the hash code of the document.
 * 
 * @author chjacob
 */
public class DocumentChecksum {

    /**
     * Constant used for representing a missing file
     */
    public static final int NON_EXISTING_DOC = -1;

    protected IDocumentListener dirtyListener = new IDocumentListener() {

        public void documentAboutToBeChanged(DocumentEvent event) {
            // we are only interested in events after the change
        }

        public void documentChanged(DocumentEvent event) {
            dirty = true;
        }
    };

    // the path to the concurrent document
    private final IPath path;

    // the length of the document
    private int length;

    // the hash code of the document
    private int hash;

    protected IDocument document;

    protected boolean dirty;

    public DocumentChecksum(IPath path) {
        this.path = path;
        this.dirty = true;
    }

    public IPath getPath() {
        return path;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getHash() {
        return hash;
    }

    public void setHash(int hash) {
        this.hash = hash;
    }

    public void setDirty(boolean b) {
        this.dirty = b;
    }

    public void dispose() {
        unbind();
    }

    private void unbind() {
        if (document != null) {
            document.removeDocumentListener(dirtyListener);
        }
    }

    public void bind(IDocument doc) {

        if (this.document == doc)
            return;

        unbind();

        this.document = doc;

        if (document != null)
            doc.addDocumentListener(dirtyListener);

        dirty = true;
    }

    public void update() {

        // If document not changed, skip
        if (!dirty)
            return;

        if (document == null) {
            this.length = this.hash = NON_EXISTING_DOC;
        } else {
            this.length = document.getLength();
            this.hash = document.get().hashCode();
        }

        dirty = false;
    }

    /**
     * Returns whether this checksum represents a file which exists at the host.
     * 
     * If false is returned, then this checksum indicates that the host has no
     * file under the given path.
     */
    public boolean existsFile() {
        return !(this.length == NON_EXISTING_DOC && this.hash == NON_EXISTING_DOC);
    }

    @Override
    public String toString() {
        return path.toOSString() + " [" + this.length + "," + this.hash + "]";
    }
}