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

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.da.dao.ReportsFolderDao;
import com.ephesoft.dcma.da.domain.Report;
import com.ephesoft.dcma.da.domain.ReportsFolder;
import com.ephesoft.dcma.da.domain.SubReport;
import com.ephesoft.dcma.da.service.ReportsFolderService;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.logger.EphesoftLogger;
import com.ephesoft.dcma.util.logger.EphesoftLoggerFactory;

/**
 * Service implementation for ReportsFolder domain object
 * 
 * @author Ephesoft
 * 
 */
@Service
public class ReportsFolderServiceImpl implements ReportsFolderService {

	@Autowired
	private ReportsFolderDao reportsFolderDao;

	private static final EphesoftLogger LOGGER = EphesoftLoggerFactory.getLogger(ReportsFolderServiceImpl.class);

	@Transactional(isolation = Isolation.SERIALIZABLE, readOnly = true)
	@Override
	public List<ReportsFolder> getAllFolders() {
		return reportsFolderDao.getAllFolders();
	}

	@Transactional(isolation = Isolation.SERIALIZABLE, readOnly = true)
	@Override
	public List<ReportsFolder> getAllFoldersEagerly() {
		final List<ReportsFolder> reportFolderList = reportsFolderDao.getAllFolders();
		if (CollectionUtils.isNotEmpty(reportFolderList)) {

			// iterating through the returned Arraylist to get values of all child report before service layer disconnect it from
			// session.
			for (final ReportsFolder reportFolder : reportFolderList) {
				final List<Report> reports = reportFolder.getChildReports();
				String hostURIPath = reportFolder.getHostURIPath();
				String folderContextPath = reportFolder.getDefaultContextPath();
				if (CollectionUtils.isNotEmpty(reports)) {
					LOGGER.info("Following is the retrieved reports Name for folder: ", hostURIPath, folderContextPath);
					for (final Report aReport : reports) {
						String parentReportName = aReport.getFolderName();
						LOGGER.debug("Report Name: ", parentReportName, ", Default Folder Path is: ", aReport.getDefaultFolderPath());
						final List<SubReport> subReports = aReport.getSubReports();
						if (CollectionUtils.isNotEmpty(subReports)) {
							LOGGER.info("Following is the retrieved sub reports Name for parent report: ", parentReportName);
							for (final SubReport aSubReport : subReports) {
								LOGGER.debug("Sub Report Name: ", aSubReport.getReportName(), " and Default Report Path is: ",
										aSubReport.getDefaultReportPath());
							}
						} else {
							LOGGER.warn("Either null or no reports are under report: ", parentReportName);
						}
					}
				} else {
					LOGGER.warn("Either null or no reports are under folder: ", hostURIPath, folderContextPath);
				}
			}
		} else {
			LOGGER.info("Report folder list came as null or empty");
		}
		return reportFolderList;
	}

	@Transactional(isolation = Isolation.SERIALIZABLE, readOnly = true)
	@Override
	public ReportsFolder getDefaultReportFolder() {
		ReportsFolder defaultFolder = null;
		final int FOLDER_INDEX = 0;
		final int MAX_RESULT = 1;
		final List<ReportsFolder> reportFolderList = reportsFolderDao.getAllFolders(FOLDER_INDEX, MAX_RESULT);
		if (CollectionUtils.isNotEmpty(reportFolderList)) {
			if (MAX_RESULT == reportFolderList.size()) {
				defaultFolder = reportFolderList.get(MAX_RESULT - 1);
			}
		} else {
			LOGGER.info("Database returned null or empty reports folder list");
		}
		return defaultFolder;
	}
	
	@Transactional(isolation = Isolation.SERIALIZABLE, readOnly = true)
	@Override
	public String getDefaultReportFolderPath(ReportsFolder defaultFolder, String hostURI) {
		
		// iterating through the returned Arraylist to get values of all child report before service layer disconnect it from
		// session.
		final int FOLDER_INDEX = 0;
		String defaultURL = null;
		final List<Report> reports = defaultFolder.getChildReports();
		String folderContextPath = defaultFolder.getDefaultContextPath();
		if (CollectionUtils.isNotEmpty(reports)) {
			Report defaultReport = reports.get(FOLDER_INDEX);
			LOGGER.info("Following is the retrieved reports Name for folder: ", folderContextPath);
			String parentReportName = defaultReport.getFolderName();
			LOGGER.debug("Report Name: ", parentReportName, ", Default Folder Path is: ", defaultReport.getDefaultFolderPath());
			final List<SubReport> subReports = defaultReport.getSubReports();
			if (CollectionUtils.isNotEmpty(subReports)) {
				LOGGER.info("Following is the retrieved sub reports Name for parent report: ", parentReportName);
				SubReport defaultSubReport = subReports.get(FOLDER_INDEX);
				defaultURL = EphesoftStringUtil.concatenate(hostURI, folderContextPath,
						defaultReport.getDefaultFolderPath(), ICommonConstants.DOT, defaultSubReport.getDefaultReportPath());
				LOGGER.debug("Deault URL is: ", defaultSubReport.getReportName(), " and Default Report Path is: ",
						defaultSubReport.getDefaultReportPath());
			} else {
				LOGGER.warn("Either null or no reports are under report: ", parentReportName);
			}
		} else {
			LOGGER.warn("Either null or no reports are under folder: ", folderContextPath);
		}

		return defaultURL;
	}

}
