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

package com.ephesoft.dcma.core.dao.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.hibernate.criterion.DetachedCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.core.common.DCMABusinessException;
import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.component.JAXB2Template;
import com.ephesoft.dcma.core.dao.Dao;
import com.ephesoft.dcma.util.ApplicationConfigProperties;
import com.ephesoft.dcma.util.FileStreamResult;
import com.ephesoft.dcma.util.FileUtils;
import com.ephesoft.dcma.util.XMLUtil;

/**
 * @author Ephesoft
 * @version 1.0
 * 
 * @param <T>
 */
public abstract class XmlDao<T> implements Dao<T> {

	/**
	 * LOGGER to print the logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(XmlDao.class);
	private static final String OPERATION_NOT_SUPPORTED = "Operation not supported.";

	public void create(T object, Serializable identifier, boolean isZipSwitchOn) {
		create(object, identifier, null, FileType.HOCR_XML.getExtension(), isZipSwitchOn, false);
	}

	public void create(T object, Serializable identifier, String pageId, String fileExtnWithDot, boolean isZipSwitchOn,
			boolean isFirstTimeUpdate) {
		LOGGER.info("Entering create method.");
		OutputStream stream = null;
		try {
			String filePath = null;
			if (null == pageId) {
				filePath = getJAXB2Template().getLocalFolderLocation() + File.separator + identifier + File.separator + identifier
						+ fileExtnWithDot;
				;
			} else {
				filePath = getJAXB2Template().getLocalFolderLocation() + File.separator + identifier + File.separator + identifier
						+ "_" + pageId + fileExtnWithDot;
			}
			File xmlFile = new File(filePath);
			boolean isZip = false;

			if (isZipSwitchOn) {
				if (isFirstTimeUpdate && !xmlFile.exists()) {
					isZip = true;
				} else if (FileUtils.isZipFileExists(filePath)) {
					isZip = true;
				}
			} else if (isFirstTimeUpdate && FileUtils.isZipFileExists(filePath)) {
				isZip = true;
			}
			StreamResult result = null;
			if (isZip) {
				stream = FileUtils.getOutputStreamFromZip(filePath, identifier + fileExtnWithDot);
				result = new FileStreamResult(new File(filePath), stream);
			} else {
				stream = new FileOutputStream(xmlFile);
				result = new FileStreamResult(xmlFile, stream);
			}

			getJAXB2Template().getJaxb2Marshaller().marshal(object, result);

		} catch (FileNotFoundException e) {
			throw new DCMABusinessException(e.getMessage(), e);
		} catch (Exception e) {
			throw new DCMABusinessException(e.getMessage(), e);
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
				}
			}
		}
		LOGGER.info("Exiting create method.");
	}

	public void create(T object, String filePath) {
		LOGGER.info("Entering create method.");
		OutputStream stream = null;
		try {
			File xmlFile = new File(filePath);
			stream = new FileOutputStream(xmlFile);
			StreamResult result = new FileStreamResult(xmlFile, stream);
			getJAXB2Template().getJaxb2Marshaller().marshal(object, result);
		} catch (FileNotFoundException e) {
			throw new DCMABusinessException(e.getMessage(), e);
		} catch (Exception e) {
			throw new DCMABusinessException(e.getMessage(), e);
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
				}
			}
		}
		LOGGER.info("Exiting create method.");
	}

	@Override
	public T get(Serializable identifier) {
		boolean isZipSwitchOn = true;
		try {
			ApplicationConfigProperties prop = ApplicationConfigProperties.getApplicationConfigProperties();
			isZipSwitchOn = Boolean.parseBoolean(prop.getProperty(ICommonConstants.ZIP_SWITCH));
		} catch (IOException ioe) {
			LOGGER.error("Unable to read the zip switch value. Taking default value as true.Exception thrown is:" + ioe.getMessage(),
					ioe);
		}
		return get(identifier, null, ICommonConstants.UNDERSCORE_BATCH_XML, isZipSwitchOn, true);
	}

	public T get(Serializable identifier, boolean isZipSwitchOn, boolean throwException) {
		return get(identifier, null, ICommonConstants.UNDERSCORE_BATCH_XML, isZipSwitchOn, throwException);
	}

	@SuppressWarnings("unchecked")
	public T get(Serializable identifier, String pageId, String fileName, boolean isZipSwitchOn, boolean throwException) {
		T result;
		LOGGER.info("Entering get method.");
		InputStream inputStream = null;
		String filePath = null;
		try {

			if (null == pageId) {
				filePath = getJAXB2Template().getLocalFolderLocation() + File.separator + identifier + File.separator + identifier
						+ fileName;
			} else {
				filePath = getJAXB2Template().getLocalFolderLocation() + File.separator + identifier + File.separator + identifier
						+ "_" + pageId + fileName;
			}
			File xmlFile = new File(filePath);
			LOGGER.info("FilePath in get batch object is : " + filePath);
			LOGGER.info("Zip switch in get batch object is : " + isZipSwitchOn);

			boolean isZip = false;

			if (isZipSwitchOn) {
				if (FileUtils.isZipFileExists(filePath)) {
					isZip = true;
				}
			} else {
				if (xmlFile.exists()) {
					isZip = false;
				} else if (FileUtils.isZipFileExists(filePath)) {
					isZip = true;
				}
			}
			Source source = null;
			if (isZip) {
				inputStream = FileUtils.getInputStreamFromZip(filePath, identifier + fileName);
				source = XMLUtil.createSourceFromStream(inputStream, new File(filePath));
			} else {
				inputStream = new FileInputStream(xmlFile);
				source = XMLUtil.createSourceFromStream(inputStream, xmlFile);
			}

			result = (T) getJAXB2Template().getJaxb2Marshaller().unmarshal(source);
		} catch (Exception e) {
			if (throwException) {
				throw new DCMABusinessException(e.getMessage(), e);
			} else {
				result = null;
			}
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException ioe) {
				LOGGER.info("Exception in closing inputstream in xml dao. Filename is:" + filePath);
			}
			LOGGER.info("Exiting get method.");
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public T get(Serializable identifier, String workflow, String pageId, String fileName, boolean isZipSwitchOn) {
		LOGGER.info("Entering get method.");
		InputStream inputStream = null;
		String filePath = null;
		try {

			filePath = getJAXB2Template().getLocalFolderLocation() + File.separator + identifier + File.separator + identifier
					+ ICommonConstants.UNDERSCORE + workflow + fileName;
			File xmlFile = new File(filePath);
			LOGGER.info("FilePath in get batch object is : " + filePath);
			LOGGER.info("Zip switch in get batch object is : " + isZipSwitchOn);

			boolean isZip = false;

			if (isZipSwitchOn) {
				if (FileUtils.isZipFileExists(filePath)) {
					isZip = true;
				}
			} else {
				if (xmlFile.exists()) {
					isZip = false;
				} else if (FileUtils.isZipFileExists(filePath)) {
					isZip = true;
				}
			}
			Source source = null;
			if (isZip) {
				inputStream = FileUtils.getInputStreamFromZip(filePath, identifier + ICommonConstants.UNDERSCORE_BATCH_XML);
				source = XMLUtil.createSourceFromStream(inputStream, new File(filePath));
			} else {
				inputStream = new FileInputStream(xmlFile);
				source = XMLUtil.createSourceFromStream(inputStream, xmlFile);
			}

			return (T) getJAXB2Template().getJaxb2Marshaller().unmarshal(source);
		} catch (Exception e) {
			throw new DCMABusinessException(e.getMessage(), e);
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException ioe) {
				LOGGER.info("Exception in closing inputstream in xml dao. Filename is:" + filePath);
			}
			LOGGER.info("Exiting get method.");
		}
	}
	
	/**
	 * API for getting the Object providing the filePath.
	 * 
	 * @param filePath
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T getObjectFromFilePath(String filePath) {
		LOGGER.info("Entering get method.");
		InputStream inputStream = null;
		try {

			File xmlFile = new File(filePath);
			LOGGER.info("FilePath in get batch object is : " + filePath);

			inputStream = new FileInputStream(xmlFile);

			Source source = XMLUtil.createSourceFromStream(inputStream, xmlFile);
			return (T) getJAXB2Template().getJaxb2Marshaller().unmarshal(source);
		} catch (Exception e) {
			throw new DCMABusinessException(e.getMessage(), e);
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException ioe) {
				LOGGER.info("Exception in closing inputstream in xml dao. Filename is:" + filePath);
			}
			LOGGER.info("Exiting get method.");
		}
	}

	@Override
	public List<T> getAll() {
		throw new UnsupportedOperationException(OPERATION_NOT_SUPPORTED);
	}

	@Override
	public T merge(T object) {
		throw new UnsupportedOperationException(OPERATION_NOT_SUPPORTED);
	}

	@Override
	public void remove(T object) {
		// TODO Auto-generated method stub
	}

	@Override
	public void saveOrUpdate(T object) {
		throw new UnsupportedOperationException(OPERATION_NOT_SUPPORTED);
	}

	@Override
	public void create(T object) {
		throw new UnsupportedOperationException(OPERATION_NOT_SUPPORTED);
	}

	@Override
	public void evict(Object object) {
		throw new UnsupportedOperationException(OPERATION_NOT_SUPPORTED);
	}

	public void update(T object, Serializable identifier, String fileName, boolean isZipSwitchOn, boolean isFirstTimeUpdate) {
		update(object, identifier, fileName, null, isZipSwitchOn, isFirstTimeUpdate);
	}

	public void update(T object, Serializable identifier, String fileName, String pageId, boolean isZipSwitchOn,
			boolean isFirstTimeUpdate) {
		LOGGER.info("Entering update method.");
		OutputStream stream = null;
		try {
			String filePath = null;
			if (pageId == null) {
				filePath = getJAXB2Template().getLocalFolderLocation() + File.separator + identifier + File.separator + identifier
						+ fileName;
			} else {
				filePath = getJAXB2Template().getLocalFolderLocation() + File.separator + identifier + File.separator + identifier
						+ "_" + pageId + fileName;
			}
			File xmlFile = new File(filePath);
			boolean isZip = false;
			if (isZipSwitchOn) {
				if (isFirstTimeUpdate && !xmlFile.exists()) {
					isZip = true;
				} else if (FileUtils.isZipFileExists(filePath)) {
					isZip = true;
				}
			} else {
				if (isFirstTimeUpdate && !FileUtils.isZipFileExists(filePath)) {
					isZip = false;
				} else if (xmlFile.exists()) {
					isZip = false;
				} else {
					isZip = true;
				}
			}
			StreamResult result = null;
			if (isZip) {
				stream = FileUtils.getOutputStreamFromZip(filePath, identifier + fileName);
				result = new FileStreamResult(new File(filePath), stream);
			} else {
				stream = new FileOutputStream(xmlFile);
				result = new FileStreamResult(xmlFile, stream);
			}

			getJAXB2Template().getJaxb2Marshaller().marshal(object, result);
		} catch (FileNotFoundException e) {
			throw new DCMABusinessException(e.getMessage(), e);
		} catch (Exception e) {
			throw new DCMABusinessException(e.getMessage(), e);
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					LOGGER.info("Exception in closing outputstream in xml dao. Filename is:" + fileName);
				}
			}
		}
		LOGGER.info("Exiting update method.");
	}

	public void create(T object, Serializable identifier, String workflow, String fileName, boolean isZipSwitchOn) {
		LOGGER.info("Entering update method.");
		OutputStream stream = null;
		try {
			String filePath = null;
				filePath = getJAXB2Template().getLocalFolderLocation() + File.separator + identifier + File.separator + identifier
						+ ICommonConstants.UNDERSCORE + workflow + fileName;
			File xmlFile = new File(filePath);
			StreamResult result = null;
			if (isZipSwitchOn) {
				stream = FileUtils.getOutputStreamFromZip(filePath, identifier + ICommonConstants.UNDERSCORE_BATCH_XML);
				result = new FileStreamResult(new File(filePath), stream);
			} else {
				stream = new FileOutputStream(xmlFile);
				result = new FileStreamResult(xmlFile, stream);
			}

			getJAXB2Template().getJaxb2Marshaller().marshal(object, result);
		} catch (FileNotFoundException e) {
			throw new DCMABusinessException(e.getMessage(), e);
		} catch (Exception e) {
			throw new DCMABusinessException(e.getMessage(), e);
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					LOGGER.info("Exception in closing outputstream in xml dao. Filename is:" + fileName);
				}
			}
		}
		LOGGER.info("Exiting update method.");
	}
	
	public void update(T object, Serializable identifier, String fileName, boolean isZipSwitchOn) {
		update(object, identifier, fileName, isZipSwitchOn, false);
	}

	@Override
	public int countAll() {
		return 0;
	}

	@Override
	public int count(DetachedCriteria criteria) {
		return 0;
	}

	public void update(T object, Serializable identifier, boolean isZipSwitchOn) {
		update(object, identifier, ICommonConstants.UNDERSCORE_BATCH_XML, isZipSwitchOn);
	}

	public void update(T object, Serializable identifier, boolean isZipSwitchOn, boolean isFirstTimeUpdate) {
		update(object, identifier, ICommonConstants.UNDERSCORE_BATCH_XML, isZipSwitchOn, isFirstTimeUpdate);
	}

	public void update(final T object, final String filePath) {
		LOGGER.info("Entering update method.");
		if (filePath == null || filePath.isEmpty()) {
			LOGGER.info("File path is either null or empty.");
		} else {
			LOGGER.info("Updating file: " + filePath);
			OutputStream stream = null;
			try {
				File xmlFile = new File(filePath);
				stream = new FileOutputStream(xmlFile);
				StreamResult result = new FileStreamResult(xmlFile, stream);
				getJAXB2Template().getJaxb2Marshaller().marshal(object, result);
			} catch (FileNotFoundException e) {
				throw new DCMABusinessException(e.getMessage(), e);
			} catch (Exception e) {
				throw new DCMABusinessException(e.getMessage(), e);
			} finally {
				if (stream != null) {
					try {
						stream.close();
					} catch (IOException e) {
						LOGGER.info("Exception in closing outputstream in xml dao. File: " + filePath);
					}
				}
			}
		}
		LOGGER.info("Exiting update method.");
	}

	public abstract JAXB2Template getJAXB2Template();

	@Override
	public void removeAll(Collection<T> entities) {

	}
	public String marshalObjectAsString(T object) {
		final StringWriter out = new StringWriter();
		getJAXB2Template().getJaxb2Marshaller().marshal(object, new StreamResult(out));
		return out.toString();
	}

	@SuppressWarnings("unchecked")
	public T unmarshalString(String xmlString) {
		StringReader reader = new StringReader(xmlString);
		return (T) getJAXB2Template().getJaxb2Marshaller().unmarshal(new StreamSource(reader));
	}
}
