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

package com.ephesoft.dcma.batch.encryption.util;

import java.io.File;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Result;
import javax.xml.transform.Source;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.XmlMappingException;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.ephesoft.dcma.batch.encryption.service.XMLSignature;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.batch.service.XMLSchemaAdapterService;
import com.ephesoft.dcma.util.constant.PrivateKeyEncryptionAlgorithm;


public class CryptoMarshaller extends Jaxb2Marshaller {

	@Autowired
	private XMLSchemaAdapterService xmlSchemaAdapterService;

	@Autowired
	private XMLKeyFinder xmlKeyFinder;
	@Autowired
	private XMLSignature xmlSignatureService;

	@Autowired
	private BatchSchemaService batchSchemaService;

	@Override
	public void marshal(final Object objectToMarshal, final Result targetStream) throws XmlMappingException {
		if (null != targetStream) {
			try {
				final Marshaller xmlMarshaller = super.createMarshaller();
				xmlMarshaller.marshal(objectToMarshal, targetStream);
			} catch (final JAXBException jaxbException) {
				throw convertJaxbException(jaxbException);
			}
		}
	}

	
	public Object unmarshal(final Source sourceStream) throws XmlMappingException {
		Object unmarshalledObject = null;
		if (null != sourceStream) {
			try {
				final Unmarshaller unmarshaller = super.createUnmarshaller();
				unmarshalledObject = unmarshaller.unmarshal(sourceStream);
			} catch (final JAXBException jaxbException) {
				throw convertJaxbException(jaxbException);
			}
		}
		return unmarshalledObject;
	}



	
	public void marshal(final Object objectToMarshal, final Result targetStream, final byte[] encryptionKey,
			final PrivateKeyEncryptionAlgorithm encryptionAlgorithm) throws XmlMappingException {
		if (null != targetStream) {
			try {
				final Marshaller xmlMarshaller = super.createMarshaller();
				xmlMarshaller.marshal(objectToMarshal, targetStream);
			} catch (final JAXBException jaxbException) {
				throw convertJaxbException(jaxbException);
			}
		}
	}

	
	public Object unmarshal(final File fileToUnMarshal, final Result targetStream, final byte[] encryptionKey,
			final PrivateKeyEncryptionAlgorithm encryptionAlgorithm) throws XmlMappingException {
		final Object unmarshalledObject = null;
		if (null != targetStream) {
			try {
				final Unmarshaller xmlMarshaller = super.createUnmarshaller();
				return xmlMarshaller.unmarshal(fileToUnMarshal);
			} catch (final JAXBException jaxbException) {
				throw convertJaxbException(jaxbException);
			}
		}
		return unmarshalledObject;
	}
}
