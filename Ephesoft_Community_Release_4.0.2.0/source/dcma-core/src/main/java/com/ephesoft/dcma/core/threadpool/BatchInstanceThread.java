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

package com.ephesoft.dcma.core.threadpool;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.util.EphesoftStringUtil;

/**
 * This class used to add the threads in the thread pool.
 * 
 * @author Ephesoft
 * @version 1.0
 * 
 */
public class BatchInstanceThread {

	/**
	 * Time duration the thread sleeps before checking if all tasks have completed.
	 */
	private final long waitThreadSleepTime;

	/**
	 * String constant for batch instance identifier.
	 */
	private String batchInstanceId;

	/**
	 * List of all tasks associated with a batch instance.
	 */
	private final List<AbstractRunnable> taskList = new LinkedList<AbstractRunnable>();

	/**
	 * Boolean to check whether thread pool is used for executing ghost script commands.
	 */
	private boolean isUsingGhostScript;

	/**
	 * Logger instance for logging using slf4j for logging information.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(BatchInstanceThread.class);

	/**
	 * An object used for synchronization.
	 */
	private Object object = new Object();

	/**
	 * @return the batchInstanceId
	 */
	public String getBatchInstanceId() {
		return batchInstanceId;
	}

	/**
	 * @param batchInstanceId the batchInstanceId to set
	 */
	public void setBatchInstanceId(String batchInstanceId) {
		this.batchInstanceId = batchInstanceId;
	}

	/**
	 * Default construtor for batch instance thread class.
	 */
	public BatchInstanceThread() {
		this(null, Boolean.FALSE, 500);
	}

	/**
	 * Constructor for batch instance thread class.
	 * 
	 * @param batchInstanceId
	 */
	public BatchInstanceThread(String batchInstanceId) {
		this(batchInstanceId, Boolean.FALSE, 500);
	}

	/**
	 * Constructor for batch instance thread class.
	 * 
	 * @param waitTime
	 */
	public BatchInstanceThread(long waitTime) {
		this(null, Boolean.FALSE, waitTime);
	}

	/**
	 * Constructor for batch instance thread class.
	 * 
	 * @param batchInstanceId
	 * @param isUsingGhostScript
	 * @param waitThreadSleepTime
	 */
	public BatchInstanceThread(String batchInstanceId, boolean isUsingGhostScript, long waitThreadSleepTime) {
		this(batchInstanceId, isUsingGhostScript, Boolean.FALSE, waitThreadSleepTime);
	}

	/**
	 * Constructor for batch instance thread class.
	 * 
	 * @param batchInstanceId
	 * @param isUsingGhostScript
	 * @param waitThreadSleepTime
	 * 
	 */
	public BatchInstanceThread(String batchInstanceId, boolean isUsingGhostScript, boolean isUsingHocrEngine, long waitThreadSleepTime) {
		this.batchInstanceId = batchInstanceId;
		this.isUsingGhostScript = isUsingGhostScript;
		this.waitThreadSleepTime = waitThreadSleepTime;
	}

	/**
	 * Constructor for batch instance thread class.
	 * 
	 * @param batchInstanceId
	 * @param isUsingGhostScript
	 * @param waitThreadSleepTime
	 * 
	 */
	public BatchInstanceThread(String batchInstanceId, int licensePoolSize, boolean isUsingNuanceHocrEngine, long waitThreadSleepTime) {
		this.batchInstanceId = batchInstanceId;
		this.isUsingGhostScript = Boolean.FALSE;
		this.waitThreadSleepTime = waitThreadSleepTime;
	}

	/**
	 * @param isUsingGhostScript the isUsingGhostScript to set
	 */
	public void setUsingGhostScript(boolean isUsingGhostScript) {
		this.isUsingGhostScript = isUsingGhostScript;
	}

	/**
	 * This method is used to add a task (command line) that needs execution.
	 * 
	 * @param runnable
	 */
	public void add(AbstractRunnable runnable) {
		synchronized (object) {
			this.taskList.add(runnable);
		}
	}

	/**
	 * This method is used to add list of task (command line) that needs execution.
	 * 
	 * @param runnableList
	 */
	public void addAll(List<AbstractRunnable> runnableList) {
		synchronized (object) {
			this.taskList.addAll(runnableList);
		}
	}

