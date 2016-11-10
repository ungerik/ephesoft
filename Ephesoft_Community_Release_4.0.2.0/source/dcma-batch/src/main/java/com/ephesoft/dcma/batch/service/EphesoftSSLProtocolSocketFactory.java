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

package com.ephesoft.dcma.batch.service;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.SocketFactory;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.HttpClientError;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.SecureProtocolSocketFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * EasySSLProtocolSocketFactory can be used to creats SSL {@link Socket}s that accept self-signed certificates.
 * </p>
 * 
 * <p>
 * Example of using custom protocol socket factory for a specific host:
 * 
 * <pre>
 * Protocol easyhttps = new Protocol(&quot;https&quot;, new EasySSLProtocolSocketFactory(), 443);
 * 
 * URI uri = new URI(&quot;https://localhost/&quot;, true);
 * // use relative url only
 * GetMethod httpget = new GetMethod(uri.getPathQuery());
 * HostConfiguration hc = new HostConfiguration();
 * hc.setHost(uri.getHost(), uri.getPort(), easyhttps);
 * HttpClient client = new HttpClient();
 * client.executeMethod(hc, httpget);
 * </pre>
 * 
 * </p>
 * <p>
 * Example of using custom protocol socket factory per default instead of the standard one:
 * 
 * <pre>
 * Protocol easyhttps = new Protocol(&quot;https&quot;, new EasySSLProtocolSocketFactory(), 443);
 * Protocol.registerProtocol(&quot;https&quot;, easyhttps);
 * 
 * HttpClient client = new HttpClient();
 * GetMethod httpget = new GetMethod(&quot;https://localhost/&quot;);
 * client.executeMethod(httpget);
 * </pre>
 * 
 * </p>
 */
public class EphesoftSSLProtocolSocketFactory implements SecureProtocolSocketFactory {

	/**
	 * LOGGER for this class.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(EphesoftSSLProtocolSocketFactory.class);

	/**
	 * sslcontext {@link SSLContext} The instance of SSLContext.
	 */
	private SSLContext sslcontext = null;

	private static SSLContext createEasySSLContext() {
		try {
			final SSLContext context = SSLContext.getInstance("SSL");
			context.init(new KeyManager[0], new TrustManager[] {new DefaultTrustManager()}, new SecureRandom());
			return context;
		} catch (final Exception exception) {
			LOGGER.error(exception.getMessage(), exception);
			throw new HttpClientError(exception.toString());
		}
	}

	/**
	 * Create a trust manager that does not validate certificate chains.
	 */
	private static class DefaultTrustManager implements X509TrustManager {

		@Override
		public void checkClientTrusted(final X509Certificate[] arg0, final String arg1) throws CertificateException {
			// Do Nothing
		}

		@Override
		public void checkServerTrusted(final X509Certificate[] arg0, final String arg1) throws CertificateException {
			// Do Nothing
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
	}

	private SSLContext getSSLContext() {
		if (this.sslcontext == null) {
			this.sslcontext = createEasySSLContext();
		}
		return this.sslcontext;
	}

	public Socket createSocket(final String host, final int port, final InetAddress clientHost, final int clientPort)
			throws IOException {

		return getSSLContext().getSocketFactory().createSocket(host, port, clientHost, clientPort);
	}

	/**
	 * Attempts to get a new socket connection to the given host within the given time limit.
	 * <p>
	 * To circumvent the limitations of older JREs that do not support connect timeout a controller thread is executed. The controller
	 * thread attempts to create a new socket within the given limit of time. If socket constructor does not return until the timeout
	 * expires, the controller terminates and throws an {@link ConnectTimeoutException}
	 * </p>
	 * 
	 * @param host the host name/IP
	 * @param port the port on the host
	 * @param clientHost the local host name/IP to bind the socket to
	 * @param clientPort the port on the local machine
	 * @param params {@link HttpConnectionParams Http connection parameters}
	 * 
	 * @return Socket a new socket
	 * 
	 * @throws IOException if an I/O error occurs while creating the socket
	 * @throws UnknownHostException if the IP address of the host cannot be determined
	 */
	public Socket createSocket(final String host, final int port, final InetAddress localAddress, final int localPort,
			final HttpConnectionParams params) throws IOException {
		if (params == null) {
			throw new IllegalArgumentException("Parameters may not be null");
		}
		final int timeout = params.getConnectionTimeout();
		final SocketFactory socketfactory = getSSLContext().getSocketFactory();
		if (timeout == 0) {
			return socketfactory.createSocket(host, port, localAddress, localPort);
		} else {
			final Socket socket = socketfactory.createSocket();
			final SocketAddress localaddr = new InetSocketAddress(localAddress, localPort);
			final SocketAddress remoteaddr = new InetSocketAddress(host, port);
			socket.bind(localaddr);
			socket.connect(remoteaddr, timeout);
			return socket;
		}
	}

	/**
	 * @see SecureProtocolSocketFactory#createSocket(java.lang.String,int)
	 */
	public Socket createSocket(final String host, final int port) throws IOException {
		return getSSLContext().getSocketFactory().createSocket(host, port);
	}

	/**
	 * @see SecureProtocolSocketFactory#createSocket(java.net.Socket,java.lang.String,int,boolean)
	 */
	public Socket createSocket(final Socket socket, final String host, final int port, final boolean autoClose) throws IOException {
		return getSSLContext().getSocketFactory().createSocket(socket, host, port, autoClose);
	}

	public boolean equals(final Object obj) {
		return ((obj != null) && obj.getClass().equals(EphesoftSSLProtocolSocketFactory.class));
	}

	public int hashCode() {
		return EphesoftSSLProtocolSocketFactory.class.hashCode();
	}

}
