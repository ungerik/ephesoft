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

package com.ephesoft.dcma.workflows.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.core.annotation.PreProcess;
import com.ephesoft.dcma.da.id.BatchInstanceID;
import com.ephesoft.dcma.util.logger.EphesoftLogger;

/**
 * This class represents a Utility class for DCMA Aspects.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 29-Oct-2014 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
public class DCMAAspectUtil {

	/***
	 * {@link EphesoftLogger} Instance of logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(DCMAAspectUtil.class);

	/**
	 * Find and execute the method for this aspect.
	 * 
	 * @param joinPoint {@link JoinPoint}
	 * @param target {@link Object}
	 * @param methods {@link Method}[]
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static void findAspectMethodsToExecute(final JoinPoint joinPoint, final Object target, final Method[] methods,
			final Class<?> annotationClass) throws IllegalAccessException, InvocationTargetException {
		for (int methodIndex = 0; methodIndex < methods.length; methodIndex++) {
			Method method = methods[methodIndex];
			Annotation annotation = method.getAnnotation(PreProcess.class);
			if (null != annotation && joinPoint.getArgs().length >= 1) {
				final Object aJoinPoint = joinPoint.getArgs()[0];
				if (aJoinPoint instanceof BatchInstanceID) {
					method.invoke(target, aJoinPoint, joinPoint.getArgs()[1]);
				} else {
					LOGGER.info("Method ", method, " does not comply to Pre-process agreement, thus method not invoked.");
				}
			}
		}
	}
}
