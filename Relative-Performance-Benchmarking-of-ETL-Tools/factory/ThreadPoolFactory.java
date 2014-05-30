package factory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import configuration.ServerConfiguration;
import constants.ThreadPools;

public class ThreadPoolFactory {
	
	public static ExecutorService createThreadPoolInstance(ServerConfiguration conf){
		ExecutorService threadPool = null;
		if(conf.getThreadPoolImpl().equalsIgnoreCase(ThreadPools.ThreadPoolImpl.singleThreadPool.toString())){
			threadPool = Executors.newSingleThreadExecutor();
		}else if(conf.getThreadPoolImpl().equalsIgnoreCase(ThreadPools.ThreadPoolImpl.fixedThreadPool.toString())){
			threadPool = Executors.newFixedThreadPool(conf.getPoolSize());
		}else if(conf.getThreadPoolImpl().equalsIgnoreCase(ThreadPools.ThreadPoolImpl.cachedThreadPool.toString())){
			threadPool = Executors.newCachedThreadPool();
		}else if(conf.getThreadPoolImpl().equalsIgnoreCase(ThreadPools.ThreadPoolImpl.scheduledThreadPool.toString())){
			threadPool = Executors.newScheduledThreadPool(conf.getPoolSize());
		}
		return threadPool;
	}
}
