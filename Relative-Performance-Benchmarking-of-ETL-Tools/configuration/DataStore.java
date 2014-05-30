package configuration;

import java.util.ArrayList;
import java.util.List;

public class DataStore {
	private List<String> data;
	
	public List<String> getData() {
		return data;
	}

	public void setData(List<String> data) {
		this.data = data;
	}

	public DataStore(){
		this.data = new ArrayList<String>();
	}
	
	public void add(String line){
		this.data.add(line);
	}
}
