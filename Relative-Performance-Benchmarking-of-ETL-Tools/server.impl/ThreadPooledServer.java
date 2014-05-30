package server.impl;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

import requesthandler.RequestHandler;
import requesthandler.impl.MultiThreadRequestHandler;
import requesthandler.impl.ThreadPooledRequestHandler;
import server.CustomServer;
import configuration.ServerConfiguration;
import factory.ThreadPoolFactory;

public class ThreadPooledServer extends CustomServer {
	private final ServerConfiguration conf;
	private ServerSocket serverSocket;
	private RequestHandler requestHandler;
	private ExecutorService threadPool;

	public ThreadPooledServer(ServerConfiguration conf) {
		super();
		this.conf = conf;
		threadPool = ThreadPoolFactory.createThreadPoolInstance(conf);
	}

	@Override
	public void startServer() {
		Socket socket;
		try {
			serverSocket = new ServerSocket(conf.getPort());
			System.out
					.println("*********************Thread Pooled Multi Threaded server started "
							+ conf.getPort() + "**************************");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void serveRequests() {
		Socket socket;
		try {
			while (true) {
				socket = serverSocket.accept();

				reqHandler = createRequestHandler(socket, threadPool);

				Thread thread = new Thread(reqHandler);

				threadPool.submit(thread);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected RequestHandler createRequestHandler(Socket socket, ExecutorService threadPool){
		return new ThreadPooledRequestHandler(businessLogicFactory, socket, threadPool);
	}
	
	@Override
	public void stopServer() {
		// TODO Auto-generated method stub

	}

}
