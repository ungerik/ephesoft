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

package com.ephesoft.gxt.admin.client.event;

import com.google.web.bindery.event.shared.binder.GenericEvent;

public class SetSelectedBatchClassEvent extends GenericEvent {

	private boolean isCopyBatchClass = false;

	private boolean isExportBatchClass = false;

	private boolean isOpenBatchClass = false;

	private boolean isGenerateKey = false;

	public boolean isCopyBatchClass() {
		return isCopyBatchClass;
	}

	public void setCopyBatchClass(boolean isCopyBatchClass) {
		this.isCopyBatchClass = isCopyBatchClass;
	}

	public boolean isExportBatchClass() {
		return isExportBatchClass;
	}

	public void setExportBatchClass(boolean isExportBatchClass) {
		this.isExportBatchClass = isExportBatchClass;
	}

	public boolean isOpenBatchClass() {
		return isOpenBatchClass;
	}

	public void setOpenBatchClass(boolean isOpenBatchClass) {
		this.isOpenBatchClass = isOpenBatchClass;
	}

	public boolean isGenerateKey() {
		return isGenerateKey;
	}

	public void setGenerateKey(boolean isGenerateKey) {
		this.isGenerateKey = isGenerateKey;
	}

}
