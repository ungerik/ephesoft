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

package com.ephesoft.dcma.docassembler;

import java.io.Serializable;

import com.ephesoft.dcma.batch.schema.DocField;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.docassembler.classification.engine.process.LucenePageProcess;
import com.ephesoft.dcma.util.EphesoftStringUtil;

/**
 * This class contains all the properties required for processing of advanced document assembler algorithm.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see LucenePageProcess
 */
public class AdvancedDocumentAssembler implements Serializable{

	/**
	 * default serial version id.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The index for pages that have been processed.
	 */
	int index;
	
	/**
	 * Flag for checking if only last page can come or not.
	 */
	boolean isLast;
	
	/**
	 * Flag for checking if first page has come or not.
	 */
	boolean isFirst;
	
	/**
	 * {@link Document} to store the current document that is open.
	 */
	Document currentDocument;
	
	/**
	 * {@link DocField} to store the current doc field type.
	 */
	DocField docPage;
	
	/**
	 * {@link Long} to store the identifier for current page.
	 */
	Long idGenerator;

	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @param index the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * @return the isLast
	 */
	public boolean isLast() {
		return isLast;
	}

	/**
	 * @param isLast the isLast to set
	 */
	public void setLast(final boolean isLast) {
		this.isLast = isLast;
	}

	/**
	 * @return the isFirst
	 */
	public boolean isFirst() {
		return isFirst;
	}

	/**
	 * @param isFirst the isFirst to set
	 */
	public void setFirst(boolean isFirst) {
		this.isFirst = isFirst;
	}

	/**
	 * @return the currentDocument
	 */
	public Document getCurrentDocument() {
		return currentDocument;
	}

	/**
	 * @param currentDocument the currentDocument to set
	 */
	public void setCurrentDocument(Document currentDocument) {
		this.currentDocument = currentDocument;
	}

	/**
	 * @return the docPage
	 */
	public DocField getDocPage() {
		return docPage;
	}

	/**
	 * @param docPage the docPage to set
	 */
	public void setDocPage(DocField docPage) {
		this.docPage = docPage;
	}

	/**
	 * @return the idGenerator
	 */
	public Long getIdGenerator() {
		return idGenerator;
	}

	/**
	 * @param idGenerator the idGenerator to set
	 */
	public void setIdGenerator(Long idGenerator) {
		this.idGenerator = idGenerator;
	}
	
	@Override
	public String toString(){
		return EphesoftStringUtil.concatenate("Index is ",index," First");
	}
}
