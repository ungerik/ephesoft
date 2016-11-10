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

package com.ephesoft.dcma.batch.service.impl;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.ephesoft.dcma.batch.service.ServerBatchPriorityService;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.da.domain.ServerRegistry;
import com.ephesoft.dcma.da.service.ServerRegistryService;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.NumberUtil;
import com.ephesoft.dcma.util.logger.EphesoftLogger;
import com.ephesoft.dcma.util.logger.EphesoftLoggerFactory;

public class ServerBatchPriorityServiceImpl implements ServerBatchPriorityService {

	private static final EphesoftLogger LOGGER = EphesoftLoggerFactory.getLogger(ServerBatchPriorityServiceImpl.class);

	/**
	 * The service for CRUD operation on server_registry table in Ephesoft Database.
	 */
	@Autowired
	private ServerRegistryService serverRegistryService;

	@Override
	public Set<Integer> getRangeOfPriority(String priorityRangeString) {
		Set<Integer> rangeOfPriority = null;

		// Precondition: priorityRangeString must not be emoty for this method.
		boolean validString = NumberUtil.isValidPriorityRange(priorityRangeString);
		if (validString) {
			String[] priorityRangeGroups = priorityRangeString.split(ICommonConstants.OR_REGEX);
			for (String aPriorityRange : priorityRangeGroups) {
				if (EphesoftStringUtil.isNullOrEmpty(aPriorityRange)) {
					LOGGER.error("Priority range syntax in properties is incorrect.");
				} else {
					String[] range = aPriorityRange.split(ICommonConstants.PRIORITY_RANGE_SEPERATOR);
					if (range.length <= 2) {
						String minimumPriorityString = range[0];
						boolean validMinimumPriority = NumberUtil.isValidPositiveInteger(minimumPriorityString);
						if (validMinimumPriority) {
							int minimumPriority = Integer.parseInt(minimumPriorityString);
							if (null == rangeOfPriority) {
								rangeOfPriority = new TreeSet<Integer>();
							}
							LOGGER.debug("Added priority: ", minimumPriority, " to server's priority range.");
							addElementsToSet(rangeOfPriority, range, minimumPriority);
						}
					} else {
						LOGGER.error("Priority range syntax in properties is incorrect.");
					}
				}
			}
		} else {
			LOGGER.error("Priority range syntax in properties is incorrect.");
		}
		return rangeOfPriority;
	}

	/**
	 * Adds elements of a priority range to a set.
	 * 
	 * @param prioritySet {@link Set}<{@link Integer}>
	 * @param range
	 * @param minimumPriority
	 */
	private void addElementsToSet(Set<Integer> prioritySet, String[] range, int minimumPriority) {
		if (null != prioritySet) {
			prioritySet.add(minimumPriority);
			if (null != range && range.length == 2) {
				String maximumPriorityString = range[1];
				boolean validMaximumPriority = NumberUtil.isValidPositiveInteger(maximumPriorityString);
				if (validMaximumPriority) {
					int maximumPriority = Integer.parseInt(maximumPriorityString);
					for (int index = minimumPriority + 1; index <= maximumPriority; index++) {
						prioritySet.add(index);
					}
				}
			}
		}
	}

	@Override
	public boolean validatePriorityRange(final Set<Integer> currentServerPrioritySet, final ServerRegistry serverRegistry, final boolean isSecure) {
		boolean isValid;
		List<Set<Integer>> prioritySetList = serverRegistryService.getPriorityLoadedActiveServers(serverRegistry.getId(), isSecure);
		if (CollectionUtils.isEmpty(currentServerPrioritySet)) {
			isValid = false;
		} else if (CollectionUtils.isEmpty(prioritySetList)) {
			isValid = true;
		} else {
			boolean isValidForAllServers = true;
			for (Set<Integer> anActiveServerPrioritySet : prioritySetList) {
				if (CollectionUtils.isEmpty(anActiveServerPrioritySet)) {
					isValidForAllServers &= true;
				} else {
					Collection<Integer> collection = (Collection<Integer>) CollectionUtils.intersection(anActiveServerPrioritySet,
							currentServerPrioritySet);
					if (CollectionUtils.isEmpty(collection) || collection.size() == currentServerPrioritySet.size()) {
						isValidForAllServers &= true;
					} else {
						isValidForAllServers &= false;
						break;
					}
				}
			}
			isValid = isValidForAllServers;
		}
		return isValid;
	}

	@Override
	public String getPriorityRangeString(final Set<Integer> setOfRangeOfPriority) {
		String priorityString = null;
		if (null != setOfRangeOfPriority) {
			StringBuilder priorityStringBuilder = new StringBuilder();
			Iterator<Integer> iterator = setOfRangeOfPriority.iterator();
			while (iterator.hasNext()) {
				Integer batchPriority = iterator.next();
				priorityStringBuilder.append(batchPriority);
				priorityStringBuilder.append(ICommonConstants.PRIORITY_SET_SEPERATOR);
			}
			priorityString = priorityStringBuilder.toString();
		}
		LOGGER.debug("Priority string is: ", priorityString);
		return priorityString;
	}
}
