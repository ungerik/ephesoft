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

package com.ephesoft.dcma.da.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.ephesoft.dcma.da.dao.ReportsDao;
import com.ephesoft.dcma.da.domain.SubReport;
import com.ephesoft.dcma.da.service.ReportsService;
import com.ephesoft.dcma.util.CollectionUtil;
import com.ephesoft.dcma.util.logger.EphesoftLogger;
import com.ephesoft.dcma.util.logger.EphesoftLoggerFactory;

/**
 * Service Implementation for {@link SubReport} domain object
 * 
 * @author Ephesoft
 * 
 */
@Service
public class ReportsServiceImpl implements ReportsService {

	/**
	 * Dao object for reports domain object.e
	 */
	@Autowired
	private ReportsDao reportsDao;

	private static final EphesoftLogger LOGGER = EphesoftLoggerFactory.getLogger(ReportsServiceImpl.class);

	@Transactional(isolation = Isolation.SERIALIZABLE, readOnly = true)
	@Override
	public List<SubReport> getAllSubReports() {
		LOGGER.info("Retrieving number of reports.");
		final List<SubReport> allSubReports = reportsDao.getAllSubReports();
		if (CollectionUtil.isNonNull.apply(allSubReports) && CollectionUtil.isEmpty(allSubReports)) {
			LOGGER.debug("No'of reports object found:  ", allSubReports.size());
		} else {
			LOGGER.warn("Either null or zero records returned by the query.");
		}

		return allSubReports;
	}

}
