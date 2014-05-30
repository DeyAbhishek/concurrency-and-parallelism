package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import requesthandler.RequestHandler;
import configuration.ServerConfiguration;
import constants.Delimiters;
import factory.BusinessLogicFactory;

public abstract class CustomServer {
	protected BusinessLogicFactory businessLogicFactory;
	protected ServerConfiguration conf = null;
	protected RequestHandler reqHandler;

	public CustomServer(){
		businessLogicFactory = new BusinessLogicFactory();
	}
	
	public void startServer() {

	}

	public void serveRequests() {

	}

	public void stopServer() {

	}

//	protected String readRequest(Socket socket) {
//		String req = null;
//		try {
//			BufferedReader reader = new BufferedReader(new InputStreamReader(
//					socket.getInputStream()));
//			String line;
//			req = reader.readLine();
//			while ((line = reader.readLine()) != null) {
//				req += Delimiters.LINE_DELIMITER + line;
//			}
//			System.out.println("Request : " + req);
//			socket.shutdownInput();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return req;
//	}
//
//	protected void writeResponse(Socket socket, String response) {
//		try {
//			PrintWriter writer = new PrintWriter(socket.getOutputStream(),
//					false);
//			writer.println(response + "");
//			writer.flush();
//			socket.shutdownOutput();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	protected RequestHandler createRequestHandler(){
		return reqHandler;
	}
}
