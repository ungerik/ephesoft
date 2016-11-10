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

package com.ephesoft.dcma.da.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.ephesoft.dcma.core.model.common.AbstractChangeableEntity;

@Entity
@Table(name = "report", uniqueConstraints = @UniqueConstraint(columnNames = {"default_folder_path", "parent_folder_id"}))
public class Report extends AbstractChangeableEntity {

	/**
	 * Default serial version id.
	 */
	private static final long serialVersionUID = 4074068366655896086L;

	@Column(name = "report_name")
	private String folderName;
	
	@Column(name = "default_folder_path")
	private String defaultFolderPath;

	@ManyToOne(cascade = {CascadeType.ALL})
	@JoinColumn(name = "parent_folder_id")
	private ReportsFolder parentFolderId;

	@OneToMany(mappedBy = "parentFolderId", fetch = FetchType.LAZY)
	private List<SubReport> subReports;

	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	public List<SubReport> getSubReports() {
		return subReports;
	}

	public void setSubReports(List<SubReport> subReports) {
		this.subReports = subReports;
	}

	// public String getDefaultReportPath() {
	// return defaultReportPath;
	// }
	//
	// public void setDefaultReportPath(String defaultReportPath) {
	// this.defaultReportPath = defaultReportPath;
	// }

	public String getDefaultFolderPath() {
		return defaultFolderPath;
	}

	public void setDefaultFolderPath(String defaultFolderPath) {
		this.defaultFolderPath = defaultFolderPath;
	}

	public ReportsFolder getParentFolderId() {
		return parentFolderId;
	}

	public void setParentFolderId(ReportsFolder parentFolderId) {
		this.parentFolderId = parentFolderId;
	}
	//
	// @Column(name = "default_report_path")
	// private String defaultReportPath;

}
