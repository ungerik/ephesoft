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

package com.ephesoft.gxt.rv.client.util;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.da.domain.DocumentType;
import com.ephesoft.gxt.core.shared.exception.UIException;
import com.ephesoft.gxt.core.shared.util.BatchSchemaUtil;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.ValidationUtil;
import com.ephesoft.gxt.rv.shared.metadata.ReviewValidateMetaData;
import com.ephesoft.dcma.util.ApplicationConfigProperties;

public final class ReviewValidateDataConveter {

	public static ReviewValidateMetaData getBatchMetadata(final Batch batch, final BatchInstance batchInstance,
			final BatchSchemaService batchSchemaService, final PluginPropertiesService pluginPropertiesService) throws UIException {
		ReviewValidateMetaData metaData = null;
		try {
			if (null != batch && null != batchInstance && null != batchSchemaService) {
				metaData = new ReviewValidateMetaData();
				final String batchInstanceIdentifier = batch.getBatchInstanceIdentifier();
				metaData.setDocumentMetadata(BatchSchemaUtil.getDocumentMetaDataMap(batch, batchInstance.getStatus()));
				metaData.setBatchInstanceStatus(batchInstance.getStatus());
				metaData.setBatchInstanceIdentifier(batchInstanceIdentifier);
				metaData.setBatchInstanceName(batch.getBatchName());
				metaData.setBatchInstancePriority(batch.getBatchPriority());
				metaData.setBatchClassIdentifier(batch.getBatchClassIdentifier());
				metaData.setDocumentTypeNamesList(getDocumentTypeNamesList(batchInstance));
				final URL batchInstanceURL = batchSchemaService.getBatchContextURL(batchInstanceIdentifier);
				metaData.setBaseHTTPUrl(batchInstanceURL == null ? null : batchInstanceURL.toString());
				String zoomCountPropertyValue = ApplicationConfigProperties.getApplicationConfigProperties().getProperty("zoom_count");
				if (ValidationUtil.isValidNumericalValue(zoomCountPropertyValue, Integer.class)) {
					metaData.setZoomCount(Integer.parseInt(zoomCountPropertyValue));
				}
			}
		} catch (IOException ioException) {
			throw new UIException("Could not read the properties from the application Property file", ioException);
		}
		return metaData;
	}

	public static List<String> getDocumentTypeNamesList(final BatchInstance batchInstance) {
		final BatchClass batchClass = batchInstance == null ? null : batchInstance.getBatchClass();
		List<String> documentTypeNameList = null;
		if (null != batchClass) {
			final List<DocumentType> documentTypeList = batchClass.getDocumentTypes();
			if (!CollectionUtil.isEmpty(documentTypeList)) {
				documentTypeNameList = new ArrayList<String>();
				for (final DocumentType documentType : documentTypeList) {
					if (null != documentType) {
						documentTypeNameList.add(documentType.getName());
					}
				}
			}
		}
		return documentTypeNameList;
	}

}
