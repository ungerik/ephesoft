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

package com.ephesoft.dcma.workflows.constant;

import com.ephesoft.dcma.core.component.ICommonConstants;

/**
 * This is a common constants file for Workflow plugin.
 * 
 * @author Ephesoft
 * @version 1.0
 */
public abstract class WorkflowConstants {

	/**
	 * {@link String}
	 */
	public static final int NUMBER_OF_PICK_UP_STATUS = 2;

	/**
	 * {@link String}
	 */
	public static final int NUMBER_OF_PAUSE_STATES = 2;

	/**
	 * {@link String}
	 */
	public static final int MAX_NUMBER_OF_JOB_RETRIES = 1;

	/**
	 * {@link String}
	 */
	public static final String ACTIVITY_ID_IN_PLUGIN = "-Plugin";

	/**
	 * {@link String}
	 */
	public static final String ACTIVITY_ID_IN_MODULE = "-Module";

	/**
	 * {@link String}
	 */
	public static final String WORKFLOW_CONTINUE_CHECK = "Workflow_Continue_Check";

	/**
	 * long
	 */
	public static final long DEFAULT_BATCH_RESOURCE_CLEAN_WAIT_TIME = 300000L;

	/**
	 * {@link String}
	 */
	public static final String ON = "ON";

	/**
	 * {@link String}
	 */
	public static final String MODULE_BUSINESS_KEY_SIGNATURE = "-m";

	/**
	 * {@link String}
	 */
	public static final String PLUGIN_BUSINESS_KEY_SIGNATURE = "-p";

	/**
	 * {@link String}
	 */
	public static final String ACTIVE_MODULE_NOT_FOUND = ICommonConstants.EMPTY_STRING;

	/**
	 * {@link String}
	 */
	public static final String ACTIVE_PLUGIN_NOT_FOUND = ICommonConstants.EMPTY_STRING;

	/**
	 * Variable for property name of batch picking algo. {@link String}
	 */
	public static final String BATCH_PICK_UP_ALGORITHM = "workflow.batchPickingAlgo";

	/**
	 * Variable for review document plugin. {@link String}
	 */
	public static final String REVIEW_DOCUMENT_PLUGIN = "Review_Document_Plugin";

	/**
	 * Variable for validate document plugin. {@link String}
	 */
	public static final String VALIDATE_DOCUMENT_PLUGIN = "Validate_Document_Plugin";

	/**
	 * Symbol for caret sign. char
	 */
	public static final char CARET = '^';

	/**
	 * Symbol for back slash sign. char
	 */
	public static final char BACK_SLASH = '\\';

	/**
	 * Symbol for forward slash sign. char
	 */
	public static final char FORWARD_SLASH = '/';

	/**
	 * Constant for not applicable. {@link String}
	 */
	public static final String NOT_APPLICABLE = "NA";

	/**
	 * Constant for space. {@link String}
	 */
	public static final String SPACE = " ";

	/**
	 * Constant for encoded plus. {@link String}
	 */
	public static final String ENCODED_PLUS = "%20";

	/**
	 * {@link String}
	 */
	public static final String RESTART_ERROR_MESSAGE = "restart_error_message";

	/**
	 * variable for Workflow Status Running module. {@link String}
	 */
	public static final String WORKFLOW_STATUS_RUNNING = "Workflow_Status_Running";

	/**
	 * variable name for Resume Evaluation result. {@link String}
	 */
	public static final String RESUME_EVALUATION_RESULT_VARIABLE = "resumeEvaluationResult";

	/**
	 * variable name for Review Evaluation result. {@link String}
	 */
	public static final String REVIEW_EVALUATION_RESULT_VARIABLE = "reviewEvaluationResult";

	/**
	 * variable name for Review Evaluation result. {@link String}
	 */
	public static final String VALIDATION_EVALUATION_RESULT_VARIABLE = "validationEvaluationResult";

	/**
	 * constant for yes. {@link String}
	 */
	public static final String YES = "yes";

	/**
	 * constant for no. {@link String}
	 */
	public static final String NO = "no";

	/**
	 * {@link String}
	 */
	public static final String META_INF_DCMA_WORKFLOWS = "META-INF\\dcma-workflows";

	/**
	 * {@link String}
	 */
	public static final String PLUGINS_CONSTANT = "plugins";

	/**
	 * {@link String}
	 */
	public static final String MODULES_CONSTANT = "modules";

	/**
	 * {@link String}
	 */
	public static final String WORKFLOWS_CONSTANT = "workflows";

	/**
	 * {@link String}
	 */
	public static final String DCMA_WORKFLOWS_PROPERTIES = "dcma-workflows.properties";

	/**
	 * {@link String}
	 */
	public static final String WORKFLOW_DEPLOY = "workflow.deploy";

	/**
	 * {@link String}
	 */
	public static final String WORKFLOW_STATUS_FINISHED = "Workflow_Status_Finished";

	/**
	 * {@link String}
	 */
	public static final String START_CONSTANT = "start";

	/**
	 * {@link String}
	 */
	public static final String END_CONSTANT = "end";

