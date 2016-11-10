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

package com.ephesoft.dcma.webservice.responseXML;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.BatchClasses;
import com.ephesoft.dcma.batch.schema.BatchInstances;
import com.ephesoft.dcma.batch.schema.Documents;
import com.ephesoft.dcma.batch.schema.ExtractKV;
import com.ephesoft.dcma.batch.schema.HocrPages;
import com.ephesoft.dcma.batch.schema.KVExtractionFieldPatterns;
import com.ephesoft.dcma.batch.schema.ListValues;
import com.ephesoft.dcma.batch.schema.Modules;

/**
 * This class is used for creating the result body for the output of web service.
 * 
 * @author Ephesoft
 * @version 3.0
 * @see RootElement
 * 
 */
@XmlRootElement
public class ResultElement {

	/*
	 * private Object batchClasses;
	 * 
	 * private Object batchInstances;
	 * 
	 * @XmlElement(type=BatchClasses.class) public void setBatchClasses(Object batchClasses) { this.batchClasses = batchClasses; }
	 * 
	 * public Object getBatchClasses() { return batchClasses; }
	 * 
	 * @XmlElement(type=BatchInstances.class) public void setBatchInstances(Object batchInstances) { this.batchInstances =
	 * batchInstances; }
	 * 
	 * public Object getBatchInstances() { return batchInstances; }
	 */

	/**
	 * An {@link Object} for storing the output received for web service hit.
	 */
	private Object resultBody;

	/**
	 * Getter for result body.
	 * 
	 * @return {@link Object}
	 */
	public Object getResultBody() {
		return resultBody;
	}

	/**
	 * Setter for result body.
	 * 
	 * @param resultBody {@link Object}
	 */
	@XmlElements( {@XmlElement(name = "Batch_Classes", type = BatchClasses.class),
			@XmlElement(name = "Batch_Instances", type = BatchInstances.class), @XmlElement(name = "Message", type = String.class),
			@XmlElement(name = "Modules", type = Modules.class), @XmlElement(name = "List", type = ListValues.class),
			@XmlElement(name = "Documents", type = Documents.class), @XmlElement(name = "ExtractKV", type = ExtractKV.class),
			@XmlElement(name = "Batch", type = Batch.class), @XmlElement(name = "Patterns", type = KVExtractionFieldPatterns.class),
			@XmlElement(name = "HocrPages", type = HocrPages.class),@XmlElement(name = "Documents", type = Batch.Documents.class)})
	public void setResultBody(final Object resultBody) {
		this.resultBody = resultBody;
	}

}
