/********************************************************************************* 
* Ephesoft is a Intelligent Document Capture and Mailroom Automation program 
* developed by Ephesoft, Inc. Copyright (C) 2015 Ephesoft Inc. 
* 
* This program is free software; you can redistribute it and/or modify it under 
* the terms of the GNU Affero General Public License version 3 as published by the 
* Free Software Foundation with the addition of the following permission added 
* to Section 15 as permitted in Section 7(a): FOR ANY PART OF THE COVERED WORK 
* IN WHICH THE COPYRIGHT IS OWNED BY EPHESOFT, EPHESOFT DISCLAIMS THE WARRANTY 
* OF NON INFRINGEMENT OF THIRD PARTY RIGHTS. 
* 
* This program is distributed in the hope that it will be useful, but WITHOUT 
* ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS 
* FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more 
* details. 
* 
* You should have received a copy of the GNU Affero General Public License along with 
* this program; if not, see http://www.gnu.org/licenses or write to the Free 
* Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 
* 02110-1301 USA. 
* 
* You can contact Ephesoft, Inc. headquarters at 111 Academy Way, 
* Irvine, CA 92617, USA. or at email address info@ephesoft.com. 
* 
* The interactive user interfaces in modified source and object code versions 
* of this program must display Appropriate Legal Notices, as required under 
* Section 5 of the GNU Affero General Public License version 3. 
* 
* In accordance with Section 7(b) of the GNU Affero General Public License version 3, 
* these Appropriate Legal Notices must retain the display of the "Ephesoft" logo. 
* If the display of the logo is not reasonably feasible for 
* technical reasons, the Appropriate Legal Notices must display the words 
* "Powered by Ephesoft". 
********************************************************************************/ 

package com.ephesoft.dcma.openoffice;

import java.io.File;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Commandline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.core.common.ExecutorTask;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.OSUtil;

/**
 * Starts Open office service for the application.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 16-Dec-2013 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
public class OpenOfficeTask {

	/**
	 * Path to home directory of Open Office.
	 */
	private String homePath;

	/**
	 * soffice kill command for windows.
	 */
	private String killCommandForWindows;

	/**
	 * soffice start command for windows.
	 */
	private String startCommandForWindows;

	/**
	 * soffice kill command for unix.
	 */
	private String killCommandForUnix;

	/**
	 * soffice start command for unix.
	 */
	private String startCommandForUnix;

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * Method to start the service.
	 */
	public void start() {
		final Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				if (null != homePath) {

					// CR defect id #10982: Use EphesoftStringUtil.concatenate instead of custom StringBuilder.
					final String directoryPathBuilder = EphesoftStringUtil.concatenate(homePath, File.separator,
							OpenOfficeConstants.EXECUTION_FOLDER_NAME);
					final File file = new File(directoryPathBuilder);
					if (file.exists()) {
						try {

							// Fix for JIRA ISSUE #11146 Some warning message comes when tomcat server starts.
							final ExecutorTask execTask = new ExecutorTask();
							execTask.setDir(file);

							// Zoho task id#: 2779. Kill command added to End Open office process if already running on used port.
							runCommandForTaskKill(execTask);
							runCommandToStartOpenOffice(execTask);
						} catch (final BuildException e) {
							logger.error("Could not start Open Office Server.", e);
						}
					} else {
						logger.error("Open office's directory path doesn't exist. Could not start Open Office Server.");
					}
				} else {
					logger.error("Homepath is null. Could not start Open Office Server.");
				}
			}

			/**
			 * Executes command to kill open office service if already running.
			 * 
			 * @param commandBuilder {@link StringBuilder}
			 * @param execTask {@link ExecutorTask}
			 */
			private void runCommandForTaskKill(final ExecutorTask execTask) {
				if (null != execTask) {
					String command = null;
					if (OSUtil.isWindows()) {
						command = killCommandForWindows;
					} else if (OSUtil.isUnix()) {
						command = killCommandForUnix;
					}
					logger.info("Command for execution is: " + command);
					final Commandline commandline = new Commandline(command);
					execTask.setCommand(commandline);
					execTask.execute();
				}
			}

			/**
			 * Executes command to start open office service through Windows Command Processor and close terminal after it.
			 * 
			 * @param commandBuilder {@link StringBuilder}
			 * @param execTask {@link ExecutorTask}
			 */
			private void runCommandToStartOpenOffice(final ExecutorTask execTask) {
				if (null != execTask) {
					String command = null;
					if (OSUtil.isWindows()) {
						command = startCommandForWindows;
					} else if (OSUtil.isUnix()) {
						command = startCommandForUnix;
					}
					logger.info("Command for execution is: " + command);
					final Commandline commandline = new Commandline(command);
					execTask.setCommand(commandline);
					execTask.execute();
				}
			}
		});
		thread.start();
	}

	/**
	 * Sets server port on which service shall run.
	 * 
	 * @param homePath {@link String}
	 */
	public void setHomePath(final String homePath) {
		this.homePath = homePath;
	}

	/**
	 * Sets soffice kill command for windows.
	 * 
	 * @param killCommandForWindows {@link String}
	 */
	public void setKillCommandForWindows(final String killCommandForWindows) {
		this.killCommandForWindows = killCommandForWindows;
	}

	/**
	 * Sets soffice start command for windows.
	 * 
	 * @param startCommandForWindows {@link String}
	 */
	public void setStartCommandForWindows(final String startCommandForWindows) {
		this.startCommandForWindows = startCommandForWindows;
	}

	/**
	 * Sets soffice kill command for unix.
	 * 
	 * @param killCommandForUnix {@link String}
	 */
	public void setKillCommandForUnix(final String killCommandForUnix) {
		this.killCommandForUnix = killCommandForUnix;
	}

	/**
	 * Sets soffice start command for unix.
	 * 
	 * @param startCommandForUnix {@link String}
	 */
	public void setStartCommandForUnix(final String startCommandForUnix) {
		this.startCommandForUnix = startCommandForUnix;
	}
}
