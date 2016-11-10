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

package com.ephesoft.dcma.mail.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.StringUtils;

import com.ephesoft.dcma.core.common.MailMetaData;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.mail.MailContentModel;
import com.ephesoft.dcma.mail.SendMailException;
import com.ephesoft.dcma.mail.constant.MailConstants;
import com.ephesoft.dcma.util.EphesoftStringUtil;

import freemarker.template.Configuration;

public class MailServiceImpl implements MailService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MailServiceImpl.class);

	/**
	 * {@link String} mail id of the sender.
	 */
	private String fromMail;

	/**
	 * {@link String} mail id of the receiver.
	 */
	private String toMail;
	private JavaMailSenderImpl mailSender;
	private Configuration freemarkerMailConfiguration;
	private boolean suppressMail;
	private static final String ORIGINAL_MAIL_MESSAGE = "\n\nErrorneous mail content Starts from here.\n\n";

	/**
	 * Gets mail id of the sender.
	 * 
	 * @return {@link String}
	 */
	public String getFromMail() {
		return fromMail;
	}

	/**
	 * Sets mail id of the sender.
	 * 
	 * @param fromMail {@link String}
	 */
	public void setFromMail(final String fromMail) {
		this.fromMail = fromMail;
	}

	/**
	 * Gets mail id of the receiver.
	 * 
	 * @return {@link String}
	 */
	public String getToMail() {
		return toMail;
	}

	/**
	 * Sets mail id of the receiver.
	 * 
	 * @param toMail {@link String}
	 */
	public void setToMail(final String toMail) {
		this.toMail = toMail;
	}

	public void setMailSender(final JavaMailSenderImpl mailSender) {
		this.mailSender = mailSender;
	}

	public void setFreemarkerMailConfiguration(final Configuration freemarkerMailConfiguration) {
		this.freemarkerMailConfiguration = freemarkerMailConfiguration;
	}

	public void setSuppressMail(final boolean suppressMail) {
		this.suppressMail = suppressMail;
	}

	@Override
	public void sendTextMail(final MailMetaData mailMetaData, final String text) {
		if (suppressMail) {
			LOGGER.info("Message suppress switch is on in dcma-mail.properties file. Error mail notfication cann't be send");
			return;
		}
		setMailProperties();
		final SimpleMailMessage mailMessage = new SimpleMailMessage();
		if (mailMetaData.getFromAddress() != null) {
			mailMessage.setFrom(EphesoftStringUtil.concatenate(mailMetaData.getFromName(), MailConstants.LESS_SYMBOL,
					mailMetaData.getFromAddress(), MailConstants.GREATER_SYMBOL));
		}
		if (null != mailMetaData.getSubject()) {
			mailMessage.setSubject(mailMetaData.getSubject());
		}
		if (mailMetaData.getCcAddresses() != null && mailMetaData.getCcAddresses().size() > 0) {
			mailMessage.setCc((String[]) mailMetaData.getCcAddresses().toArray(new String[mailMetaData.getCcAddresses().size()]));
		}
		if (mailMetaData.getBccAddresses() != null && mailMetaData.getBccAddresses().size() > 0) {
			mailMessage.setBcc((String[]) mailMetaData.getBccAddresses().toArray(new String[mailMetaData.getBccAddresses().size()]));
		}
		if (mailMetaData.getToAddresses() != null && mailMetaData.getToAddresses().size() > 0) {
			mailMessage.setTo((String[]) mailMetaData.getToAddresses().toArray(new String[mailMetaData.getToAddresses().size()]));
		}
		mailMessage.setText(text);
		try {
			mailSender.send(mailMessage);
		} catch (MailException mailException) {
			LOGGER.error("Eror while sending mail to configured mail account", mailException);
			throw new SendMailException(EphesoftStringUtil.concatenate("Error sending mail: ", mailMetaData.toString()), mailException);
		}
		LOGGER.info("mail sent successfully");
	}

	/**
	 * API to Set socket factory class according to the SSL or NON-SSL option selected by user.
	 */
	private void setMailProperties() {
		Properties mailProperties = mailSender.getJavaMailProperties();
		String sslEnabled = (String) mailProperties.get(MailConstants.MAIL_SMTP_SSL_PROPERTY);
		if (!EphesoftStringUtil.isNullOrEmpty(sslEnabled) && sslEnabled.equalsIgnoreCase(MailConstants.TRUE)) {
			mailProperties.setProperty(MailConstants.MAIL_SOCKET_PROPERTY, MailConstants.MAIL_SOCKET_CLASS);
		}
	}

	@Override
	public void sendMailWithPreviousMailContent(final MailMetaData mailMetaData, final String text, final Message previousMailMessage) {
		LOGGER.debug("Sending mail with content from previous mail(MailServiceImpl)");
		if (suppressMail) {
			return;
		}
		setMailProperties();
		MimeMessagePreparator messagePreparator = new MimeMessagePreparator() {

			public void prepare(MimeMessage mimeMessage) throws Exception {

				if (null != mailMetaData.getCcAddresses() && mailMetaData.getCcAddresses().size() > 0) {
					setAddressToMessage(RecipientType.CC, mimeMessage, mailMetaData.getCcAddresses());
				}
				if (null != mailMetaData.getBccAddresses() && mailMetaData.getBccAddresses().size() > 0) {
					setAddressToMessage(RecipientType.BCC, mimeMessage, mailMetaData.getBccAddresses());
				}
				if (null != mailMetaData.getToAddresses() && mailMetaData.getToAddresses().size() > 0) {
					setAddressToMessage(RecipientType.TO, mimeMessage, mailMetaData.getToAddresses());
				}

				if (null != mailMetaData.getFromAddress()) {
					mimeMessage.setFrom(new InternetAddress(new StringBuilder().append(mailMetaData.getFromName())
							.append(MailConstants.LESS_SYMBOL).append(mailMetaData.getFromAddress())
							.append(MailConstants.GREATER_SYMBOL).toString()));
				}

				if (null != mailMetaData.getSubject() && null != text) {
					mimeMessage.setSubject(mailMetaData.getSubject());
				}

				if (null != text) {
					mimeMessage.setText(text);
				}

				// Create your new message part
				BodyPart messageBodyPart = new MimeBodyPart();
				messageBodyPart.setText(EphesoftStringUtil.concatenate(text, ORIGINAL_MAIL_MESSAGE));

				// Create a multi-part to combine the parts
				Multipart multipart = new MimeMultipart();
				multipart.addBodyPart(messageBodyPart);

				// Create and fill part for the forwarded content
				messageBodyPart = new MimeBodyPart();
				messageBodyPart.setDataHandler(previousMailMessage.getDataHandler());

				// Add part to multi part
				multipart.addBodyPart(messageBodyPart);

				// Associate multi-part with message
				mimeMessage.setContent(multipart);

			}
		};
		try {
			mailSender.send(messagePreparator);
		} catch (MailException mailException) {
			LOGGER.error("Error while sending mail to configured mail account", mailException);
			throw new SendMailException(EphesoftStringUtil.concatenate("Error sending mail: ", mailMetaData.toString()), mailException);
		}
		LOGGER.info("mail sent successfully");
	}

	/**
	 * To set receiptent's mail ids in message to be sent.
	 * 
	 * @param receiptentType {@link RecipientType}
	 * @param mimeMessage {@link MimeMessage}
	 * @param addressList {@link List}
	 */
	private void setAddressToMessage(RecipientType receiptentType, MimeMessage mimeMessage, List<String> addressList) {
		try {
			for (String address : addressList) {
				mimeMessage.addRecipients(receiptentType, address);
			}
		} catch (MessagingException messagingException) {
			throw new SendMailException(new StringBuilder().append("Error while setting address. PLease verify email addresses")
					.toString(), messagingException);
		}

	}

	@Override
	public void sendTextMailWithClasspathTemplate(final MailMetaData mailMetaData, final String templateLocation,
			final MailContentModel model) {
		LOGGER.debug("Inside sendTextMailWithClasspathTemplate");
		if (null != mailMetaData && null != templateLocation && null != model) {
			model.add(MailConstants.MAIL_META, mailMetaData);
			try {
				LOGGER.info("Sending mail from: " + mailMetaData.getFromAddress());
				final String result = FreeMarkerTemplateUtils.processTemplateIntoString(
						freemarkerMailConfiguration.getTemplate(templateLocation), model.getModel());
				LOGGER.info("Resultant mail is: " + result);
				sendTextMail(mailMetaData, result);
			} catch (Exception exception) {
				LOGGER.error("Error while sending mail(Workflow error): ", exception);
				throw new SendMailException(EphesoftStringUtil.concatenate("Error sending mail: ", mailMetaData.toString()), exception);
			}
		} else {
			LOGGER.error("Some of the input values are null. \n 1) MailMetaData  2) TemplateLocation 3) model");
		}
	}

	@Override
	public void sendTextMailWithClasspathTemplate(final MailMetaData mailMetaData, final String templateLocation,
			final MailContentModel model, Message previousMailMessage) {
		LOGGER.debug("Inside sendTextMailWithClasspathTemplate");
		if (null != mailMetaData && null != templateLocation && null != model) {
			model.add(MailConstants.MAIL_META, mailMetaData);
			try {
				LOGGER.info(EphesoftStringUtil.concatenate("Sending mail from: ", mailMetaData.getFromAddress(),
						" \n Template Location:  ", templateLocation));
				final String result = FreeMarkerTemplateUtils.processTemplateIntoString(
						freemarkerMailConfiguration.getTemplate(templateLocation), model.getModel());
				LOGGER.info(EphesoftStringUtil.concatenate("Resultant mail is (without previous mail content): ", result));
				sendMailWithPreviousMailContent(mailMetaData, result, previousMailMessage);
			} catch (Exception exception) {
				LOGGER.error("Error while sending mail(Mail processing error): ", exception);
				throw new SendMailException(EphesoftStringUtil.concatenate("Error sending mail: ", mailMetaData.toString()), exception);
			}
		} else {
			LOGGER.error("Some of the input values are null. \n 1) MailMetaData  2) TemplateLocation 3) model");
		}
	}

	@Override
	public void mailOnWorkflowError(BatchInstance batchInstance, Exception exception, String subject, String mailTemplatePath,
			String errorPlugin) throws DCMAApplicationException {
		LOGGER.trace("Creating  mailMetaData in Mail service impl");
		if (null != batchInstance && null != exception) {
			MailMetaData metaData = new MailMetaData();
			metaData.setFromAddress(this.fromMail);
			metaData.setFromName(this.fromMail);
			metaData.setSubject(subject);
			metaData.setToAddresses(new ArrayList<String>(StringUtils.commaDelimitedListToSet(toMail)));
			MailContentModel model = new MailContentModel();
			model.add(MailConstants.WORKFLOW, batchInstance.getBatchClass().getName());
			model.add(MailConstants.BATCH_INSTANCE, batchInstance.getIdentifier());
			model.add(MailConstants.BATCH_NAME, batchInstance.getBatchName());
			String exceptionMessage = exception.getMessage();
			if (null == exceptionMessage) {
				exceptionMessage = ICommonConstants.EMPTY_STRING;
			}
			model.add(MailConstants.ERROR_MESSAGE, exceptionMessage);
			model.add(MailConstants.ERROR_LOG, ExceptionUtils.getFullStackTrace(exception));
			model.add(MailConstants.ERROR_PLUGIN, errorPlugin);
			try {
				sendTextMailWithClasspathTemplate(metaData, mailTemplatePath, model);
			} catch (final SendMailException sendMailException) {
				LOGGER.error("Error encountered while sending mail.", sendMailException);
				throw new DCMAApplicationException("Error encountered while sending mail.", sendMailException);
			}
		} else {
			LOGGER.error("Either or both batchInstance or exception object is null. Error notification mail cann't be sent.");
		}
		LOGGER.trace("Exiting mail send on error for workflow in Mail service impl");
	}

	@Override
	public void mailOnError(final String subject, final Message previousMailMessage, final String batchClassName,
			final String userName, final String mailTemplatePath) throws DCMAApplicationException {
		LOGGER.debug("Creating  mailMetaData odr mail or error.");
		if (null != subject && null != batchClassName && null != userName && null != previousMailMessage) {
			final MailMetaData metaData = new MailMetaData();
			metaData.setFromAddress(fromMail);
			metaData.setFromName(fromMail);
			metaData.setSubject(subject);
			metaData.setToAddresses(new ArrayList<String>(StringUtils.commaDelimitedListToSet(toMail)));
			final MailContentModel model = new MailContentModel();
			model.add(MailConstants.BATCH_CLASS, batchClassName);
			model.add(MailConstants.USER_ID, userName);
			try {
				String ccString = Arrays.toString(previousMailMessage.getHeader(MailConstants.CC))
						.replace(MailConstants.OPENING_BRACKET, MailConstants.SPACE)
						.replace(MailConstants.CLOSING_BRACKET, MailConstants.EMPTY_STRING);
				String toString = Arrays.toString(previousMailMessage.getHeader(MailConstants.TO))
						.replace(MailConstants.OPENING_BRACKET, MailConstants.SPACE)
						.replace(MailConstants.CLOSING_BRACKET, MailConstants.EMPTY_STRING);

				// below date format will be used to format date string received
				// from mail header. Mail header can provide a lot more
				// info.
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(MailConstants.DATE_FORMAT);
				model.add(MailConstants.SUBJECT, previousMailMessage.getSubject());
				model.add(
						MailConstants.FROM,
						Arrays.toString(previousMailMessage.getHeader(MailConstants.FROM))
								.replace(MailConstants.OPENING_BRACKET, MailConstants.SPACE)
								.replace(MailConstants.CLOSING_BRACKET, ""));
				model.add(MailConstants.TO, toString.equalsIgnoreCase(MailConstants.NULL_STRING) ? MailConstants.EMPTY_STRING
						: toString);
				model.add(MailConstants.CC, ccString.equalsIgnoreCase(MailConstants.NULL_STRING) ? MailConstants.EMPTY_STRING
						: ccString);
				model.add(MailConstants.RECEIVED_DATE,
						simpleDateFormat.parseObject(previousMailMessage.getHeader(MailConstants.DATE_STRING)[0].toString())
								.toString());
			} catch (javax.mail.MessagingException mailException) {
				LOGGER.error("Error encountered while extarcting info from previos mail object", mailException);
				throw new DCMAApplicationException("Error encountered while extarcting infor from previos mail object", mailException);
			} catch (java.text.ParseException parseException) {
				LOGGER.error("Error encountered while parsing date extracted from mail header of previos mail object", parseException);
				throw new DCMAApplicationException("Error encountered while parsing received date from previos mail object",
						parseException);
			}
			LOGGER.debug(EphesoftStringUtil.concatenate("Batch Class Name: ", batchClassName, "  UserName: ", userName));
			sendTextMailWithClasspathTemplate(metaData, mailTemplatePath, model, previousMailMessage);
		} else {
			LOGGER.error("Either or all of the following values are null. Error notification mail cann't be sent. \n Subject,BatchClassName, UserName");
		}
	}

}
