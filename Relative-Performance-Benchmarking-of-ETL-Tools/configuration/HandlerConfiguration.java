package configuration;

import java.util.Arrays;
import java.util.List;

public class HandlerConfiguration {
	private boolean dataInRequest;
	private List<String> businessLogics;
	private List<String> businessLogicConfigurationStrings;
	
	public boolean isDataInRequest() {
		return dataInRequest;
	}
	
	public void setDataInRequest(boolean dataInRequest) {
		this.dataInRequest = dataInRequest;
	}
	
	public List<String> getBusinessLogics() {
		return businessLogics;
	}
	
	public void setBusinessLogics(List<String> businessLogics) {
		this.businessLogics = businessLogics;
	}
	
	public List<String> getBusinessLogicConfigurationStrings() {
		return businessLogicConfigurationStrings;
	}

	public void setBusinessLogicConfigurationStrings(
			List<String> businessLogicConfigurationStrings) {
		this.businessLogicConfigurationStrings = businessLogicConfigurationStrings;
	}

	public HandlerConfiguration(boolean dataInRequest, List<String> businessLogics, List<String> businessLogicConfigurationStrings){
		this.dataInRequest = dataInRequest;
		this.businessLogics = businessLogics;
		this.businessLogicConfigurationStrings = businessLogicConfigurationStrings;
	}
}
