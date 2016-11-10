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

package com.ephesoft.dcma.batch.dao.xml;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.core.component.JAXB2Template;
import com.ephesoft.dcma.core.dao.xml.XmlDao;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.logger.EphesoftLogger;
import com.ephesoft.dcma.util.logger.EphesoftLoggerFactory;

/**
 * @author Ephesoft
 * @version 1.0
 * 
 */
@Repository
public class BatchSchemaDao extends XmlDao<Batch> {

	@Autowired
	@Qualifier("batchJAXB2Template")
	private JAXB2Template jaxb2Template;

	/**
	 * Object used for synchronisation.
	 */
	private Object object = new Object();

	/**
	 * Instance of Logger.
	 */
	private static final EphesoftLogger LOGGER = EphesoftLoggerFactory.getLogger(BatchSchemaDao.class);

	/**
	 * Map to hold In-Memory Batch XMLs {@link Map}<{@link String},{@link Batch}>
	 */
	private Map<String, Batch> batchXMLMemoryMap;

	/**
	 * Fetches the XML Map.
	 * 
	 * @return {@link Map}<{@link String},{@link Batch}>
	 */
	public Map<String, Batch> getBatchXMLMemoryMap() {
		if (null == batchXMLMemoryMap) {
			synchronized (object) {
				if (null == batchXMLMemoryMap) {
					batchXMLMemoryMap = new ConcurrentHashMap<String, Batch>();
				}
			}
		}
		return batchXMLMemoryMap;
	}

	/**
	 * Returns a referenced Set of all keys currently present in the XML Map.
	 * 
	 * @return {@link Set}<{@link String}>
	 */
	public Set<String> getKeys() {
		Set<String> keySet = null;
		if (null != batchXMLMemoryMap) {
			LOGGER.info("Fetching KeySet for BatchXML Map.");
			keySet = batchXMLMemoryMap.keySet();
		}
		return keySet;
	}

	@Override
	public JAXB2Template getJAXB2Template() {
		return this.jaxb2Template;
	}

	/**
	 * Fetches batch XML map entry for a batch instance
	 * 
	 * @param batchInstanceIdentifier {@link String}
	 * @return {@link Batch}
	 */
	public Batch getInMemoryBatch(final Serializable batchInstanceIdentifier) {
		Batch batch;
		if (null == batchXMLMemoryMap) {
			batch = null;
		} else {
			LOGGER.info(EphesoftStringUtil.concatenate("Fetching In-Memory Batch for :", batchInstanceIdentifier));
			batch = batchXMLMemoryMap.get(batchInstanceIdentifier.toString());
		}
		return batch;
	}

	/**
	 * Updates batch XML map entry for a batch instance
	 * 
	 * @param batch {@link Batch}
	 * @param batchInstanceIdentifier {@link String}
	 */
	public void updateInMemoryBatch(Batch batch, String batchInstanceIdentifier) {
		getBatchXMLMemoryMap().put(batchInstanceIdentifier, batch);
	}

	/**
	 * Removes batch XML map entry for a batch instance
	 * 
	 * @param batchInstanceIdentifier {@link String}
	 */
	public void removeInMemoryBatch(final String batchInstanceIdentifier) {
		if (null == batchXMLMemoryMap) {
			LOGGER.warn("Batch xml for batch: ", batchInstanceIdentifier,
					" is missing from the in memory map. It could not be cleared.");
		} else {
			batchXMLMemoryMap.remove(batchInstanceIdentifier);
			LOGGER.info("Batch xml for batch: ", batchInstanceIdentifier, " is removed from internal memory map.");
		}
	}
}
