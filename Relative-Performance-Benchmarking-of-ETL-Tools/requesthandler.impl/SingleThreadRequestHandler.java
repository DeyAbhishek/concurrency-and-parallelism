package requesthandler.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

import requesthandler.RequestHandler;
import businesslogic.BusinessLogic;
import configuration.HandlerConfiguration;
import configuration.Request;
import configuration.Response;
import constants.Delimiters;
import factory.BusinessLogicFactory;

public class SingleThreadRequestHandler extends RequestHandler {
	private Socket socket;

	public SingleThreadRequestHandler(
			BusinessLogicFactory businessLogicFactory, Socket socket) {
		super(businessLogicFactory);
		this.socket = socket;
	}

	public void run() {
		try {
			String requestString = readRequest(socket);
			Request req = new Request(requestString);
			conf = req.getHandlerConfiguration();
			Response resp;
			if(conf.isDataInRequest()){
				resp = processInDataRequest(req);
			}else{
				processRequestWithoutData(req);
				resp = new Response("DONE");
			}
			writeResponse(socket, req.getId(), resp);
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void processRequestWithoutData(Request request) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(request.getInpath()));
		BufferedWriter bw = new BufferedWriter(new FileWriter(request.getOutpath()));
		String line = "";
		
		List<String> businessLogics = conf.getBusinessLogics();
		List<String> businessLogicConfigurationString = conf
				.getBusinessLogicConfigurationStrings();
		List<String> resp = null;
		List<BusinessLogic> businessLogicInstances = null;
		for (int i = 0; i < businessLogics.size(); i++) {
			businessLogicInstances = (List<BusinessLogic>) businessLogicFactory.getBusinessLogicInstance(
					businessLogics.get(i),
					businessLogicConfigurationString.get(i), null, null, null,
					null, null);
		}
		
		BusinessLogic businessLogic = null;
		while((line = br.readLine()) != null){
			for(int i=0;i<businessLogicInstances.size();i++){
				businessLogic = businessLogicInstances.get(i);
				businessLogic.setData(line);
				line = businessLogicInstances.get(i).execute().get(0);
			}
			bw.write(line);
		}
		
		br.close();
		bw.close();
	}
	
}