	/**
	 * {@link String}
	 */
	public static final String RESUME_DECISION_CLASS = "com.ephesoft.dcma.workflows.decisionevaluator.ResumeEvaluation";

	/**
	 * {@link String}
	 */
	public static final String REVIEW_DECISION_CLASS = "com.ephesoft.dcma.workflows.decisionevaluator.ReviewEvaluation";

	/**
	 * {@link String}
	 */
	public static final String VALIDATION_DECISION_CLASS = "com.ephesoft.dcma.workflows.decisionevaluator.ValidationEvaluation";

	/**
	 * {@link String}
	 */
	public static final String IS_REVIEW_REQUIRED = "reviewEvaluation";

	/**
	 * {@link String}
	 */
	public static final String IS_VALIDATION_REQUIRED = "validationEvaluation";

	/**
	 * {@link String}
	 */
	public static final String RESUME_DECISION_GATEWAY = "Resume_Option";

	/**
	 * {@link String}
	 */
	public static final String VALIDATION_DECISION_GATEWAY = "is-validation-required";

	/**
	 * {@link String}
	 */
	public static final String REVIEW_DECISION_GATEWAY = "is-review-required";

	/**
	 * {@link String}
	 */
	public static final String SCRIPT_NAME_VARIABLE = "scriptName";

	/**
	 * {@link String}
	 */
	public static final String BACKUP_FILE_NAME_VARIABLE = "backupFileName";

	/**
	 * {@link String}
	 */
	public static final String RESUME_EVALUATOR_TASK_NAME = "resumeEvaluation";

	/**
	 * {@link String}
	 */
	public static final String MODULE_EXECUTION_START_LISTENER_EXPRESSION = "${moduleExecutionStartListener}";

	/**
	 * {@link String}
	 */
	public static final String MODULE_EXECUTION_END_LISTENER_EXPRESSION = "${moduleExecutionEndListener}";

	/**
	 * {@link String}
	 */
	public static final String VALIDATION_DECISION = "validationDecision";
	/**
	 * {@link String}
	 */
	public static final String REVIEW_DECISION = "reviewDecision";

	/**
	 * {@link String}
	 */
	public static final String CURLY_BRACES_BEGIN = "{";

	/**
	 * {@link String}
	 */
	public static final String CURLY_BRACES_END = "}";

	/**
	 * {@link String}
	 */
	public static final String SINGLE_QUOTE = "'";

	/**
	 * {@link String}
	 */
	public static final String CHECK_EQUALITY = "==";

	/**
	 * {@link String}
	 */
	public static final String DOLLAR = "$";

	/**
	 * {@link String}
	 */
	public static final String COMMA = ",";

	/**
	 * {@link String}
	 */
	public static final String PERIOD = ".";

	/**
	 * {@link String}
	 */
	public static final String BEGIN_ROUND_BRACKETS = "(";

	/**
	 * {@link String}
	 */
	public static final String CLOSE_ROUND_BRACKETS = ")";

	/**
	 * {@link String}
	 */
	public static final String UNDERSCORE = "_";

	/**
	 * {@link String}
	 */
	public static final String BC3 = "BC3";

	public static final int DEFAULT_JOB_EXECUTOR_QUEUE_SIZE = 3;
	public static final int DEFAULT_JOB_EXECUTOR_CORE_POOL_SIZE = 3;
	public static final int FACTOR_FOR_JOB_LOCK_TIME = 2 * 12;
	public static final int FACTOR_FOR_MAX_POOL_SIZE = 2;

	public static final String PLUGIN_EXECUTION_LISTENER_EXPRESSION = "${pluginExecutionStartListener}";
	public static final String PRE_REVIEW_BACKUP_NAME = "Pre_Review_Document_Plugin";
	public static final String POST_REVIEW_BACKUP_NAME = "Post_Review_Document_Plugin";
	public static final String PRE_VALIDATION_BACKUP_NAME = "Pre_Validate_Document_Plugin";
	public static final String POST_VALIDATION_BACKUP_NAME = "Post_Validate_Document_Plugin";
	
	public static final String WORKFLOW_UNIT_END_INDICATOR = "end";

	/**
	 * Int constant for comparison result when object being compared is less than object being compared to.
	 */
	public static final int LESS_COMPARISON = -1;
	
	/**
	 * Int constant for comparison result when object being compared is less than object being compared to.
	 */
	public static final int GREATER_COMPARISON = 1;
	
	/**
	 * {@link String} Constant for lower case "module".
	 */
	public static final String MODULE_KEYWORD_LOWER_CASE = "module";
	
	/**
	 * {@link String} Constant for camel case "module".
	 */
	public static final String MODULE_KEYWORD_CAMEL_CASE = "Module";
	
	/**
	 * {@link String} Constant for lower case "plugin".
	 */
	public static final String PLUGIN_KEYWORD_LOWER_CASE = "plugin";
	
	/**
	 * {@link String} Constant for camel case "plugin".
	 */
	public static final String PLUGIN_KEYWORD_CAMEL_CASE = "Plugin";
}
