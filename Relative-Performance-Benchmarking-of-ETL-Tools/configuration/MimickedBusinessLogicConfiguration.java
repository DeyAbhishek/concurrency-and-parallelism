package configuration;

public class MimickedBusinessLogicConfiguration {
	private int iterations;
	
	public int getIterations() {
		return iterations;
	}

	public void setIterations(int iterations) {
		this.iterations = iterations;
	}

	public MimickedBusinessLogicConfiguration(String configurationString){
		this.iterations = Integer.parseInt(configurationString);
	}	
}
