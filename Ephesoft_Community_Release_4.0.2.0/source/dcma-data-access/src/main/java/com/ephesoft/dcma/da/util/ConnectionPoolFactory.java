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

package com.ephesoft.dcma.da.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.xalan.lib.sql.ConnectionPool;
import org.apache.xalan.lib.sql.DefaultConnectionPool;

import com.ephesoft.dcma.core.common.ConnectionType;
import com.ephesoft.dcma.da.domain.Connections;
import com.ephesoft.dcma.util.EphesoftStringUtil;

public class ConnectionPoolFactory {

	private static Map<String, ConnectionPool> connectionPoolMap;

	private static Map<Connection, ConnectionPool> poolForConnectionMap;

	private static Map<ConnectionPool, Integer> connectionPoolHitsCount;

	private static int MIN_CONNECTION_PER_POOL = 3;
	private static int THRESHOLD_HIT_COUNT = MIN_CONNECTION_PER_POOL * 2;

	private static Object lockedObject = new Object();

	static {
		connectionPoolMap = new ConcurrentHashMap<String, ConnectionPool>();
		poolForConnectionMap = new ConcurrentHashMap<Connection, ConnectionPool>();
		connectionPoolHitsCount = new ConcurrentHashMap<ConnectionPool, Integer>();
	}

	public static Connection getConnection(Connections connectionModel) throws SQLException {
		Connection connection = null;
		ConnectionPool activePool = null;
		synchronized (lockedObject) {
			if (null != connectionModel) {
				String connectionName = connectionModel.getConnectionName();
				if (!EphesoftStringUtil.isNullOrEmpty(connectionName)) {
					ConnectionPool pool = connectionPoolMap.get(connectionName);
					if (null != pool) {
						pool.setPoolEnabled(true);
						connection = pool.getConnection();
						activePool = pool;
					} else {
						ConnectionPool newPool = getPool(connectionModel, MIN_CONNECTION_PER_POOL);
						if (null != newPool) {
							connectionPoolMap.put(connectionName, newPool);
							connection = newPool.getConnection();
							activePool = newPool;
							connectionPoolHitsCount.put(newPool, 0);
						}
					}
				}
			}
			if (null != activePool && null != connection) {
				poolForConnectionMap.put(connection, activePool);
				Integer count = connectionPoolHitsCount.get(activePool);
				count = ((count) % THRESHOLD_HIT_COUNT) + 1;
				connectionPoolHitsCount.put(activePool, count);
			}
		}
		return connection;
	}

	private static synchronized ConnectionPool getPool(Connections connection, int totalConnection) {
		ConnectionPool pool = null;
		if (null != connection) {
			pool = new DefaultConnectionPool();
			String driverName = ConnectionType.getDriver(connection.getDatabaseType());
			String url = connection.getConnectionURL();
			String userName = connection.getUserName();
			String password = connection.getPassword();
			pool.setDriver(driverName);
			pool.setPassword(password);
			pool.setUser(userName);
			pool.setPoolEnabled(true);
			pool.setURL(url);
			pool.setMinConnections(totalConnection);
		}
		return pool;
	}

	public static boolean testConnection(Connections connection) {
		boolean isSuccessful = false;
		ConnectionPool pool = getPool(connection, 1);
		if (null != pool) {
			isSuccessful = pool.testConnection();
		}
		return isSuccessful;
	}

	public static synchronized void releaseConnection(final Connection connection) throws SQLException {
		synchronized (lockedObject) {
			if (null != connection) {
				ConnectionPool pool = poolForConnectionMap.get(connection);
				if (null != pool) {
					pool.releaseConnection(connection);
					freePool(connection, pool);
				}
			}
		}
	}

	private static void freePool(final Connection connection, ConnectionPool pool) {
		poolForConnectionMap.remove(connection);
		long count = connectionPoolHitsCount.get(pool);
		if (count >= THRESHOLD_HIT_COUNT) {
			pool.freeUnused();
			connectionPoolHitsCount.put(pool, 0);
		}
	}

	public static void releaseConnectionOnError(final Connection connection) throws SQLException {
		synchronized (lockedObject) {
			if (null != connection) {
				ConnectionPool pool = poolForConnectionMap.get(connection);
				if (null != pool) {
					pool.releaseConnectionOnError(connection);
					freePool(connection, pool);
				}
			}
		}
	}

}
