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

package com.ephesoft.gxt.core.shared.exception;

public class UIArgumentException extends UIException {

	/**
	 * Serialized Version UID of the class.
	 */
	private static final long serialVersionUID = 1L;

	private String argument = "";

	/**
	 * Constructs a new UI Exception with <code>null</code> as its detail message. The cause is not initialized, and may subsequently
	 * be initialized by a call to {@link #initCause}.
	 */
	public UIArgumentException() {
		super();
	}

	/**
	 * Constructs a new exception with the specified detail message. The cause is not initialized, and may subsequently be initialized
	 * by a call to {@link #initCause}.
	 * 
	 * @param message the detail message. The detail message is saved for later retrieval by the {@link #getMessage()} method.
	 */

	public UIArgumentException(String message) {
		super(message);
	}

	public UIArgumentException(String message, String argument){
		super(message);
		this.argument = argument;
	}
	
	/**
	 * Constructs a new exception with the specified detail message and cause.
	 * <p>
	 * Note that the detail message associated with <code>cause</code> is <i>not</i> automatically incorporated in this exception's
	 * detail message.
	 * 
	 * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
	 * @param cause is the reason (which is saved for later retrieval by the {@link #getCause()} method). (A <tt>null</tt> value is
	 *            permitted, and indicates that the cause is nonexistent or unknown.)
	 */
	public UIArgumentException(String message, Throwable cause) {
		super(message, cause);
	}

	public String getArgument() {
		return argument;
	}

	public void setArgument(String argument) {
		this.argument = argument;
	}

}
