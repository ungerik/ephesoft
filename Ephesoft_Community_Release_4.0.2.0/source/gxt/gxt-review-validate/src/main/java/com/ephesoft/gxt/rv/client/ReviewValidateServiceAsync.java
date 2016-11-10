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

package com.ephesoft.gxt.rv.client;

import java.util.List;
import java.util.Map;

import com.ephesoft.dcma.batch.schema.Coordinates;
import com.ephesoft.dcma.batch.schema.DataTable;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage.Spans.Span;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.batch.schema.Row;
import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.gxt.core.client.DCMARemoteServiceAsync;
import com.ephesoft.gxt.core.shared.dto.DocumentTypeDTO;
import com.ephesoft.gxt.core.shared.dto.PointCoordinate;
import com.ephesoft.gxt.rv.shared.metadata.PluginPropertiesMetaData;
import com.ephesoft.gxt.rv.shared.metadata.ReviewValidateMetaData;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ReviewValidateServiceAsync extends DCMARemoteServiceAsync {

	public void getReviewValidateMetaData(String batchInstanceIdentifier, AsyncCallback<ReviewValidateMetaData> asyncCallback);

	public void getDocument(String batchInstanceIdentifier, String docIndentifier, AsyncCallback<Document> asyncCallback);

	public void saveBatch(final String batchInstanceIdentifier, final Map<String, Document> alteredDocumentsMap,
			final List<String> documentIdentifierList, AsyncCallback<Void> asyncCallback);

	public void duplicatePage(final String batchInstanceIdentifier, final Map<String, Document> alteredDocumentsMap,
			final List<String> documentIdentifierList, final String documentIdentifier, final String duplicatePageIdentifier,
			AsyncCallback<Void> asyncallAsyncCallback);

	public void getDocumentType(final String batchInstanceIdentifier, final String documentTypeName,
			final AsyncCallback<DocumentTypeDTO> callback);

	public void getHOCRContent(final PointCoordinate pointCoordinate1, final PointCoordinate pointCoordinate2,
			final String batchInstanceIdentifier, final String hocrFileName, final boolean rectangularCoordinateSet,
			final AsyncCallback<List<Span>> asyncCallback);

	public void getTableData(final Map<Integer, Coordinates> columnVsCoordinates, Document selectedDocument,
			DataTable selectedDataTable, String batchClassIdentifier, final String batchInstanceIdentifier, final String pageID,
			String hocrFileName, AsyncCallback<List<Row>> asyncCallback);

	public void getFdTypeByDocTypeName(final String batchInstanceIdentifier, final String docTypeName,
			final AsyncCallback<Document> callback);

	public void getHOCRContent(PointCoordinate pointCoordinate, String batchInstanceIdentifier, String hocrFileName,
			AsyncCallback<Span> callback);

	public void rotateImage(final String batchInstanceIdentifier, final Map<String, Document> alteredDocumentsMap,
			final List<String> documentIdentifierList, Page page, String documentId, AsyncCallback<Page> callback);

	public void getPluginConfigurations(String batchInstanceIdentifier, BatchInstanceStatus batchInstanceStatus,
			AsyncCallback<PluginPropertiesMetaData> callback);

	public void invalidate(final String batchInstanceIdentifier, AsyncCallback<Void> callback);

	public void signalWorkflow(final String batchInstanceIdentifier, final Map<String, Document> alteredDocumentsMap,
			final List<String> documentIdentifierList, AsyncCallback<Void> callback);

}
