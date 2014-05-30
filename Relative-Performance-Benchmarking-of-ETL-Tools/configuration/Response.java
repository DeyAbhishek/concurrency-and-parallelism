package configuration;

import java.util.ArrayList;
import java.util.List;

public class Response {
	private List<String> response;
	
	public Response(){
		response = new ArrayList<String>();
	}
	
	public Response(String output){
		response = new ArrayList<String>();
		response.add(output);
	}

	public List<String> getResponse() {
		return response;
	}

	public void setResponse(List<String> response) {
		this.response = response;
	}

}
