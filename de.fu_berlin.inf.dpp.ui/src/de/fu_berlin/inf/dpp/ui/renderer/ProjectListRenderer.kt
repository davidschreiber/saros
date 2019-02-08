/**
 *
 */
package de.fu_berlin.inf.dpp.ui.renderer

import java.io.IOException
import org.apache.log4j.Logger
import de.fu_berlin.inf.ag_se.browser.extensions.IJQueryBrowser
import de.fu_berlin.inf.dpp.HTMLUIContextFactory
import de.fu_berlin.inf.dpp.HTMLUIStrings
import de.fu_berlin.inf.dpp.filesystem.IWorkspaceRoot
import de.fu_berlin.inf.dpp.ui.JavaScriptAPI
import de.fu_berlin.inf.dpp.ui.manager.ProjectListManager
import de.fu_berlin.inf.dpp.ui.model.ProjectTree

/**
 * This class is responsible for sending the Project list to the HTML UI.
 *
 * Because the concepts of what an actual project is, varies from IDE to IDE
 * this class use the {@link ProjectTree} model to form a common representation
 * for the UI to display.
 *
 * This class use a {@link IWorkspaceRoot} to received the necessary data for
 * the model creation.
 *
 * Created by PicoContainer
 *
 * @param projectListManager
 * the projectListManager
 * @see HTMLUIContextFactory
 */
class ProjectListRenderer (projectListManager: ProjectListManager?) : Renderer() {
	private val projectListManager: ProjectListManager? = projectListManager
	
	@Synchronized
	public override fun render(browser: IJQueryBrowser?) {
		// The project model creation shouldn't be done for every browser, only
		// the `SarosApi.trigger('updateProjectTrees', json)` needs to be called
		// for each page this renderer is associated with.
		// TODO: Change the Renderer interface to avoid multiple model
		// creations.
		try {
			projectListManager!!.createProjectModels()
		} catch (e: IOException) {
			LOG!!.error("Failed to load workspace resources: ", e)
			JavaScriptAPI.showError(
				browser,
				HTMLUIStrings.ERR_SESSION_PROJECT_LIST_IOEXCEPTION
			)
		}
		// FIXME: Workspaces with a big number of files cause massive
		// performance problems. See
		// https://github.com/saros-project/saros/issues/71
		JavaScriptAPI.updateProjects(
			browser,
			projectListManager!!.getProjectModels()
		)
}

	companion object {
		private val LOG = Logger
			.getLogger(ProjectListRenderer::class.java)
	}
}