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

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.util.Date;

import com.ephesoft.dcma.core.model.common.AbstractChangeableEntity;

@Entity
@Table(name = "mobile_web_service_status")
public class MobileWebServiceStatus extends AbstractChangeableEntity implements Serializable {
	

	private static final long serialVersionUID = 1L;

	@Column(name = "service_id")
	private String serviceId;

	@Column(name = "start_operation")
	private Date operationStartTime;
	
	@Column(name = "split_completion")
	private Date splitCompletionTime;

	@Column(name = "ocr_completion")
	private Date ocrCompletionTime;

	@Column(name = "extraction_completion")
	private Date extractionCompletionTime;

	@Column(name = "current_status")
	private String serviceCurrentStatus;

	@Column(name = "classification_completion")
	private Date classificationCompletionTime;

	public Date getOperationStartTime() {
		return operationStartTime;
	}

	public void setOperationStartTime(Date operationStartTime) {
		this.operationStartTime = operationStartTime;
	}

	public Date getSplitCompletionTime() {
		return splitCompletionTime;
	}

	public void setSplitCompletionTime(Date splitCompletionTime) {
		this.splitCompletionTime = splitCompletionTime;
	}

	public Date getOcrCompletionTime() {
		return ocrCompletionTime;
	}

	public void setOcrCompletionTime(Date ocrCompletionTime) {
		this.ocrCompletionTime = ocrCompletionTime;
	}

	public Date getExtractionCompletionTime() {
		return extractionCompletionTime;
	}

	public void setExtractionCompletionTime(Date extractionCompletionTime) {
		this.extractionCompletionTime = extractionCompletionTime;
	}

	public String getServiceCurrentStatus() {
		return serviceCurrentStatus;
	}

	public void setServiceCurrentStatus(String serviceCurrentStatus) {
		this.serviceCurrentStatus = serviceCurrentStatus;
	}


	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setClassificationCompletionTime(
			Date classificationCompletionTime) {
		this.classificationCompletionTime = classificationCompletionTime;
	}

	public Date getClassificationCompletionTime() {
		return classificationCompletionTime;
	}
}

