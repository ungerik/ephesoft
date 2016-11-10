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

/**
 * 
 */
package com.ephesoft.dcma.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.Normalizer;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.internet.MimeUtility;
import javax.mail.search.FlagTerm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.mail.imap.IMAPSSLStore;
import com.sun.mail.pop3.POP3SSLStore;

/**
 * This class is a utility file consisting of various APIs related to different functions that can be performed with an Email.
 * 
 * @author Ephesoft
 * @version 1.0
 * 
 */
public class MailUtil {

	private final static Logger LOGGER = LoggerFactory.getLogger(MailUtil.class);

	/**
	 * Private constructor to avoid instantiation of this class.
	 */
	private MailUtil() {
		// DO Nothing.
	}

	/**
	 * This API returns an email store for the provided set of email configuration values like server type, server name, username,
	 * password, port, SSL.
	 * 
	 * @param serverType {@link String}
	 * @param serverName {@link String}
	 * @param userName {@link String}
	 * @param password {@link String}
	 * @param portNumber {@link Integer}
	 * @param isSSL {@link Boolean}
	 * @return email store object {@link Store}
	 * @throws MessagingException
	 */
	public static Store getMailStore(final String serverType, final String serverName, final String userName, final String password,
			final Integer portNumber, final Boolean isSSL) throws MessagingException {
		Store store = null;
		final StringBuilder logMessage = new StringBuilder();
		logMessage.append("Creating an email store object with configuration:\nServer type: ");
		logMessage.append(serverType);
		logMessage.append("\n Server Name: ");
		logMessage.append(serverName);
		logMessage.append("\nUser Name: ");
		logMessage.append(userName);
		logMessage.append("\nPort Number: ");
		logMessage.append(portNumber);
		LOGGER.info(logMessage.toString());
		if (isSSL != null && isSSL) {
			if (serverType.equalsIgnoreCase(IUtilCommonConstants.SERVER_TYPE_POP3)) {
				store = connectWithPOP3SSL(serverType, serverName, userName, password, portNumber);
			} else if (serverType.equalsIgnoreCase(IUtilCommonConstants.SERVER_TYPE_IMAP)) {
				store = connectWithIMAPSSL(serverType, serverName, userName, password, portNumber);
			} else {
				LOGGER.error("Error in Server Type Configuration, only imap/pop3 is allowed. Configuration used : ");
				throw new MessagingException("Error in Server Type Configuration, only imap/pop3 is allowed. Configuration used : ");
			}

		} else {
			store = connectWithNonSSL(serverType, serverName, userName, password, portNumber);
		}
		return store;
	}

	/**
	 * This API connects to a defined POP3 server for the given email configuration settings which include server name, username,
	 * password, port.
	 * 
	 * @param serverType {@link String}
	 * @param serverName {@link String}
	 * @param userName {@link String}
	 * @param password {@link String}
	 * @param portNumber {@link Integer}
	 * @return email store object {@link Store}
	 * @throws MessagingException
	 */
	private static Store connectWithPOP3SSL(final String serverType, final String serverName, final String userName,
			final String password, final Integer portNumber) throws MessagingException {
		Integer finalPortNumber = portNumber;
		final Properties pop3Props = new Properties();
		Store store = null;

		Session session = null;

		pop3Props.setProperty(IUtilCommonConstants.POP3_SOCKET_FACTORY_CLASS_PARAM, IUtilCommonConstants.SSL_FACTORY);
		pop3Props.setProperty(IUtilCommonConstants.POP3_SOCKET_FACTORY_FALLBACK_PARAM, IUtilCommonConstants.FALSE_PARAM_VALUE);

		if (finalPortNumber == null) {
			LOGGER.error("Could not find port number. Trying with default value of 995");
			finalPortNumber = IUtilCommonConstants.POP3_SSL_DEFAULT_PORT_NUMBER;
		}

		final URLName url = new URLName(serverType, serverName, finalPortNumber, "", userName, password);

		session = Session.getInstance(pop3Props, null);
		store = new POP3SSLStore(session, url);
		store.connect();
		return store;

	}

