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

package com.ephesoft.gxt.core.client.ui.factory;

import java.util.HashMap;

import com.ephesoft.gxt.core.client.constant.PropertyAccessModel;
import com.ephesoft.gxt.core.client.ui.service.dualListConfig.DualListConfigService;
import com.ephesoft.gxt.core.client.ui.service.dualListConfig.impl.BatchClassModuleDualListConfigService;
import com.ephesoft.gxt.core.client.ui.service.dualListConfig.impl.BatchClassPluginDualListConfigService;
import com.ephesoft.gxt.core.client.ui.service.dualListConfig.impl.PluginDependenciesDualListConfigService;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;

public class DualListAttributeFactory {

	private static HashMap<PropertyAccessModel, DualListConfigService<?, ?>> dualListConfigFactory;

	static {
		dualListConfigFactory = new HashMap<PropertyAccessModel, DualListConfigService<?, ?>>();
	}

	public static <M, T> ModelKeyProvider<M> getKeyProvider(final PropertyAccessModel propertyAccessor) {
		final DualListConfigService<M, T> configService = DualListAttributeFactory.<M, T> getConfigService(propertyAccessor);
		ModelKeyProvider<M> modelKeyProvider = null;
		if (configService != null) {
			modelKeyProvider = configService == null ? null : configService.getKeyProvider();
		}
		return modelKeyProvider;
	}

	@SuppressWarnings("unchecked")
	private static <M, T> DualListConfigService<M, T> getConfigService(final PropertyAccessModel propertyAccessor) {
		DualListConfigService<?, ?> dualListConfigService = null;
		if (propertyAccessor != null) {
			dualListConfigService = dualListConfigFactory.get(propertyAccessor);
			if (dualListConfigService == null) {
				switch (propertyAccessor) {
					case PLUGIN_DEPENDENCIES:
						dualListConfigService = new PluginDependenciesDualListConfigService();
						dualListConfigFactory.put(PropertyAccessModel.PLUGIN_DEPENDENCIES, dualListConfigService);
						break;
					case MODULE_TYPE:
						dualListConfigService = new BatchClassModuleDualListConfigService();
						dualListConfigFactory.put(PropertyAccessModel.MODULE_TYPE, dualListConfigService);
						break;
					case PLUGIN_TYPE:
						dualListConfigService = new BatchClassPluginDualListConfigService();
						dualListConfigFactory.put(PropertyAccessModel.PLUGIN_TYPE, dualListConfigService);
						break;
					default:
						dualListConfigService = null;
						break;
				}
			}
		}
		return dualListConfigService == null ? null : (DualListConfigService<M, T>) dualListConfigService;
	}

	public static <M, T> ValueProvider<? super M, T> getValueProvider(final PropertyAccessModel propertyAccessor) {
		final DualListConfigService<M, T> configService = DualListAttributeFactory.<M, T> getConfigService(propertyAccessor);
		ValueProvider<? super M, T> valueProvider = null;
		if (configService != null) {
			valueProvider = configService == null ? null : configService.getValueProvider();
		}
		return valueProvider;
	}
}
