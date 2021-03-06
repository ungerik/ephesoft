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

package com.ephesoft.dcma.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.PrettyHtmlSerializer;
import org.htmlcleaner.TagNode;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.w3c.tidy.Tidy;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XMLUtil {

	private static final String ISO_ENCODING = "iso-8859-1";
	private static final String DOC_TYPE_OMIT = "omit";
	private static final String UTF_ENCODING = "UTF-8";
	private static final String PROPERTY_INDENT = "indent";
	private static final String VALUE_YES = "yes";
	public static final String HTML_PARSER = "html_parser";
	public static final String HTML_CLEANER = "0";
	public static final String JTIDY = "1";

	private static DocumentBuilder getBuilder() throws FactoryConfigurationError, ParserConfigurationException {
		return getBuilder(false);
	}

	private static DocumentBuilder getBuilder(boolean isXPATH) throws FactoryConfigurationError, ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		if (isXPATH) {
			factory.setNamespaceAware(true);
		}
		return factory.newDocumentBuilder();
	}

	public static Document createDocumentFrom(InputStream input) throws Exception {
		DocumentBuilder builder = getBuilder();
		return builder.parse(input);
	}

	public static Document createDocumentFrom(File file) throws IllegalArgumentException, SAXException, IOException,
			FactoryConfigurationError, ParserConfigurationException {
		return createDocumentFrom(file, false);
	}

	public static Document createDocumentFrom(File file, boolean isXPATH) throws IllegalArgumentException, SAXException, IOException,
			FactoryConfigurationError, ParserConfigurationException {
		DocumentBuilder builder = getBuilder(isXPATH);
		return builder.parse(file);
	}

	public static Document createDocumentFromResource(String resourceName) throws Exception {
		ClassLoader loader = XMLUtil.class.getClassLoader();
		InputStream inputStream = loader.getResourceAsStream(resourceName);
		return createDocumentFrom(inputStream);
	}

	public static Document createDocumentFromAbsoluteResource(String resourceName) throws Exception {
		return createDocumentFrom(new File(resourceName));
	}

	public static Document createDocumentFrom(String xmlString) throws IllegalArgumentException, SAXException, IOException,
			FactoryConfigurationError, ParserConfigurationException {
		StringReader strReader = new StringReader(xmlString);
		InputSource iSrc = new InputSource(strReader);
		DocumentBuilder builder = getBuilder();
		return builder.parse(iSrc);
	}

	public static Document createNewDocument() throws Exception {
		DocumentBuilder builder = getBuilder();
		return builder.newDocument();
	}

	public static DOMSource createSourceFromFile(File file) throws Exception {
		Document document = createDocumentFrom(file);
		return getDomSourceForDoc(document);
	}

	public static DOMSource createSourceFromStream(InputStream inputStream) throws Exception {
		Document document = createDocumentFrom(inputStream);
		return getDomSourceForDoc(document);
	}

	public static DOMSource createSourceFromStream(final InputStream inputStream, final File file) throws Exception {
		Document document = createDocumentFrom(inputStream);
		return getDomSourceForDoc(document, file);
	}

	private static DOMSource getDomSourceForDoc(Document document) throws TransformerFactoryConfigurationError,
			TransformerConfigurationException {
		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer = factory.newTransformer();
		transformer.setOutputProperty(PROPERTY_INDENT, VALUE_YES);
		return new javax.xml.transform.dom.DOMSource(document);
	}

	private static DOMSource getDomSourceForDoc(Document document, File file) throws TransformerFactoryConfigurationError,
			TransformerConfigurationException {
		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer = factory.newTransformer();
		transformer.setOutputProperty(PROPERTY_INDENT, VALUE_YES);
		return new FileDOMSource(document, file);
	}

	public static final int WRITER_SIZE = 1024 * 4;

	public static String toXMLString(Document document) throws Exception {

		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer = factory.newTransformer();
		transformer.setOutputProperty(PROPERTY_INDENT, VALUE_YES);
		javax.xml.transform.dom.DOMSource src = new javax.xml.transform.dom.DOMSource(document);
		java.io.CharArrayWriter writer = new java.io.CharArrayWriter(1024);
		javax.xml.transform.stream.StreamResult result = new javax.xml.transform.stream.StreamResult(writer);
		transformer.transform(src, result);
		return writer.toString();
	}

	/**
	 * This method should eventually replace the toXMLString(Document doc) method.
	 * 
	 * @param xmlNode
	 * @return
	 * @throws Exception
	 */
	public static String xmlNode2String(Node xmlNode) throws Exception {
		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer = factory.newTransformer();
		transformer.setOutputProperty(PROPERTY_INDENT, VALUE_YES);
		javax.xml.transform.dom.DOMSource src = new javax.xml.transform.dom.DOMSource(xmlNode);
		java.io.CharArrayWriter writer = new java.io.CharArrayWriter(1024);
		javax.xml.transform.stream.StreamResult result = new javax.xml.transform.stream.StreamResult(writer);
		transformer.transform(src, result);
		return writer.toString();
	}

	public static void flushDocumentToFile(Document document, String fileName) throws FileNotFoundException, TransformerException {
		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer = factory.newTransformer();
		transformer.setOutputProperty(PROPERTY_INDENT, VALUE_YES);
		javax.xml.transform.dom.DOMSource src = new javax.xml.transform.dom.DOMSource(document);
		javax.xml.transform.stream.StreamResult result = new javax.xml.transform.stream.StreamResult(new FileOutputStream(fileName));
		transformer.transform(src, result);

	}

	public static void appendLeafChild(Document doc, Node parent, String childName, String childData) {
		Element child = doc.createElement(childName);
		if (childData != null && childData.length() != 0) {
			Text text = doc.createTextNode(childData);

			child.appendChild(text);
		}
		parent.appendChild(child);
	}

	public static Document getClonedXMLDocument(Document xmlDoc) throws Exception {
		String XMLString = toXMLString(xmlDoc);
		return createDocumentFrom(XMLString);

	}

	public static String applyTransformation(Document doc, String xsltPath) throws Exception {
		InputStream xsltFile = XMLUtil.class.getClassLoader().getResourceAsStream(xsltPath);
		TransformerFactory xsltFactory = TransformerFactory.newInstance();
		StreamSource inputSource = new StreamSource(xsltFile);
		Transformer transformer = xsltFactory.newTransformer(inputSource);
		javax.xml.transform.dom.DOMSource src = new javax.xml.transform.dom.DOMSource(doc);
		java.io.CharArrayWriter writer = new java.io.CharArrayWriter(1024);
		javax.xml.transform.stream.StreamResult result = new javax.xml.transform.stream.StreamResult(writer);
		transformer.transform(src, result);
		return writer.toString();
	}

	public static byte[] applyXSLTransformation(Document xmlDocument, String stylesheetFileLocation) throws Exception {
		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer = factory.newTransformer(new StreamSource(stylesheetFileLocation));
		transformer.setOutputProperty(PROPERTY_INDENT, VALUE_YES);
		javax.xml.transform.dom.DOMSource src = new javax.xml.transform.dom.DOMSource(xmlDocument);
		java.io.CharArrayWriter writer = new java.io.CharArrayWriter(1024);
		javax.xml.transform.stream.StreamResult result = new javax.xml.transform.stream.StreamResult(writer);
		transformer.transform(src, result);
		return writer.toString().getBytes();
	}

	/**
	 * @param pathOfHOCRFile String
	 * @param outputFilePath String
	 * @return FileWriter
	 * @throws IOException
	 */
	public static void htmlOutputStream(final String pathOfHOCRFile, final String outputFilePath) throws IOException {
		ApplicationConfigProperties applicationConfigProperties = ApplicationConfigProperties.getApplicationConfigProperties();
		String htmlParser = applicationConfigProperties.getProperty(HTML_PARSER);
		if (htmlParser != null && htmlParser.equals(HTML_CLEANER)) {
			htmlOutputStreamViaHtmlCleaner(pathOfHOCRFile, outputFilePath);
		} else {
			htmlOutputStreamViaTidy(pathOfHOCRFile, outputFilePath);
		}
	}

	public static void htmlOutputStreamViaTidy(final String pathOfHOCRFile, final String outputFilePath) throws IOException {

		Tidy tidy = new Tidy();
		tidy.setXHTML(true);
		tidy.setDocType(DOC_TYPE_OMIT);
		tidy.setInputEncoding(UTF_ENCODING);
		tidy.setOutputEncoding(UTF_ENCODING);
		tidy.setForceOutput(true);
		tidy.setWraplen(0);
		FileInputStream inputStream = null;

		OutputStream fout = null;
		OutputStream bout = null;
		OutputStreamWriter out = null;
		try {
			/*
			 * Fix for UTF-8 encoding to support special characters in turkish and czech language. UTF-8 encoding supports major
			 * characters in all the languages
			 */
			fout = new FileOutputStream(outputFilePath);
			bout = new BufferedOutputStream(fout);
			out = new OutputStreamWriter(bout, UTF_ENCODING);

			inputStream = new FileInputStream(pathOfHOCRFile);
			tidy.parse(inputStream, out);
		} finally {
			try {
				if (null != inputStream) {
					inputStream.close();
				}
			} catch (IOException e) {

			}

			try {
				if (null != inputStream) {
					inputStream.close();
				}
			} catch (IOException e) {

			}
			try {
				if (null != fout) {
					fout.flush();
					fout.close();
				}
			} catch (IOException e) {

			}
			try {
				if (null != bout) {
					bout.flush();
					bout.close();
				}
			} catch (IOException e) {

			}
			try {
				if (null != out) {
					out.close();
				}
			} catch (IOException e) {

			}

		}
	}

	public static void htmlOutputStreamViaHtmlCleaner(String pathOfHOCRFile, String outputFilePath) throws IOException {
		CleanerProperties cleanerProps = new CleanerProperties();

		// set some properties to non-default values
		cleanerProps.setTransResCharsToNCR(true);
		cleanerProps.setTranslateSpecialEntities(true);
		cleanerProps.setOmitComments(true);
		cleanerProps.setOmitDoctypeDeclaration(true);
		cleanerProps.setOmitXmlDeclaration(false);
		HtmlCleaner cleaner = new HtmlCleaner(cleanerProps);

		// take default cleaner properties
		// CleanerProperties props = cleaner.getProperties();
		FileInputStream hOCRFileInputStream = new FileInputStream(pathOfHOCRFile);
		TagNode tagNode = cleaner.clean(hOCRFileInputStream, UTF_ENCODING);
		if (null != hOCRFileInputStream) {
			hOCRFileInputStream.close();
		}
		try {
			new PrettyHtmlSerializer(cleanerProps).writeToFile(tagNode, outputFilePath, UTF_ENCODING);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param pathOfHOCRFile String
	 * @param outputFilePath String
	 * @return FileWriter
	 * @throws IOException
	 */

	public static void htmlOutputStreamForISOEncoding(final String pathOfHOCRFile, final String outputFilePath) throws IOException {

		Tidy tidy = new Tidy();
		tidy.setXHTML(true);
		tidy.setDocType(DOC_TYPE_OMIT);
		tidy.setInputEncoding(ISO_ENCODING);
		tidy.setOutputEncoding(ISO_ENCODING);
		tidy.setHideEndTags(false);

		FileInputStream inputStream = null;
		FileWriter outputStream = null;
		try {
			inputStream = new FileInputStream(pathOfHOCRFile);
			outputStream = new FileWriter(outputFilePath);
			tidy.parse(inputStream, outputStream);
		} finally {
			if (null != inputStream) {
				inputStream.close();
			}
			if (null != outputStream) {
				outputStream.flush();
				outputStream.close();
			}
		}
	}

	/**
	 * This method transforms source xml into target xml using XSLT provided.
	 * 
	 * @param pathToSourceXML String
	 * @param pathToTargetXML String
	 * @param pathToXSL String
	 * @throws TransformerException
	 * @throws FileNotFoundException
	 */
	public static void transformXML(final String pathToSourceXML, final String pathToTargetXML, final InputStream xslStream)
			throws TransformerException, FileNotFoundException, Exception {
		InputStream fis = new FileInputStream(new File(pathToSourceXML));
		transformXMLWithStream(fis, pathToTargetXML, xslStream);
	}

	/**
	 * @param pathToSourceXML
	 * @param pathToTargetXML
	 * @param xslStream
	 * @throws TransformerFactoryConfigurationError
	 * @throws TransformerConfigurationException
	 * @throws FileNotFoundException
	 * @throws TransformerException
	 * @throws IOException
	 */
	public static void transformXMLWithStream(final InputStream fis, final String pathToTargetXML, final InputStream xslStream)
			throws TransformerFactoryConfigurationError, TransformerConfigurationException, FileNotFoundException,
			TransformerException, IOException {
		FileOutputStream fileOutputStream = null;
		try {
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer(new javax.xml.transform.stream.StreamSource(xslStream));
			fileOutputStream = new FileOutputStream(pathToTargetXML);
			transformer.transform(new javax.xml.transform.stream.StreamSource(fis), new javax.xml.transform.stream.StreamResult(
					fileOutputStream));
		} finally {
			if (fileOutputStream != null) {
				fileOutputStream.flush();
				fileOutputStream.close();
			}

			if (fis != null) {
				fis.close();
			}
		}
	}

	/**
	 * API for creating JDOM document using file path.
	 * 
	 * @param filePath {@link String}
	 * @return
	 * @throws Exception
	 */
	public static org.jdom.Document createJDOMDocumentFrom(String filePath) throws Exception {
		return new SAXBuilder().build(filePath);
	}

	/**
	 * API for creating JDOM document using file.
	 * 
	 * @param file {@link File}
	 * @return
	 * @throws Exception
	 */
	public static org.jdom.Document createJDOMDocumentFromFile(File file) throws JDOMException, IOException {
		return new SAXBuilder().build(file);
	}

	/**
	 * API for creating JDOM document from input stream.
	 * 
	 * @param inputStream {@link InputStream}
	 * @return
	 * @throws Exception
	 */
	public static org.jdom.Document createJDOMDocumentFromInputStream(InputStream inputStream) throws Exception {
		return new SAXBuilder().build(inputStream);
	}

	/**
	 * API to get the value for an xPathExpression from a {@link org.w3c.dom.Document}
	 * 
	 * @param doc {@link org.w3c.dom.Document}
	 * @param xPathExpression {@link String}
	 * @return
	 */
	public static String getValueFromXML(final Document doc, final String xPathExpression) throws XPathExpressionException {
		XPath xpath = XPathFactory.newInstance().newXPath();
		String requiredValue = null;
		try {
			XPathExpression expr = xpath.compile(xPathExpression);
			Object result = expr.evaluate(doc, XPathConstants.NODESET);
			NodeList nodes = (NodeList) result;
			Node item = nodes.item(0);
			if (item != null) {
				requiredValue = item.getFirstChild().getNodeValue();
			}
		} catch (Exception e) {
		}
		return requiredValue;
	}

	/**
	 * An API get {@link NodeList} for an xPathExpression from an xml.
	 * 
	 * @param document {@link Object} object representing the document from which node list is to be extracted
	 * @param xPathExpression {@link XPathExpression} XPath Expression
	 * @return {@link NodeList}
	 * @throws XPathExpressionException
	 */
	public static NodeList getDataFromXml(final Object document, final XPathExpression xPathExpression)
			throws XPathExpressionException {
		NodeList wordList = null;
		if (document != null && xPathExpression != null) {
			wordList = (NodeList) xPathExpression.evaluate(document, XPathConstants.NODESET);
		}
		return wordList;
	}

	/**
	 * An API get value of a node based on a xPathExpression from an xml.
	 * 
	 * @param xPathExpr
	 * @param xmlNode
	 * @return
	 * @throws XPathExpressionException
	 */
	public static String getNodeValue(final XPathExpression xPathExpr, final Node xmlNode) throws XPathExpressionException {
		Node imageNameNode = null;
		String nodeValue = null;
		NodeList imageNameNodeList = (NodeList) getDataFromXml(xmlNode, xPathExpr);
		if (imageNameNodeList != null && imageNameNodeList.getLength() > 0) {
			// Assuming there will only be one name node for an image node.
			imageNameNode = imageNameNodeList.item(0);
		}
		if (imageNameNode != null) {
			nodeValue = imageNameNode.getTextContent();
		}
		return nodeValue;
	}

}
