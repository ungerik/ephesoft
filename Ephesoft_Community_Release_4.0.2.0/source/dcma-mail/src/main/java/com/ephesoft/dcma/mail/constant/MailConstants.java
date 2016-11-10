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

package com.ephesoft.dcma.mail.constant;


/**
 * @author Ephesoft
 * 
 */
public interface MailConstants {

	/**
	 * String constant for receiver name.
	 */
	String RECEIVER_NAME = "receiverName";

	/**
	 * String constant for sender name.
	 */
	String SENDER_NAME = "senderName";

	/**
	 * String constant for user name.
	 */
	String USER_NAME = "userName";

	/**
	 * String constant for password.
	 */
	String PASSWORD = "password";

	/**
	 * String constant for single comma delimeter.
	 */
	String SINGLE_COMMA_DELIMITER = ",";
	
	/**
	 * String constant for single comma delimeter.
	 */
	String EXCEPTION_STACKTRACE = "exceptionStackTrace";
	
	/**
	 * String constant for single comma delimeter.
	 */
	String USER_EMAIL_ID = "userEmailId";
	
	/**
	 * String constant for empty string.
	 */
	String EMPTY_STRING = "";
	
	/**
	 * String constant for company name of registered user.
	 */
	String COMPANY_NAME = "companyName";
	
	/**
	 * String constant for contact number of registered user.
	 */
	String CONTACT_NUMBER = "phoneNumber";
	
	/**
	 * String constant for space.
	 */
	String SPACE = " ";
	
	/**
	 * String constant for greaterThan symbol.
	 */
	String GREATER_SYMBOL = ">";
	
	/**
	 * String constant for lessThan symbol.
	 */
	String LESS_SYMBOL = "<";
	/**
	 * Constant for date format
	 */
	String DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss Z";
	/**
	 * Constant for Subject string
	 */
	String SUBJECT = "Subject";
	/**
	 * Constant for FROM string
	 */
	String FROM = "From";
	/**
	 * Constant for To string
	 */
	String TO = "To";
	/**
	 * Constant for Cc string
	 */
	String CC = "Cc";
	/**
	 * Constant for ReceivedDate string
	 */
	String RECEIVED_DATE = "ReceivedDate";
	/**
	 * Constant for Date string
	 */
	String DATE_STRING = "Date";
	/**
	 * Constant for null string
	 */
	String NULL_STRING = "null";
	/**
	 * Constant for batchClass string
	 */
	String BATCH_CLASS = "batchClass";
	/**
	 * Constant for userId string
	 */
	String USER_ID = "userId";
	/**
	 * Constant for closing brackets.
	 */
	String CLOSING_BRACKET = "]";
	/**
	 * Constant for opening brackets.
	 */
	String OPENING_BRACKET = "[";
	/**
	 * Constant for workflow string.
	 */
	String WORKFLOW = "workflow";
	/**
	 * Constant for batchInstance string.
	 */
	String BATCH_INSTANCE = "batchInstance";
	/**
	 * Constant for batchName string.
	 */
	String BATCH_NAME = "batchName";
	/**
	 * Constant for errorMessage string.
	 */
	String ERROR_MESSAGE = "errorMessage";
	/**
	 * Constant for errorLog string.
	 */
	String ERROR_LOG = "errorLog";
	/**
	 * Constant for errorPlugin String.
	 */
	String ERROR_PLUGIN = "errorPlugin";
	/**
	 * Constant for mailMeta String
	 */
	String MAIL_META = "mailMeta";
	/**
	 * Mail smtp socket property
	 */
	String MAIL_SOCKET_PROPERTY= "mail.smtp.socketFactory.class";
	/**
	 * Mail smtp socket class
	 */
	String MAIL_SOCKET_CLASS="javax.net.ssl.SSLSocketFactory";
	/**
	 * Mail Smtp ssl enable
	 */
	String MAIL_SMTP_SSL_PROPERTY="mail.smtp.ssl.enable";
	/**
	 * True String
	 */
	String TRUE="true";
}
