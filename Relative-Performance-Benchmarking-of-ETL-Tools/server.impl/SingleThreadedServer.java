package server.impl;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import requesthandler.RequestHandler;
import requesthandler.impl.SingleThreadRequestHandler;
import server.CustomServer;
import configuration.ServerConfiguration;

public class SingleThreadedServer extends CustomServer {
	private final ServerConfiguration conf;
	private ServerSocket serverSocket;

	public SingleThreadedServer(ServerConfiguration conf) {
		super();
		this.conf = conf;
	}

	@Override
	public void startServer() {
		try {
			serverSocket = new ServerSocket(conf.getPort());
			System.out
					.println("*********************Single Threaded server started at port " + conf.getPort() + "**************************");
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

				reqHandler.run();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected RequestHandler createRequestHandler(Socket socket) {
		return new SingleThreadRequestHandler(businessLogicFactory, socket);
	}

	@Override
	public void stopServer() {

	}
}
