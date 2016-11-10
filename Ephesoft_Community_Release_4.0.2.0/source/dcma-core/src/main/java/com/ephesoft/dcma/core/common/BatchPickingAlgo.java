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

import com.ephesoft.dcma.util.logger.EphesoftLogger;
import com.ephesoft.dcma.util.logger.EphesoftLoggerFactory;

/**
 * This class represents the enumeration of algorithms used for order of picking up batches for pick up service.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 07-Nov-2014 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
public enum BatchPickingAlgo {

	/**
	 * First in first out order for batches.
	 */
	FIFO(0),
	/**
	 * Round robin order based on looking for batches of a batch class whose batches are not picked since long time.
	 */
	ROUND_ROBIN(1);

	/**
	 * {@link EphesoftLogger} Instance of logger.
	 */
	private final static EphesoftLogger log = EphesoftLoggerFactory.getLogger(BatchPickingAlgo.class);

	/**
	 * Identifier code for an enumeration constant.
	 */
	private int id;

	/**
	 * Gets identifier code for an enumeration constant.
	 * 
	 * @return
	 */
	private int getCode() {
		return id;
	}

	/**
	 * Default constructor that sets identifier code for an enumeration constant initially.
	 * 
	 * @param id int
	 */
	private BatchPickingAlgo(final int id) {
		this.id = id;
	}

	/**
	 * Gets enumeration constant for the passed identifier code.
	 * 
	 * @param code int Identifier code for an enumeration constant.
	 * @return {@link BatchPickingAlgo}
	 */
	public static BatchPickingAlgo getBatchPickingAlgo(final int code) {
		BatchPickingAlgo[] batchPickingAlgos = BatchPickingAlgo.values();
		BatchPickingAlgo batchPickingAlgo = null;
		for (BatchPickingAlgo algo : batchPickingAlgos) {
			if (algo.getCode() == code) {
				batchPickingAlgo = algo;
				break;
			}
		}
		if (null == batchPickingAlgo) {
			final BatchPickingAlgo defaultAlgo = batchPickingAlgos[1];
			log.warn("Invalid value specified for workflow Batch Picking Algorithm. Setting it to its default value: ", defaultAlgo);
			batchPickingAlgo = defaultAlgo;
		}
		return batchPickingAlgo;
	}
}
