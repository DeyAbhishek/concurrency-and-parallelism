package server.impl;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import configuration.ServerConfiguration;
import factory.ThreadPoolFactory;
import requesthandler.RequestHandler;
import requesthandler.impl.MultiThreadRequestHandler;
import requesthandler.impl.SingleThreadRequestHandler;
import server.CustomServer;

public class MultiThreadedServer extends CustomServer {
	private final ServerConfiguration conf;
	private ServerSocket serverSocket;
	private RequestHandler requestHandler;
	
	public MultiThreadedServer(ServerConfiguration conf){
		super();
		this.conf = conf;
	}
	
	@Override
	public void startServer() {
		Socket socket;
		try {
			serverSocket = new ServerSocket(conf.getPort());
			System.out.println("*********************Multi Threaded server started " + conf.getPort() + "**************************");
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
				
				reqHandler = createRequestHandler(socket);
				
				Thread thread = new Thread(reqHandler);
				
				thread.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected RequestHandler createRequestHandler(Socket socket){
		return new MultiThreadRequestHandler(businessLogicFactory, socket);
	}
	
	@Override
	public void stopServer() {
		
	}

}
