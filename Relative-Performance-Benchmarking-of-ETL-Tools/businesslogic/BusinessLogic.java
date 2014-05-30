package businesslogic;

import java.util.List;

public abstract class BusinessLogic implements Runnable {
	protected String data;
	
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public abstract List<String> execute();
	
	public BusinessLogic(String data){
		this.data = data;
	}
}
