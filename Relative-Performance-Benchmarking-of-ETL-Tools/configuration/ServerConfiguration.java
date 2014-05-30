package configuration;

import constants.ThreadPools;

public class ServerConfiguration {
	private int port = -1;
	private boolean threadedServer = false;
	private boolean threadPooledServer = false;
	private String threadPoolImpl = null;// OPTIONAL PARAMETER
	private int poolSize;// OPTIONAL PARAMETER

	public int getPort() {
		return port;
	}

	public int getPoolSize() {
		return poolSize;
	}

	public void setPoolSize(int poolSize) {
		this.poolSize = poolSize;
	}

	public boolean isThreadedServer() {
		return threadedServer;
	}

	public boolean isThreadPooledServer() {
		return threadPooledServer;
	}

	public String getThreadPoolImpl() {
		return threadPoolImpl;
	}

	public ServerConfiguration(String[] serverConfParamCSV) {
		this.port = Integer.parseInt(serverConfParamCSV[0]);
		this.threadedServer = Boolean.parseBoolean(serverConfParamCSV[1]);
		this.threadPooledServer = Boolean.parseBoolean(serverConfParamCSV[2]);
		if (this.threadPooledServer) {
			this.threadPoolImpl = serverConfParamCSV[3];
		}
		if (this.threadPoolImpl != null && (this.threadPoolImpl
				.equalsIgnoreCase(ThreadPools.ThreadPoolImpl.fixedThreadPool
						.toString())
				|| this.threadPoolImpl
						.equalsIgnoreCase(ThreadPools.ThreadPoolImpl.scheduledThreadPool
								.toString()))) {
			this.poolSize = Integer.parseInt(serverConfParamCSV[4]);
		}
	}
}
