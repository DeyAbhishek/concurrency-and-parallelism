package requesthandler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

import configuration.HandlerConfiguration;
import configuration.Request;
import configuration.Response;
import constants.Delimiters;
import factory.BusinessLogicFactory;

public abstract class RequestHandler implements Runnable {
	protected BusinessLogicFactory businessLogicFactory;
	protected HandlerConfiguration conf;

	public RequestHandler(BusinessLogicFactory businessLogicFactory){
		this.businessLogicFactory = businessLogicFactory;
	}
	
	protected Response processRequest(Request request) {
		return null;
	}
	
	protected Response processInDataRequest(Request request) {
		Response response = new Response();
		List<String> businessLogics = conf.getBusinessLogics();
		List<String> businessLogicConfigurationString = conf
				.getBusinessLogicConfigurationStrings();
		String data = request.getData();
		List<String> resp = null;
		for (int i = 0; i < businessLogics.size(); i++) {
			resp = businessLogicFactory.getBusinessLogicInstance(
					businessLogics.get(i),
					businessLogicConfigurationString.get(i), data, null, null,
					null, null).execute();
			data = resp.get(0);
			for (int j = 1; j < resp.size(); j++)
				data += resp.get(i) + Delimiters.LINE_DELIMITER;
		}
		response.setResponse(resp);
		return response;
	}

	protected String readRequest(Socket socket) {
		String req = null;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			String line;
			req = reader.readLine();
			while ((line = reader.readLine()) != null) {
				req += Delimiters.LINE_DELIMITER + line;
			}
			System.out.println("Request : " + req);
			socket.shutdownInput();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return req;
	}

	protected void writeResponse(Socket socket, String id, Response response) {
		try {
			PrintWriter writer = new PrintWriter(socket.getOutputStream(),
					false);
			writer.println(id + Delimiters.LINE_DELIMITER + response.getResponse() + "");
			writer.flush();
			socket.shutdownOutput();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
