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

package com.ephesoft.dcma.core.common;
 																						
import java.io.Serializable;

/**
 * Class to keep index field successfully inserted, already exist and getting error while inserting in db.
 * 
 * @author ephesoft
 * @version 1.0
 * 
 */

public class ImportExportIndexField implements Serializable {

	private static final long serialVersionUID = 1L;
	private String indexFieldName;
	private boolean isAlreadyExist;
	private boolean isError;
	private boolean isSerError;

	public ImportExportIndexField() {
		super();
		this.indexFieldName = "";
		this.isAlreadyExist = false;
		this.isError = false;
	}

	public ImportExportIndexField(String indexField, boolean isAlreadyExist, boolean isError, boolean isSerError) {
		super();
		this.indexFieldName = indexField;
		this.isAlreadyExist = isAlreadyExist;
		this.isError = isError;
		this.isSerError = isSerError;
	}

	public String getIndexFieldName() {
		return indexFieldName;
	}

	public void setIndexFieldName(String indexFieldName) {
		this.indexFieldName = indexFieldName;
	}

	public boolean isAlreadyExist() {
		return isAlreadyExist;
	}

	public void setAlreadyExist(boolean isAlreadyExist) {
		this.isAlreadyExist = isAlreadyExist;
	}

	public boolean isError() {
		return isError;
	}

	public void setError(boolean isError) {
		this.isError = isError;
	}

	public boolean isSerError() {
		return isSerError;
	}

	public void setSerError(boolean isSerError) {
		this.isSerError = isSerError;
	}

}
