/**
 * 
 */
package de.fu_berlin.inf.dpp.net.business;

import java.util.List;

import org.apache.log4j.Logger;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.picocontainer.annotations.Inject;

import de.fu_berlin.inf.dpp.User;
import de.fu_berlin.inf.dpp.annotations.Component;
import de.fu_berlin.inf.dpp.concurrent.management.DocumentChecksum;
import de.fu_berlin.inf.dpp.concurrent.watchdog.ConsistencyWatchdogClient;
import de.fu_berlin.inf.dpp.net.JID;
import de.fu_berlin.inf.dpp.net.internal.XMPPChatReceiver;
import de.fu_berlin.inf.dpp.net.internal.extensions.ChecksumExtension;
import de.fu_berlin.inf.dpp.net.internal.extensions.PacketExtensionUtils;
import de.fu_berlin.inf.dpp.observables.SessionIDObservable;
import de.fu_berlin.inf.dpp.observables.SharedProjectObservable;
import de.fu_berlin.inf.dpp.project.ISharedProject;
import de.fu_berlin.inf.dpp.project.SessionManager;

/**
 * This class is responsible for processing a Checksum sent to us.
 */
@Component(module = "net")
public class ChecksumHandler {

    private static final Logger log = Logger.getLogger(ChecksumHandler.class);

    /* Dependencies */
    @Inject
    protected ConsistencyWatchdogClient watchdogClient;

    @Inject
    protected SharedProjectObservable sharedProject;

    protected SessionManager sessionManager;

    /* Fields */
    protected Handler handler;

    public ChecksumHandler(SessionManager sessionManager,
        XMPPChatReceiver receiver, SessionIDObservable sessionIDObservable) {

        this.sessionManager = sessionManager;
        this.handler = new Handler(sessionIDObservable);

        receiver.addPacketListener(handler, handler.getFilter());
    }

    protected class Handler extends ChecksumExtension {

        public Handler(SessionIDObservable sessionID) {
            super(sessionID);
        }

        @Override
        public PacketFilter getFilter() {
            return new AndFilter(super.getFilter(), PacketExtensionUtils
                .getSessionIDPacketFilter(sessionID));
        }

        @Override
        public void checksumsReceived(JID sender,
            List<DocumentChecksum> checksums) {

            ISharedProject project = sharedProject.getValue();

            if (project == null) {
                log.warn("Received checksum message from " + sender
                    + " but shared" + " project has already ended: "
                    + checksums);
                return;
            }

            User user = project.getUser(sender);
            if (user == null) {
                log.warn("Received checksum message from user who"
                    + " is not part of our shared project session: " + sender);
                return;
            }

            if (!user.isHost()) {
                log.warn("Received checksum message from user who"
                    + " is host: " + sender);
                return;
            }

            if (project.isHost()) {
                log.warn("Receiving checksums as host does not make sense");
                return;
            }

            watchdogClient.setChecksums(checksums);
            watchdogClient.checkConsistency();
        }
    }

}