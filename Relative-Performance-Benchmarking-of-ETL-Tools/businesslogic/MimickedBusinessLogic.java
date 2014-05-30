package businesslogic;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import configuration.DataStore;
import configuration.MimickedBusinessLogicConfiguration;
import constants.Delimiters;
import businesslogic.BusinessLogic;

public class MimickedBusinessLogic extends BusinessLogic {
	private MimickedBusinessLogicConfiguration conf;
	private BlockingQueue inQueue;
	private BlockingQueue outQueue;
	private AtomicBoolean isPreviousBusinessLogicDone;
	private AtomicBoolean isCurrentBusinessLogicDone;
	
	public MimickedBusinessLogic(MimickedBusinessLogicConfiguration conf, String data, BlockingQueue inQueue, BlockingQueue outQueue, AtomicBoolean isPreviousBusinessLogicDone, AtomicBoolean isCurrentBusinessLogicDone){
		super(data);
		this.conf = conf;
		this.inQueue = inQueue;
		this.outQueue = outQueue;
		this.isPreviousBusinessLogicDone = isPreviousBusinessLogicDone;
		this.isCurrentBusinessLogicDone = isCurrentBusinessLogicDone;
	}
	
	@Override
	public void run() {
		DataStore dataStore;
		try {
			while (!(isPreviousBusinessLogicDone.get() && inQueue.isEmpty())) {
				dataStore = (DataStore) inQueue.poll(1, TimeUnit.NANOSECONDS);
				if (dataStore != null) {
					List<String> input = dataStore.getData();
					this.data = input.get(0);
					for (int i = 1; i < input.size(); i++) {
						this.data += Delimiters.LINE_DELIMITER + input.get(i);
					}
					
					List<String> output = execute();
					
					dataStore.setData(output);
					boolean result = false;
					while(!result){
						result = outQueue.offer(dataStore, 1, TimeUnit.NANOSECONDS);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<String> execute() {
		List<String> records = Arrays.asList(data.split(Delimiters.LINE_DELIMITER));
		
		//Mimicked Business Logic.
		Random random = new Random();
		for(int i=0;i<conf.getIterations();i++)
			random.nextInt();
		
		return records;
	}

}