	/**
	 * This API connects to a defined IMAP server for the given email configuration settings which include server name, username,
	 * password, port.
	 * 
	 * @param serverType {@link String}
	 * @param serverName {@link String}
	 * @param userName {@link String}
	 * @param password {@link String}
	 * @param portNumber {@link Integer}
	 * @return email store object {@link Store}
	 * @throws MessagingException
	 */
	private static Store connectWithIMAPSSL(final String serverType, final String serverName, final String userName,
			final String password, final Integer portNumber) throws MessagingException {
		Integer finalPortNumber = portNumber;
		final Properties imapProps = new Properties();
		Store store = null;

		Session session = null;

		imapProps.setProperty(IUtilCommonConstants.IMAP_SOCKET_FACTORY_CLASS_PARAM, IUtilCommonConstants.SSL_FACTORY);
		imapProps.setProperty(IUtilCommonConstants.IMAP_SOCKET_FACTORY_FALLBACK_PARAM, IUtilCommonConstants.FALSE_PARAM_VALUE);
		imapProps.setProperty(IUtilCommonConstants.IMAP_PARTIAL_FETCH_PARAM, IUtilCommonConstants.FALSE_PARAM_VALUE);

		if (finalPortNumber == null) {
			LOGGER.error("Could not find port number. Trying with default value of 993");
			finalPortNumber = IUtilCommonConstants.IMAP_SSL_DEFAULT_PORT_NUMBER;
		}

		final URLName url = new URLName(serverType, serverName, finalPortNumber, "", userName, password);

		session = Session.getInstance(imapProps, null);
		store = new IMAPSSLStore(session, url);
		store.connect();
		return store;

	}

	/**
	 * This API connects to a defined a NON SSL server for the given email configuration settings which include server name, username,
	 * password and port.
	 * 
	 * @param serverType {@link String}
	 * @param serverName {@link String}
	 * @param userName {@link String}
	 * @param password {@link String}
	 * @param portNumber {@link Integer}
	 * @return email store object {@link Store}
	 * @throws MessagingException
	 */
	private static Store connectWithNonSSL(final String serverType, final String serverName, final String userName,
			final String password, final Integer port) throws MessagingException {
		Store store = null;
		Session session = null;
		session = Session.getInstance(System.getProperties(), null);
		Integer connectionPort = null;
		if (port == null) {
			if (serverType.equalsIgnoreCase(IUtilCommonConstants.SERVER_TYPE_POP3)) {
				connectionPort = IUtilCommonConstants.POP3_NON_SSL_DEFAULT_PORT;
			} else if (serverType.equalsIgnoreCase(IUtilCommonConstants.SERVER_TYPE_IMAP)) {
				connectionPort = IUtilCommonConstants.IMAP_NON_SSL_DEFAULT_PORT;
			} else {
				LOGGER.error("Error in Server Type Configuration, only imap/pop3 is allowed. Configuration used : ");
				throw new MessagingException("Error in Server Type Configuration, only imap/pop3 is allowed. Configuration used : ");
			}
		} else {
			connectionPort = port;
		}

		store = session.getStore(serverType.toLowerCase(IUtilCommonConstants.DEAFULT_LOCALE));
		store.connect(serverName, connectionPort, userName, password);
		return store;
	}

	/**
	 * Downloads attachment represented by the given part on the specified path.
	 * 
	 * @param part
	 * @param folderPath
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws MessagingException
	 */
	public static void downloadAttachment(final Part part, final String folderPath) throws FileNotFoundException, IOException,
			MessagingException {
		if (null != part && null != folderPath) {
			final String disPosition = part.getDisposition();
			final String fileName = part.getFileName();
			String decodedText = fileName;
			final StringBuilder fileInfoBuilder = new StringBuilder();
			fileInfoBuilder.append("Disposition type :: ");
			fileInfoBuilder.append(disPosition);
			fileInfoBuilder.append("Attached File Name :: ");
			fileInfoBuilder.append(fileName);
			if (null != fileName) {
				try {
					decodedText = MimeUtility.decodeText(fileName);
					fileInfoBuilder.append("****** Decoded file name :: ");
					fileInfoBuilder.append(decodedText);
				} catch (final UnsupportedEncodingException unsupportedEncodingException) {
					LOGGER.error(EphesoftStringUtil.concatenate("Error while decoding text ", decodedText,
							unsupportedEncodingException.getMessage()));
				}
				decodedText = Normalizer.normalize(decodedText, Normalizer.Form.NFC);
				fileInfoBuilder.append("****** Normalized file name :: ");
				fileInfoBuilder.append(decodedText);
				final File parentFile = new File(folderPath);
				decodedText = FileUtils.getUpdatedFileNameForDuplicateFile(decodedText, parentFile);
				fileInfoBuilder.append("****** Updated file name :: ");
				fileInfoBuilder.append(decodedText);

				// Replacing all NON-ASCII characters present in attachment name
				// with Empty string.
				decodedText = decodedText
						.replaceAll(IUtilCommonConstants.NON_ASCII_CHARACTER_REGEX, IUtilCommonConstants.EMPTY_STRING);
				// Replacing all characters those are not supported in windows
				// or linux file name
				decodedText = decodedText.replaceAll(IUtilCommonConstants.INVALID_CHARACTER_REGEX, IUtilCommonConstants.EMPTY_STRING);
				fileInfoBuilder.append("After ignoring non ASCII character file name :: ");
				fileInfoBuilder.append(decodedText);
			}
			if (null != disPosition && disPosition.equalsIgnoreCase(Part.ATTACHMENT)) {
				fileInfoBuilder.append("DisPosition is ATTACHMENT type.");
				final File file = new File(EphesoftStringUtil.concatenate(folderPath, File.separator, decodedText));
				file.getParentFile().mkdirs();
				saveEmailAttachment(file, part);
			} else if (null != fileName) {
				fileInfoBuilder.append("DisPosition is Null type but file name is valid. Possibly inline attchment");
				final File file = new File(EphesoftStringUtil.concatenate(folderPath, File.separator, decodedText));
				file.getParentFile().mkdirs();
				saveEmailAttachment(file, part);
			}
			LOGGER.info("Attachment info\n " + fileInfoBuilder.toString());
		} else {
			LOGGER.error("Invalid parameters are Specified for method call.");
		}
	}

