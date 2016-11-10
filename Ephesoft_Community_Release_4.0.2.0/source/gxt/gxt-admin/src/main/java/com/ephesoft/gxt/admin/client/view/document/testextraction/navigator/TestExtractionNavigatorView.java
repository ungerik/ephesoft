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

package com.ephesoft.gxt.admin.client.view.document.testextraction.navigator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.DataTable;
import com.ephesoft.dcma.batch.schema.DocField;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.batch.schema.Document.DataTables;
import com.ephesoft.dcma.batch.schema.Document.DocumentLevelFields;
import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.presenter.document.testextraction.TestExtractionCompositePresenter;
import com.ephesoft.gxt.admin.client.presenter.document.testextraction.navigator.TestExtractionNavigatorPresenter;
import com.ephesoft.gxt.admin.client.widget.TestExtractionNavigationTree;
import com.ephesoft.gxt.admin.client.widget.property.TestExtractionNavigationNode;
import com.ephesoft.gxt.core.client.View;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

public class TestExtractionNavigatorView extends View<TestExtractionNavigatorPresenter> {

	@SuppressWarnings("rawtypes")
	@UiField
	protected TestExtractionNavigationTree navigationTree;

	@UiField
	protected VerticalLayoutContainer treeContainer;

	interface Binder extends UiBinder<Component, TestExtractionNavigatorView> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	public TestExtractionNavigatorView() {
		super();
		initWidget(binder.createAndBindUi(this));
		setWidgetIds();
		navigationTree.addStyleName("testExtractionNavigationTree");
		treeContainer.addStyleName("testExtractionNavigationTreeContainer");
	}

	private void setWidgetIds() {
		WidgetUtil.setID(navigationTree, "bcm-testExtraction-tree");
	}

	@Override
	public void initialize() {

	}

	public void createTree(Batch batch) {
		Map<String, String> pagesMap = new HashMap<String, String>();
		List<Document> documentsList = batch.getDocuments().getDocument();
		if (!CollectionUtil.isEmpty(documentsList)) {
			TestExtractionCompositePresenter<?, DocumentLevelFields> dlfCompositePresenter = presenter.getDlfCompositePresenter();
			TestExtractionCompositePresenter<?, DataTable> dataTableCompositePresenter = presenter.getDataTablesCompositePresenter();
			for (Document document : documentsList) {
				if (null != document) {
					List<Page> pagesList = null;
					if (null != document.getPages()) {
						pagesList = document.getPages().getPage();
						mapPageIdWithName(pagesList, pagesMap);
					}
					String pagesStartTag = null;
					String pagesEndTag = null;
					if (!CollectionUtil.isEmpty(pagesList)) {
						pagesStartTag = pagesList.get(0).getIdentifier();
						pagesEndTag = pagesList.get(pagesList.size() - 1).getIdentifier();
					}
					String parentNodeDisplayParameter = null;
					if (pagesStartTag.equalsIgnoreCase(pagesEndTag) && null != pagesEndTag && null != pagesStartTag) {
						parentNodeDisplayParameter = StringUtil.concatenate(document.getIdentifier(), " ",
								BatchClassConstants.OPEN_BRACKET, document.getType(), BatchClassConstants.CLOSE_BRACKET, " ", "[",
								pagesStartTag, "]");
					} else if (null != pagesEndTag && null != pagesStartTag) {
						parentNodeDisplayParameter = StringUtil.concatenate(document.getIdentifier(), " ",
								BatchClassConstants.OPEN_BRACKET, document.getType(), BatchClassConstants.CLOSE_BRACKET, " ", "[",
								pagesStartTag, "-", pagesEndTag, "]");
					} else {
						parentNodeDisplayParameter = StringUtil.concatenate(document.getIdentifier(), " ",
								BatchClassConstants.OPEN_BRACKET, document.getType(), BatchClassConstants.CLOSE_BRACKET);
					}
					TestExtractionNavigationNode<Document> newNode = new TestExtractionNavigationNode<Document>(
							parentNodeDisplayParameter, null, document);
					navigationTree.addRoot(newNode);
					addExtractedDlfNode(document, dlfCompositePresenter, newNode);
					addDataTablesNode(document, dataTableCompositePresenter, newNode);
				}
			}
			presenter.setPageIdMappingWithName(pagesMap);
		}
	}

	private void addDataTablesNode(Document document, TestExtractionCompositePresenter<?, DataTable> dataTableCompositePresenter,
			TestExtractionNavigationNode<Document> newNode) {
		DataTables dataTables = document.getDataTables();
		if (null != dataTables && !CollectionUtil.isEmpty(dataTables.getDataTable())) {
			TestExtractionNavigationNode<DataTables> dataTablesNode = new TestExtractionNavigationNode<DataTables>(
					LocaleDictionary.getConstantValue(BatchClassConstants.DATA_TABLE_NODE), null, dataTables);
			navigationTree.addChild(newNode, dataTablesNode);
			addDataTableNode(dataTables, dataTablesNode, dataTableCompositePresenter);
		}
	}

	private void addDataTableNode(DataTables dataTables, TestExtractionNavigationNode<DataTables> dataTablesNode,
			TestExtractionCompositePresenter<?, DataTable> dataTableCompositePresenter) {
		if (!CollectionUtil.isEmpty(dataTables.getDataTable())) {
			for (DataTable dataTable : dataTables.getDataTable()) {
				TestExtractionNavigationNode<DataTable> dataTableNode = new TestExtractionNavigationNode<DataTable>(
						dataTable.getName(), dataTableCompositePresenter, dataTable);
				navigationTree.addChild(dataTablesNode, dataTableNode);
			}
		}
	}

	private void addExtractedDlfNode(Document document,
			TestExtractionCompositePresenter<?, DocumentLevelFields> dlfCompositePresenter,
			TestExtractionNavigationNode<Document> newNode) {
		DocumentLevelFields dlfs = document.getDocumentLevelFields();
		if (null != dlfs && !CollectionUtil.isEmpty(dlfs.getDocumentLevelField()) && isExtractionResultsFound(dlfs.getDocumentLevelField())) {
			TestExtractionNavigationNode<DocumentLevelFields> extractedDlfNode = new TestExtractionNavigationNode<DocumentLevelFields>(
					LocaleDictionary.getConstantValue(BatchClassConstants.EXTRACTED_DLF_NODE), dlfCompositePresenter, dlfs);
			navigationTree.addChild(newNode, extractedDlfNode);
		}
	}

	private boolean isExtractionResultsFound(List<DocField> documentLevelField) {
		boolean extractionFound = false;
		for(DocField docField : documentLevelField){
			if(null != docField && !StringUtil.isNullOrEmpty(docField.getValue())){
				extractionFound = true;
				break;
			}
		}
		return extractionFound;
	}

	private void mapPageIdWithName(List<Page> pagesList, Map<String, String> pagesMap) {
		if (!CollectionUtil.isEmpty(pagesList)) {
			for (Page page : pagesList) {
				if (null != page) {
					pagesMap.put(page.getIdentifier(), page.getOldFileName());
				}
			}
		}
	}

	public void clearTreeStore() {
		TreeStore<TestExtractionNavigationNode> ts = navigationTree.getStore();
		ts.clear();
	}
}
