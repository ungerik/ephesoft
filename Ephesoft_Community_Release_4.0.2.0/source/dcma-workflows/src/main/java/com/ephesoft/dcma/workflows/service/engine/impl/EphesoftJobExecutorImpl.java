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

package com.ephesoft.dcma.workflows.service.engine.impl;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.activiti.engine.impl.jobexecutor.ExecuteJobsRunnable;
import org.activiti.engine.impl.jobexecutor.JobExecutor;

import com.ephesoft.dcma.batch.service.EphesoftContext;
import com.ephesoft.dcma.util.logger.EphesoftLogger;
import com.ephesoft.dcma.util.logger.EphesoftLoggerFactory;
import com.ephesoft.dcma.workflows.constant.WorkflowConstants;
import com.ephesoft.dcma.workflows.service.engine.EphesoftJobExecutor;

/**
 * This class represents a job executor impelmenting Ephesoft's job executor interface and overriding third party's Default job
 * Executor.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> Oct 21, 2014 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
public class EphesoftJobExecutorImpl extends JobExecutor implements EphesoftJobExecutor {

	/**
	 * int Queue size of the thread pool of job executors.
	 */
	protected int queueSize = 1;

	/**
	 * int Core pool size of the thread pool of job executors.
	 */
	protected int corePoolSize = 1;

	/**
	 * {@link BlockingQueue}<{@link Runnable}> Queue of thread pool.
	 */
	protected BlockingQueue<Runnable> threadPoolQueue;

	/**
	 * {@link ThreadPoolExecutor} Thread pool executor.
	 */
	protected ThreadPoolExecutor threadPoolExecutor;

	/**
	 * {@link EphesoftLogger} Instance of Logger.
	 */
	private static final EphesoftLogger LOGGER = EphesoftLoggerFactory.getLogger(EphesoftJobExecutorImpl.class);
	private int maxPoolSize = 10;

	/**
	 * Default constructor of the class, setting name for the job executor and resetting default job lock time.
	 */
	public EphesoftJobExecutorImpl() {
		String serverContextPath = EphesoftContext.getHostServerContextPath();
		setName(serverContextPath);
		this.lockTimeInMillis = WorkflowConstants.FACTOR_FOR_JOB_LOCK_TIME * 5 * 60 * 1000;
	}

	/**
	 * Starts executing jobs.
	 */
	protected void startExecutingJobs() {
		acquireJobsCmd = new EphesoftAcquireJobsCmdImpl(this);
		setMaxPoolSize(WorkflowConstants.FACTOR_FOR_MAX_POOL_SIZE * corePoolSize);
		if (threadPoolQueue == null) {
			threadPoolQueue = new ArrayBlockingQueue<Runnable>(queueSize);
		}
		if (threadPoolExecutor == null) {
			threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maxPoolSize, 0L, TimeUnit.MILLISECONDS, threadPoolQueue);
			threadPoolExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
		}
		startJobAcquisitionThread();
	}

	/**
	 * Stops executing jobs.
	 */
	protected void stopExecutingJobs() {
		stopJobAcquisitionThread();

		// Ask the thread pool to finish and exit
		threadPoolExecutor.shutdown();

		// Waits for 1 minute to finish all currently executing jobs
		try {
			if (!threadPoolExecutor.awaitTermination(60L, TimeUnit.SECONDS)) {
				LOGGER.warn("Timeout during shutdown of job executor. The current running jobs could not end within 60 seconds after shutdown operation.");
			}
		} catch (InterruptedException e) {
			LOGGER.warn("Interrupted", getName(), " while shutting down the job executor. ", e);
		}

		threadPoolExecutor = null;
	}

	@Override
	public void executeJobs(final List<String> jobIds) {
		LOGGER.info("No of jobs is: ", jobIds.size());
		try {
			threadPoolExecutor.execute(new ExecuteJobsRunnable(this, jobIds));
		} catch (RejectedExecutionException e) {
			rejectedJobsHandler.jobsRejected(this, jobIds);
		}
	}

	@Override
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * Gets size of the of queue of the thread pool executor.
	 * 
	 * @return int
	 */
	public int getQueueSize() {
		return queueSize;
	}

	@Override
	public void setQueueSize(final int queueSize) {
		if (queueSize > this.queueSize) {
			this.queueSize = queueSize;
		}
	}

	/**
	 * Gets core pool size of the thread pool executor.
	 * 
	 * @return int
	 */
	public int getCorePoolSize() {
		return corePoolSize;
	}

	@Override
	public void setCorePoolSize(final int corePoolSize) {
		if (corePoolSize > this.corePoolSize) {
			this.corePoolSize = corePoolSize;
		}
	}

	/**
	 * Gets maximum pool size of the thread pool executor.
	 * 
	 * @return int
	 */
	public int getMaxPoolSize() {
		return maxPoolSize;
	}

	@Override
	public void setMaxPoolSize(final int maxPoolSize) {
		if (maxPoolSize > this.maxPoolSize) {
			this.maxPoolSize = maxPoolSize;
		}
	}

	/**
	 * Gets thread pool queue.
	 * 
	 * @return {@link BlockingQueue}<{@link Runnable}>
	 */
	public BlockingQueue<Runnable> getThreadPoolQueue() {
		return threadPoolQueue;
	}

	/**
	 * Sets thread pool queue.
	 * 
	 * @param threadPoolQueue {@link BlockingQueue}<{@link Runnable}>
	 */
	public void setThreadPoolQueue(final BlockingQueue<Runnable> threadPoolQueue) {
		this.threadPoolQueue = threadPoolQueue;
	}

	/**
	 * Gets thread pool executor.
	 * 
	 * @return {@link ThreadPoolExecutor}
	 */
	public ThreadPoolExecutor getThreadPoolExecutor() {
		return threadPoolExecutor;
	}

	/**
	 * Sets thread pool executor.
	 * 
	 * @param threadPoolExecutor {@link ThreadPoolExecutor}
	 */
	public void setThreadPoolExecutor(final ThreadPoolExecutor threadPoolExecutor) {
		this.threadPoolExecutor = threadPoolExecutor;
	}
}