	/**
	 * This API saves the email attachment represented by the part object on the path associated with the saveFile object.
	 * 
	 * @param saveFile {@link File}
	 * @param part {@link Part}
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws MessagingException
	 */
	public static void saveEmailAttachment(final File saveFile, final Part part) throws FileNotFoundException, IOException,
			MessagingException {
		InputStream inputStream = null;
		try {
			if (part != null) {
				inputStream = part.getInputStream();
				if (inputStream != null) {
					FileUtils.writeFileViaStream(saveFile, inputStream);
				}
			}
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (final IOException e) {
				LOGGER.error("Error while closing the input stream.", e);
			}
		}
	}

	/**
	 * This API handles each message part of a multipart message to extract the mail body. Each part is handled according to its Mime
	 * Type.
	 * 
	 * @param part {@link Part}
	 * @return Mail body content of the individual part {@link String}
	 * @throws MessagingException
	 * @throws IOException
	 */
	public static String getMailPartAsText(final Part part) throws MessagingException, IOException {
		String partString = "";
		if (part.isMimeType(IUtilCommonConstants.MIME_TYPE_TEXT_PLAIN)) {
			final StringBuilder mailBodyBuilder = new StringBuilder();
			mailBodyBuilder.append(IUtilCommonConstants.PLAIN_TEXT_BODY_HTML_TAG_START);
			mailBodyBuilder.append(part.getContent().toString());
			mailBodyBuilder.append(IUtilCommonConstants.PLAIN_TEXT_BODY_HTML_TAG_END);
			partString = mailBodyBuilder.toString();
		} else if (part.isMimeType(IUtilCommonConstants.MIME_TYPE_TEXT)) {
			partString = (String) part.getContent();
		} else if (part.isMimeType(IUtilCommonConstants.MIME_TYPE_IMAGE)) {
			final String contentType = part.getContentType();
			final String disposition = part.getDisposition();
			final String fileName = part.getFileName();
			final StringBuilder imageTextString = new StringBuilder();
			imageTextString.append("[Image]");
			imageTextString.append("[Inline]");
			imageTextString.append(contentType);
			imageTextString.append(fileName);
			partString = imageTextString.toString();
			final StringBuilder imageInfoLogString = new StringBuilder();
			imageInfoLogString.append("Image in body.. with name:: ");
			imageInfoLogString.append(fileName);
			imageInfoLogString.append(" disposition:: ");
			imageInfoLogString.append(disposition);
			imageInfoLogString.append("HTML file tag:: ");
			imageInfoLogString.append(partString);
			LOGGER.info(imageInfoLogString.toString());
		} else if (part.isMimeType(IUtilCommonConstants.MIME_TYPE_MULTIPART)) {
			final Multipart multiPart = (Multipart) part.getContent();
			final StringBuilder partStringBuilder = new StringBuilder();
			BodyPart bodyPart = null;
			for (int i = 0; i < multiPart.getCount(); i++) {
				bodyPart = multiPart.getBodyPart(i);
				if (!bodyPart.isMimeType(IUtilCommonConstants.MIME_TYPE_TEXT_PLAIN)) {
					partStringBuilder.append(getMailPartAsText(bodyPart));
				}
			}
			partString = partStringBuilder.toString();
		} else {
			partString = part.getContent().toString();
		}

		return partString;
	}

