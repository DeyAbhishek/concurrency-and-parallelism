package test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class Test implements Runnable {

	@Override
	public void run(){
		try{
			Thread.currentThread().sleep(1000);
			Socket clientSocket = new Socket();
			SocketAddress socketAddress = new InetSocketAddress("127.0.0.1", 8085);
			clientSocket.connect(socketAddress);
		
			DataInputStream input = new DataInputStream(clientSocket.getInputStream());
			PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), false);
			writer.println("SENDING REQUEST");
			writer.flush();
			clientSocket.shutdownOutput();
			
			while(input.available() == 0);
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			String line;
			String res = reader.readLine();
			while ((line = reader.readLine()) != null) {
				res += "/n" + line;
			}
			System.out.println("Response : " + res);
			clientSocket.shutdownInput();
			
			clientSocket.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
