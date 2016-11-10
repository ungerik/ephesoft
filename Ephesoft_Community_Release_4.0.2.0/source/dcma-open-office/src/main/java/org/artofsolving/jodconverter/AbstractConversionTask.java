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

//
// JODConverter - Java OpenDocument Converter
// Copyright 2009 Art of Solving Ltd
// Copyright 2004-2009 Mirko Nasato
//
// JODConverter is free software: you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public License
// as published by the Free Software Foundation, either version 3 of
// the License, or (at your option) any later version.
//
// JODConverter is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General
// Public License along with JODConverter.  If not, see
// <http://www.gnu.org/licenses/>.
//
package org.artofsolving.jodconverter;

import static org.artofsolving.jodconverter.office.OfficeUtils.SERVICE_DESKTOP;
import static org.artofsolving.jodconverter.office.OfficeUtils.cast;
import static org.artofsolving.jodconverter.office.OfficeUtils.toUrl;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import org.artofsolving.jodconverter.office.OfficeContext;
import org.artofsolving.jodconverter.office.OfficeException;
import org.artofsolving.jodconverter.office.OfficeTask;

import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.sun.star.beans.PropertyState;
import com.sun.star.beans.PropertyValue;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.frame.XStorable;
import com.sun.star.io.IOException;
import com.sun.star.io.XInputStream;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.lang.XComponent;
import com.sun.star.lib.uno.adapter.ByteArrayToXInputStreamAdapter;
import com.sun.star.task.ErrorCodeIOException;
import com.sun.star.util.CloseVetoException;
import com.sun.star.util.XCloseable;
import com.sun.star.util.XRefreshable;

/**
 * This is class for abstract conversion.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see org.artofsolving.jodconverter.office.OfficeTask
 */
@SuppressWarnings("PMD")
public abstract class AbstractConversionTask implements OfficeTask {

	/**
	 * inputFile File.
	 */
	private final File inputFile;
	
	/**
	 * outputFile File.
	 */
	private final File outputFile;
	
	/**
	 * inputFileURL String.
	 */
	private final String inputFileURL;

	/**
	 * file buffer.
	 */
	private final byte[] buffer;

	/**
	 * Constructor.
	 * @param inputFile File
	 * @param outputFile File
	 */
	public AbstractConversionTask(final File inputFile, final File outputFile) {
		this.inputFile = inputFile;
		this.outputFile = outputFile;
		this.inputFileURL = null;
		this.buffer = null;
	}

	/**
	 * Constructor.
	 * @param inputFileURL String
	 * @param outputFile File
	 */
	public AbstractConversionTask(final String inputFileURL, final File outputFile) {
		this.inputFile = null;
		this.outputFile = outputFile;
		this.inputFileURL = inputFileURL;
		this.buffer = null;
	}

	/**
	 * Constructor.
	 * @param fileBuffer byte[]
	 * @param outputFile File
	 */
	public AbstractConversionTask(final byte[] buffer, final File outputFile) {
		this.inputFile = null;
		this.inputFileURL = null;
		this.outputFile = outputFile;
		this.buffer = buffer;
	}

	protected abstract Map<String, ?> getLoadProperties(File inputFile);

	protected abstract Map<String, ?> getStoreProperties(File outputFile, XComponent document);

	public void execute(final OfficeContext context) throws OfficeException {
		XComponent document = null;
		try {
			if (inputFile != null) {
				document = loadDocument(context, inputFile);
			} else if (inputFileURL != null) {
				document = loadDocument(context, inputFileURL);
			} else if (buffer != null) {
				document = loadDocument(context, buffer);
			} else {
				throw new OfficeException("No reference to input document found.");
			}
			storeDocument(document, outputFile);
		} catch (final OfficeException officeException) {
			throw officeException;
		} catch (final Exception exception) {
			throw new OfficeException("conversion failed", exception);
		} finally {
			if (document != null) {
				final XCloseable closeable = cast(XCloseable.class, document);
				if (closeable != null) {
					try {
						closeable.close(true);
					} catch (final CloseVetoException closeVetoException) {
						// whoever raised the veto should close the document
					}
				} else {
					document.dispose();
				}
			}
		}
	}

