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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.ephesoft.dcma.core.common.ClusterPropertyType;
import com.ephesoft.dcma.core.model.common.AbstractChangeableEntity;

/**
 * Domain Object to wwork with cluster_property table
 * 
 * @author Ephesoft
 *
 */
@Entity
@Table(name = "cluster_property")
public class ClusterProperty extends AbstractChangeableEntity {

	/**
	 * Reference for serialVersionUID.
	 */
	private static final long serialVersionUID = 1217247362985982731L;

	/**
	 * Property name string
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "property_name")
	private ClusterPropertyType propertyName;

	/**
	 * Property Value String
	 */
	@Column(name = "property_value")
	private String propertyValue;

	/**
	 * Getter for propertyName
	 * 
	 * @return {@link String}
	 */
	public ClusterPropertyType getPropertyName() {
		return propertyName;
	}

	/**
	 * Setter for propertyName
	 * 
	 * @param propertyName{@link String}
	 */
	public void setPropertyName(ClusterPropertyType propertyName) {
		this.propertyName = propertyName;
	}

	/**
	 * Getter for propertyValue
	 * 
	 * @return {@link String}
	 */
	public String getPropertyValue() {
		return propertyValue;
	}

	/**
	 * Setter for propertyValue
	 * 
	 * @param propertyValue {@link String}
	 */
	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}

}
