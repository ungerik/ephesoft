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

package com.ephesoft.dcma.gwt.core.shared.common;

import java.util.HashMap;
import java.util.Map;

import com.ephesoft.dcma.batch.schema.DataTable;
import com.ephesoft.dcma.batch.schema.DocField;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * This class <code>StickyDocumentData</code> holds the document data to be persisted when document type is changed on the validation
 * screen.
 * <p>
 * Currently this class holds references to the document level fields which are to be stored. In future more data with respect to
 * document, needed to be retained, can be added, like {@link DataTable}
 * </p>
 * 
 * <p>
 * 
 * </p>
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 17-Jul-2013 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */

public class StickyDocumentData implements IsSerializable {

	/**
	 * Holds the fields whose data is to be retained.
	 */
	private Map<String, DocField> stickyFieldsMap;

	/**
	 * Creates new instance.
	 */
	public StickyDocumentData() {
		super();
		this.stickyFieldsMap = new HashMap<String, DocField>();
	}

	/**
	 * Returns the {@link DocField} document level field corresponding to the field name.
	 * 
	 * @param fieldName {@link String} document level field name whose field details are to be fetched
	 * @return
	 */
	public DocField getDocField(final String fieldName) {
		return stickyFieldsMap.get(fieldName);
	}

	/**
	 * Adds the document level field to the sticky fields map.
	 * 
	 * @param fieldName {@link String} document level field name
	 * @param docField {@link DocField} document level field
	 */
	public void addDocField(final String fieldName, final DocField docField) {
		this.stickyFieldsMap.put(fieldName, docField);
	}

}