	/**
	 * The method is used to execute the tasks provided. The method returns when all the tasks have stopped execution.
	 * 
	 * @throws DCMAApplicationException
	 */
	public void execute() throws DCMAApplicationException {
		synchronized (object) {
			ThreadPool threadPoolInstance = getThreadPoolInstance();
			if (batchInstanceId != null) {
				threadPoolInstance.putBatchInstanceThreadMap(batchInstanceId, this);
			}
			for (AbstractRunnable runnable : taskList) {
				try {
					if (isUsingGhostScript) {
						threadPoolInstance.addTaskForGhostScript(runnable);
					} else {
						threadPoolInstance.addTask(runnable);
					}
				} catch (RejectedExecutionException e) {
					LOG.error("Cannot add any more tasks. Some tasks for this batch instance may not have been added.");
					runnable.setDcmaApplicationException(new DCMAApplicationException(
							"Cannot add any more tasks. Thread pool has reached maximum size."));
				}
			}
			wait(taskList);
			if (batchInstanceId != null) {
				threadPoolInstance.removeBatchInstanceThreadMap(batchInstanceId);
			}
		}
	}

	/**
	 * API for the getting the thread pool instance object.
	 * 
	 * @return threadPoolInstance
	 */
	public ThreadPool getThreadPoolInstance() {
		ThreadPool threadPoolInstance;
		if (isUsingGhostScript) {
			threadPoolInstance = ThreadPool.getInstanceForGhostScript();
		} else {
			threadPoolInstance = ThreadPool.getInstance();
		}
		return threadPoolInstance;
	}

	/**
	 * API for removing the thread from the threadpool and batch instance thread map.
	 * 
	 */
	public void remove() {
		ThreadPool threadPoolInstance = removeTasksFromPool();
		waitForCompletion(taskList, 0L);
		if (batchInstanceId != null) {
			threadPoolInstance.removeBatchInstanceThreadMap(batchInstanceId);
		}
	}

	private ThreadPool removeTasksFromPool() {
		ThreadPool threadPoolInstance = getThreadPoolInstance();
		for (Iterator<AbstractRunnable> iterator = taskList.iterator(); iterator.hasNext();) {
			AbstractRunnable runnable = iterator.next();
			if (!runnable.isStarted() || runnable.isCompleted()) {
				threadPoolInstance.removeTask(runnable);
				iterator.remove();
			}
		}
		return threadPoolInstance;
	}

	/**
	 * Removes all the resources for a particular batch.
	 * 
	 * @param waitTime time till which system will wait for resources to be removed.
	 */
	public void remove(final long waitTime) {
		final ThreadPool threadPoolInstance = removeTasksFromPool();
		waitForCompletion(taskList, waitTime);
		if (batchInstanceId != null) {
			threadPoolInstance.removeBatchInstanceThreadMap(batchInstanceId);
		}
	}

	/**
	 * Waits for all the threads in the provided list to complete execution.
	 * 
	 * @param threadClassList the list on which to wait.
	 * @throws DCMAApplicationException
	 */
	public void wait(List<AbstractRunnable> taskList) throws DCMAApplicationException {
		boolean completed = false;
		while (!completed) {
			completed = true;
			for (AbstractRunnable th : taskList) {
				completed &= th.isCompleted();
				if (th.isCompleted() && th.getDcmaApplicationException() != null) {
					throw th.getDcmaApplicationException();
				}
			}
			try {
				if (!completed) {
					Thread.sleep(waitThreadSleepTime);
				}
			} catch (InterruptedException e) {
				LOG.error(Thread.currentThread()
						+ " interrupted. Resuming execution. Some tasks of this batch instance may not have completed execution.");
			}
		}
	}

	/**
	 * Waits for all the threads in the provided list to complete execution if the wait time is 0 otherwise wait until the time.
	 * 
	 * @param taskList tasks which needs to be completed.
	 * @param waitTime time till which system can wait.
	 */
	public void waitForCompletion(final List<AbstractRunnable> taskList, final long waitTime) {
		boolean completed = false;

		// #5211: To stop batches stuck at RESTART_IN_PROGRESS state
		long startTime = System.currentTimeMillis();
		while (!completed && (waitTime == 0L || (System.currentTimeMillis() - startTime) < waitTime)) {
			completed = true;
			for (AbstractRunnable th : taskList) {
				completed &= th.isCompleted();
			}
			try {
				if (!completed) {
					Thread.sleep(waitThreadSleepTime);
				}
			} catch (InterruptedException interruptedException) {
				LOG.error(EphesoftStringUtil.concatenate(Thread.currentThread(),
						" interrupted. Resuming execution. Some tasks of this batch instance may not have completed execution."));
			}
		}
	}

	public List<AbstractRunnable> getTaskList() {
		return taskList;
	}

}