	/**
	 * API to set message read flag to true.
	 * 
	 * @param isUsingImapConf
	 * @param currentMessage
	 * @throws MessagingException
	 */
	public static void setMessageProcessedFlag(final boolean isUsingImapConf, final Message currentMessage) throws MessagingException {
		LOGGER.info("Changing the seen flag for message with subject " + currentMessage.getSubject());
		currentMessage.setFlag(Flags.Flag.SEEN, true);

		if (!isUsingImapConf) {
			currentMessage.setFlag(Flags.Flag.DELETED, true);
			currentMessage.isSet(Flags.Flag.SEEN);
		}
	}

	/**
	 * This API is used to extract Message body from a {@link Message}. If the message is Multipart, each part is treated separately.
	 * If the message is Plain Text message, it's body is changed by adding the "pre" HTML tags to maintain the plain text formatting.
	 * 
	 * @param message {@link Message}
	 * @param isTextMailBody
	 * @return Mail body {@link String}
	 * @throws MessagingException
	 * @throws IOException
	 */
	public static String getBodyFromMultipartMessage(final Message message, final boolean isTextMailBody) throws MessagingException,
			IOException {
		String mailContent = "";
		Object messageContentObject;
		messageContentObject = message.getContent();
		if (messageContentObject != null) {
			if (isTextMailBody) {
				if (messageContentObject instanceof Multipart) {
					final Multipart multipart = (Multipart) messageContentObject;
					mailContent = getText((Part) multipart.getBodyPart(0));
				} else {
					mailContent = messageContentObject.toString();
				}
			} else {
				mailContent = getHTMLContent(messageContentObject);
			}
		}
		return mailContent;
	}

	private static String getHTMLContent(final Object messageContentObject) throws MessagingException, IOException {
		String mailContent = "";
		if (messageContentObject instanceof Multipart) {
			// This case happens when either the mail is HTML of any type or
			// Plain text mail with attachment.
			LOGGER.debug("Multipart message encountered. Each part will be used to extract body content.");
			final Multipart multipart = (Multipart) messageContentObject;
			Part bodyPart = (Part) multipart.getBodyPart(0);

			if (bodyPart.isMimeType(IUtilCommonConstants.MIME_TYPE_TEXT)) {
				LOGGER.debug("The concerned email is either HTML mail without attachment or plain text with attachment.");
				// This case happens when mail is either HTML without attachment
				// or Plain text with attachment.
				final Part bodyPartHtml = (Part) multipart.getBodyPart(1);
				if (bodyPartHtml.isMimeType(IUtilCommonConstants.MIME_TYPE_TEXT_HTML)) {
					// This is the case when mail is HTML without attachment.
					LOGGER.debug("The concerned email is either HTML mail without attachment.");
					bodyPart = bodyPartHtml;
				}
			}
			mailContent = getMailPartAsText(bodyPart);
		} else if (messageContentObject instanceof String) {
			// This case happens when we have a plain text mail without
			// attachment.
			LOGGER.debug("Plain text message encountered. Will be rendered as it is after replacing next line with break tag");
			final StringBuilder mailBodyBuilder = new StringBuilder();
			mailBodyBuilder.append(IUtilCommonConstants.PLAIN_TEXT_BODY_HTML_TAG_START);
			mailBodyBuilder.append(messageContentObject.toString());
			mailBodyBuilder.append(IUtilCommonConstants.PLAIN_TEXT_BODY_HTML_TAG_END);
			mailContent = mailBodyBuilder.toString();
		}
		return mailContent;
	}

