package configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import constants.Delimiters;

public class Request {
	private String id;
	private HandlerConfiguration handlerConfiguration;
	private List<String> businessLogicConfigurationStrings;
	private String data = "";
	private String inpath = "";
	private String outpath = "";

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public HandlerConfiguration getHandlerConfiguration() {
		return handlerConfiguration;
	}

	public void setHandlerConfiguration(HandlerConfiguration handlerConfiguration) {
		this.handlerConfiguration = handlerConfiguration;
	}
	
	public List<String> getBusinessLogicConfigurationStrings() {
		return businessLogicConfigurationStrings;
	}

	public void setBusinessLogicConfigurationStrings(
			List<String> businessLogicConfigurationStrings) {
		this.businessLogicConfigurationStrings = businessLogicConfigurationStrings;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
	public String getInpath() {
		return inpath;
	}

	public void setInpath(String inpath) {
		this.inpath = inpath;
	}

	public String getOutpath() {
		return outpath;
	}

	public void setOutpath(String outpath) {
		this.outpath = outpath;
	}

	public Request(String requestString) {
		int offset = 2;
		String[] requestParams = requestString.split(Delimiters.LINE_DELIMITER);
		id = requestParams[0];
		String[] handlersParams = requestParams[1]
				.split(Delimiters.FIELD_DELIMITER);
		List<String> businessLogic = Arrays.asList(Arrays.copyOfRange(handlersParams, 1, handlersParams.length));
		List<String> businessLogicConfigurationStrings = new ArrayList<String>();
		int numberOfBusinessLogics = handlersParams.length-1;
		for(int i=0;i<numberOfBusinessLogics;i++)
			businessLogicConfigurationStrings.add(requestParams[i+offset]);
		handlerConfiguration = new HandlerConfiguration(
				Boolean.parseBoolean(handlersParams[0]), businessLogic, businessLogicConfigurationStrings);
		
		int i = numberOfBusinessLogics + offset;
		if (handlerConfiguration.isDataInRequest()) {
			while (i < requestParams.length){
				data += requestParams[i] + Delimiters.LINE_DELIMITER;
				i++;
			}
		}else{
			inpath = requestParams[i];
			outpath = requestParams[i+1];
		}
	}
}
