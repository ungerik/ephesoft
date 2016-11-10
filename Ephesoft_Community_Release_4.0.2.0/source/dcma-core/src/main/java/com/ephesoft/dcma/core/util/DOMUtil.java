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

package com.ephesoft.dcma.core.util;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.DOMBuilder;
import org.jdom.output.DOMOutputter;
import org.jdom.transform.JDOMResult;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.ephesoft.dcma.core.component.JAXB2Template;

/**
 * Utility to perform DOM operations on an Object. This utility can be used to perform DOM operations, marshaling/unmarshaling a DOM
 * object
 * <p>
 * This DOM utility can be used to marshal/unmarshal jdom, w3c Document.
 * </p>
 * 
 * @author Ephesoft.
 * 
 */
public final class DOMUtil {

	/**
	 * Marshals the Object as an JDOM Document. If creates an in-memory marshaling of the Object. This approach will avoid extra
	 * IO-Operation by converting the Object from one form to another.
	 * 
	 * @param jaxbTemplate {@link JAXB2Template} which stores the context of the marshaller. It also ensures that in memory
	 *            transactions are not encrypted befor processing.
	 * @param objectToMarshal {@link Object} the object which needs to be marshaled. The schema for the object should be provided in
	 *            the context of the marshaler.
	 * @return {@link Document} which is an in-memory JDOM representation of the object.
	 */
	public static Document marshalJDOMDocument(final JAXB2Template jaxbTemplate, final Object objectToMarshal) {
		Document marshalledDocument = null;
		if (jaxbTemplate != null && objectToMarshal != null) {
			final Jaxb2Marshaller marshaller = jaxbTemplate.getJaxb2Marshaller();
			final JDOMResult result = new JDOMResult();
			if (marshaller != null) {
				marshaller.marshal(objectToMarshal, result);
			}
			marshalledDocument = result.getDocument();
		}
		return marshalledDocument;
	}

	/**
	 * API that converts JDOM Document to the W3C document. This is an in-memory transaction of converting jdom document to the W3C
	 * document.
	 * 
	 * @param jdomDocument {@link Document} which needs to be converted.
	 * @return {@link org.w3c.dom.Document} w3c dom representation of the JDOM document.
	 * @throws JDOMException When the Document cannot be converted.
	 */
	public static org.w3c.dom.Document convertToW3CDocument(final Document jdomDocument) throws JDOMException {
		org.w3c.dom.Document w3cDocument = null;
		if (jdomDocument != null) {
			final DOMOutputter xmlOutputter = new DOMOutputter();
			w3cDocument = xmlOutputter.output(jdomDocument);
		}
		return w3cDocument;
	}

	/**
	 * API that converts W3C Document to the JDOM document. This is an in-memory transaction of converting w3c document to the JDOM
	 * document.
	 * 
	 * @param w3cDocument{@link org.w3c.dom.Document} which needs to be converted.
	 * @return {@link Document} jdom representation of the W3C document.
	 */
	public static Document convertToJDOMDocument(final org.w3c.dom.Document w3cDocument) {
		Document jdomDocument = null;
		if (w3cDocument != null) {
			final DOMBuilder domBuilder = new DOMBuilder();
			jdomDocument = domBuilder.build(w3cDocument);
		}
		return jdomDocument;
	}

	/**
	 * Marshals the Object as an W3CDocument. If creates an in-memory marshaling of the Object. This approach will avoid extra
	 * IO-Operation by converting the Object from one form to another.
	 * 
	 * @param jaxbTemplate {@link JAXB2Template} which stores the context of the marshaller. It also ensures that in memory
	 *            transactions are not encrypted before processing.
	 * @param objectToMarshal {@link Object} the object which needs to be marshaled. The schema for the object should be provided in
	 *            the context of the marshaler.
	 * @return {@link Document} which is an in-memory W3C representation of the object.
	 */
	public static org.w3c.dom.Document marshalW3CDocument(final JAXB2Template jaxbTemplate, final Object objectToMarshal)
			throws JDOMException {
		Document marshalledDocument = null;
		if (jaxbTemplate != null && objectToMarshal != null) {
			final Jaxb2Marshaller marshaller = jaxbTemplate.getJaxb2Marshaller();
			final JDOMResult result = new JDOMResult();
			if (marshaller != null) {
				marshaller.marshal(objectToMarshal, result);
				marshalledDocument = result.getDocument();
			}
		}
		return convertToW3CDocument(marshalledDocument);
	}
}
