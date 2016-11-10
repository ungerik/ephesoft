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

package com.ephesoft.dcma.docassembler.classification;

import java.util.List;

import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.docassembler.DocumentAssembler;

/**
 * This class does the actual processing of document assembly. It will read all the pages of the document type Unknown and then create
 * new documents for all these pages found.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.docassembler.DocumentAssembler
 */
public interface DocumentClassification {

	/**
	 * This method will process all the unclassified pages present at document type Unknown. Process every page one by one and create
	 * new documents.
	 * 
	 * @param documentAssembler DocumentAssembler
	 * @param batchInstanceID Long
	 * @throws DCMAApplicationException If any invalid parameter found.
	 */
	void processUnclassifiedPages(final DocumentAssembler documentAssembler, final String batchInstanceIdentifierIdentifier,
			final PluginPropertiesService pluginPropertiesService) throws DCMAApplicationException;

	/**
	 * Processes passed document list and does reclassification for each document. Reclassification is done for documents whose
	 * document type information not present.
	 * 
	 * @param batchInstanceIdentifer
	 * @param batchClassIdentifier
	 * @param documentList
	 * @throws DCMAApplicationException
	 */
	void processDocumentTypes(DocumentAssembler documentAssembler, String batchInstanceIdentifer, String batchClassIdentifier,
			List<Document> documentList) throws DCMAApplicationException;

}
