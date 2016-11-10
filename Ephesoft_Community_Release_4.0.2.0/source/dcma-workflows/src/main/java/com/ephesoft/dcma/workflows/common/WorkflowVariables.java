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

package com.ephesoft.dcma.workflows.common;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents the cluster of methods and variables for workflow process instance accessories.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> Oct 21, 2014 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
public class WorkflowVariables {

	/**
	 * {@link String} Variable for Batch instance ID object for the workflow process instance.
	 */
	public static final String BATCH_INSTANCE_ID = "batchInstanceID";

	/**
	 * {@link String} Variable for module name, from which restart of workflow is required, object for the workflow process instance.
	 */
	public static final String RESTART_WORKFLOW = "restartWorkflow";

	/**
	 * {@link String} Variable for denoting if the module execution is remote or not for the workflow process instance.
	 */
	public static final String IS_MODULE_REMOTE = "isModuleRemote";

	/**
	 * {@link String} Variable for key(batch instance identifier used) object for the workflow process instance.
	 */
	public static final String KEY = "key";

	/**
	 * {@link Map}<{@link String},{@link Object}> Map instance for all variables for a process instance of workflow.
	 */
	private Map<String, Object> variables;

	/**
	 * Constructor method to initiliaze variables.
	 */
	private WorkflowVariables() {
		this.variables = new HashMap<String, Object>();
	}

	/**
	 * Creates an instance of WorkFlow Variables.
	 * 
	 * @return {@link WorkflowVariables}
	 */
	public static WorkflowVariables create() {
		return new WorkflowVariables();
	}

	/**
	 * Get the variables.
	 * 
	 * @return {@link Map}<{@link String}, {@link Object}>
	 */
	public Map<String, Object> getVariables() {
		return variables;
	}

	/**
	 * Set the variables.
	 * 
	 * @param variables {@link Map}<{@link String}, {@link Object}>
	 */
	public void setVariables(final Map<String, Object> variables) {
		this.variables = variables;
	}

	/**
	 * Adds a variable to the map.
	 * 
	 * @param key {@link String}
	 * @param value {@link Object}
	 */
	public void addVariable(final String key, final Object value) {
		this.variables.put(key, value);
	}
}
