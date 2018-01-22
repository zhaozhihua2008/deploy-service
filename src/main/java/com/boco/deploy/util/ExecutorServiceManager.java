/**
 * 
 */
package com.boco.deploy.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description:
 * <p>
 * Change Log:
 * <p>
 * 2016-4-21 create by zhaozhihua
 * <p>
 * 
 * @version 1.0
 * @date 2016-4-21
 * @author zhaozhihua
 */
public class ExecutorServiceManager {
	private static final Logger logger = LoggerFactory.getLogger(ExecutorServiceManager.class);

	public static final String EXECUTOR_DEFAULT = "def";
	
	private final AtomicInteger counter = new AtomicInteger(0);

	private Map<String, ExecutorService> serviceMap = new HashMap<String, ExecutorService>();

	private static ExecutorServiceManager manager = new ExecutorServiceManager();

	private ExecutorServiceManager() {
	};

	public static ExecutorServiceManager getInstance() {
		return manager;
	}

	/**
	 * @return default executor
	 */
	public ExecutorService getExecutor() {
		return getExecutor(EXECUTOR_DEFAULT);
	}

	/**
	 * get executor by name
	 * 
	 * @param name
	 * @return
	 */
	public ExecutorService getExecutor(String name) {
		ExecutorService service = serviceMap.get(name);
		if (service == null) {
			service = create(name);
		}
		return service;
	}

	public void printStat() {
		for (Entry<String, ExecutorService> entry : serviceMap.entrySet()) {
			System.out.println("disexecutor " + entry.getKey() + "==" + entry.getValue());
		}
	}

	private synchronized ExecutorService create(final String name) {
		ExecutorService service = serviceMap.get(name);
		if (service == null) {
			int coreThreads = 8;
			int maxThreads = 16;
			int keepAliveTime = 0;
			BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>(10000);
			ThreadFactory tf = new ThreadFactory() {
				@Override
				public Thread newThread(Runnable r) {
					String threadName = name + "-thread-" + counter.getAndIncrement();
					Thread th = new Thread(r, threadName);
					th.setDaemon(true);
					return th;
				}
			};
			service = new ThreadPoolExecutor(coreThreads, maxThreads, keepAliveTime,
					TimeUnit.MILLISECONDS, queue, tf, new ThreadPoolExecutor.CallerRunsPolicy());
			serviceMap.put(name, service);
		}
		return service;
	}

}