	private XComponent loadDocument(final OfficeContext context, final File inputFile) throws OfficeException {
		final String actualFilePath = EphesoftStringUtil.getDecodedString(inputFile.getPath());
		final File actualFile = new File(actualFilePath);
		final boolean actualFileExists = actualFile.exists();
		if (!actualFileExists) {
			throw new OfficeException("input document not found: " + actualFile.getPath());
		}
		// if (!inputFile.exists()) {
		// throw new OfficeException("input document not found: " + inputFile.getPath());
		// }
		final XComponentLoader loader = cast(XComponentLoader.class, context.getService(SERVICE_DESKTOP));
		XComponent document = null;
		try {
			final PropertyValue[] loadProps = new PropertyValue[1];
			loadProps[0] = new PropertyValue();
			loadProps[0].Name = "Hidden";
			loadProps[0].Value = true;
			document = loader.loadComponentFromURL(toUrl(inputFile), "_blank", 0, loadProps);
		} catch (final IllegalArgumentException illegalArgumentException) {
			throw new OfficeException("could not load document: " + inputFile.getPath(), illegalArgumentException);
		} catch (final ErrorCodeIOException errorCodeIOException) {
			throw new OfficeException("could not load document: " + inputFile.getPath() + "; errorCode: "
					+ errorCodeIOException.ErrCode, errorCodeIOException);
		} catch (final IOException ioException) {
			throw new OfficeException("could not load document: " + inputFile.getPath(), ioException);
		}
		if (document == null) {
			throw new OfficeException("could not load document: " + inputFile.getPath());
		}
		final XRefreshable refreshable = cast(XRefreshable.class, document);
		if (refreshable != null) {
			refreshable.refresh();
		}
		return document;
	}

	private XComponent loadDocument(final OfficeContext officeContext, final byte[] buffer) {
		XComponent document = null;
		final XComponentLoader loader = cast(XComponentLoader.class, officeContext.getService(SERVICE_DESKTOP));
		final XInputStream xStream = new ByteArrayToXInputStreamAdapter(buffer);

		final PropertyValue[] args = new PropertyValue[2];

		args[0] = new PropertyValue();

		args[0].Name = "InputStream";

		args[0].Value = xStream;

		args[0].State = PropertyState.DIRECT_VALUE;

		args[1] = new PropertyValue();

		args[1].Name = "Hidden";

		args[1].Value = new Boolean(true);

		try {
			document = loader.loadComponentFromURL("private:stream", "_default", 0, args);
		} catch (final IOException e) {
			e.printStackTrace();
		} catch (final IllegalArgumentException e) {
			e.printStackTrace();
		}
		return document;
	}

	private XComponent loadDocument(final OfficeContext context, String inputFileURL) throws OfficeException {

		final XComponentLoader loader = cast(XComponentLoader.class, context.getService(SERVICE_DESKTOP));
		XComponent document = null;
		try {
			final PropertyValue[] loadProps = new PropertyValue[1];
			loadProps[0] = new PropertyValue();
			loadProps[0].Name = "Hidden";
			loadProps[0].Value = true;
			try {
				inputFileURL = URLEncoder.encode(inputFileURL, "UTF-8");
			} catch (final UnsupportedEncodingException e) {

			}
			document = loader.loadComponentFromURL(inputFileURL, "_blank", 0, loadProps);
		} catch (final IllegalArgumentException illegalArgumentException) {
			throw new OfficeException("could not load document: " + inputFileURL + " . Illegeal arguement at position : "
					+ illegalArgumentException.ArgumentPosition, illegalArgumentException);
		} catch (final ErrorCodeIOException errorCodeIOException) {
			throw new OfficeException("could not load document: " + inputFileURL + "; errorCode: " + errorCodeIOException.ErrCode,
					errorCodeIOException);
		} catch (final IOException ioException) {
			throw new OfficeException("could not load document: " + inputFileURL, ioException);
		}
		if (document == null) {
			throw new OfficeException("could not load document: " + inputFileURL);
		}
		final XRefreshable refreshable = cast(XRefreshable.class, document);
		if (refreshable != null) {
			refreshable.refresh();
		}
		return document;
	}

	private void storeDocument(final XComponent document, final File outputFile) throws OfficeException {
		final Map<String, ?> storeProperties = getStoreProperties(outputFile, document);
		if (storeProperties == null) {
			throw new OfficeException("unsupported conversion");
		}
		try {
			final PropertyValue[] storeProps = new PropertyValue[1];
			storeProps[0] = new PropertyValue();
			storeProps[0].Name = "FilterName";
			storeProps[0].Value = "writer_pdf_Export";
			cast(XStorable.class, document).storeToURL(toUrl(outputFile), storeProps);
		} catch (final ErrorCodeIOException errorCodeIOException) {
			throw new OfficeException("could not store document: " + outputFile.getName() + "; errorCode: "
					+ errorCodeIOException.ErrCode, errorCodeIOException);
		} catch (final IOException ioException) {
			throw new OfficeException("could not store document: " + outputFile.getName(), ioException);
		}
	}

}