	/**
	 * Gets the text content from {@link javax.mail.Part} object.
	 * 
	 * @param part {@link javax.mail.Part} object whose text content is to be retrieved
	 * @return returns the text content as a {@link String}
	 * @throws MessagingException
	 * @throws IOException
	 */
	public static String getText(final Part part) throws MessagingException, IOException {
		String partString = "";
		if (part.isMimeType(IUtilCommonConstants.MIME_TYPE_TEXT)) {
			partString = (String) part.getContent();
			// return partString;
		} else if (part.isMimeType(IUtilCommonConstants.MIME_TYPE_IMAGE)) {
			final String contentType = part.getContentType();
			final String disposition = part.getDisposition();
			final String fileName = part.getFileName();
			final StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(IUtilCommonConstants.IMAGE_INLINE_KEYWORD);
			stringBuilder.append(contentType);
			stringBuilder.append(fileName);
			partString = stringBuilder.toString();
			LOGGER.info("Image in body.. with name:: " + fileName + " disposition:: " + disposition);
			LOGGER.info("HTML file tag:: " + partString);
			// if (partString != null) {
			// return partString;
			// }
		} else if (part.isMimeType(IUtilCommonConstants.MIME_TYPE_MULTIPART_ALTERNATIVE)) {
			final Multipart multiPart = (Multipart) part.getContent();
			Part bodyPart = null;
			for (int i = 0; i < multiPart.getCount(); i++) {
				bodyPart = multiPart.getBodyPart(i);
				if (bodyPart.isMimeType(IUtilCommonConstants.MIME_TYPE_TEXT_PLAIN)) {
					if (partString == null) {
						partString = getText(bodyPart);
						// return partString;
					}
					continue;
				} else if (bodyPart.isMimeType(IUtilCommonConstants.MIME_TYPE_TEXT_HTML)) {
					partString = getText(bodyPart);
					// if (partString != null)
					// return partString;
				} else if (bodyPart.isMimeType(IUtilCommonConstants.MIME_TYPE_IMAGE)) {
					final String contentType = bodyPart.getContentType();
					final String disposition = bodyPart.getDisposition();
					final String fileName = bodyPart.getFileName();
					final StringBuilder stringBuilder = new StringBuilder();
					stringBuilder.append(IUtilCommonConstants.IMAGE_INLINE_KEYWORD);
					stringBuilder.append(contentType);
					stringBuilder.append(fileName);
					partString = stringBuilder.toString();
					LOGGER.info("Image in body.. with name:: " + fileName + " disposition:: " + disposition);
					LOGGER.info("HTML file tag:: " + partString);
					// if (partString != null)
					// return partString;
				} else {
					partString = getText(bodyPart);
				}
			}
			// return partString;
		} else if (part.isMimeType(IUtilCommonConstants.MIME_TYPE_MULTIPART)) {
			final Multipart multiPart = (Multipart) part.getContent();
			for (int i = 0; i < multiPart.getCount(); i++) {
				partString = getText(multiPart.getBodyPart(i));
				// if (partString != null)
				// return partString;
			}
		}

		return partString;
	}

	/**
	 * This API is used to read Unseen/New mails from a particular mail folder. Mode of reading the mails varies for different mail
	 * protocols. POP3 automatically returns NEW mails whereas IMAP needs to be specifically searched against the mails with their SEEN
	 * flag as false.
	 * 
	 * @param folder {@link Folder}
	 * @param serverType {@link String}
	 * @return {@link Message}[] the messages which cleared the search criteria. <code>null</code> in case of exception or when server
	 *         type is other than IMAP or POP3.
	 * @throws MessagingException in case there was an error while reading
	 */
	public static Message[] readUnseenMailsFromFolder(final Folder folder, final String serverType) throws MessagingException {
		Message[] messages = null;
		if (serverType.equalsIgnoreCase(IUtilCommonConstants.SERVER_TYPE_IMAP)) {
			// Creating a search query for IMAP protocol. Will only pick mails
			// with SEEN flag as false.
			final Flags seen = new Flags(Flags.Flag.SEEN);
			final FlagTerm unseenFlagTerm = new FlagTerm(seen, false);
			messages = folder.search(unseenFlagTerm);
		} else if (serverType.equalsIgnoreCase(IUtilCommonConstants.SERVER_TYPE_POP3)) {
			// Fetching all messages from POP folder. POP by default returns
			// only unread mails.
			messages = folder.getMessages();
		} else {
			LOGGER.info("Invalid server type " + serverType + " specified.");
		}
		return messages;
	}

	/**
	 * Returns the mail <code>Folder</code> contained in the given <code>Store</code> object whose name is represented by the given
	 * mail folder name.
	 * 
	 * @param store {@link Store}
	 * @param mailFolderName {@link String}
	 * @return {@link Folder}, Returns the folder object if connectivity is successful, and <code>null</code> in any of the following
	 *         cases <li>
	 *         the store is null <li>
	 *         the store is not connected <li>
	 *         the folder does not exists or cannot be connected.
	 * @throws MessagingException
	 */
	public static Folder getFolderFromMailStore(final Store store, final String mailFolderName) throws MessagingException {
		Folder folder = null;
		if (null != store) {
			try {
				folder = store.getDefaultFolder().getFolder(mailFolderName);
				folder.open(Folder.READ_WRITE);
			} catch (final IllegalStateException e) {
				LOGGER.error(EphesoftStringUtil.concatenate("Store is not connected", e.getMessage()), e);
			}
			if (null != folder && !folder.isOpen()) {
				LOGGER.error(EphesoftStringUtil.concatenate("Cannot connect to the folder ", mailFolderName,
						". The folder either does not exist or cannot be opened."));
				throw new MessagingException(EphesoftStringUtil.concatenate("Cannot connect to the folder ", mailFolderName,
						". The folder either does not exist or cannot be opened."));
			}
		}
		return folder;
	}
}
