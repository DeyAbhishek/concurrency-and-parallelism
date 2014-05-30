package main;

import java.net.ServerSocket;
import java.net.Socket;

import configuration.ServerConfiguration;
import constants.Delimiters;
import constants.ThreadPools;
import factory.ServerFactory;
import server.CustomServer;
import test.Test;

public class Main {
	
	public static void main(String[] args) {
		
		ServerConfiguration conf = getServerConfiguration(args);
		
		int NPROCS = Runtime.getRuntime().availableProcessors();
		
		ServerFactory serverFactory = new ServerFactory(conf);
		
		CustomServer server = serverFactory.getServerInstance();
		
		server.startServer();
		
		server.serveRequests();
			
	}
	
	private static ServerConfiguration getServerConfiguration(String[] args){
		String[] serverConfParamCSV = args[0].split(Delimiters.FIELD_DELIMITER);
		return new ServerConfiguration(serverConfParamCSV);
	}
	
}
