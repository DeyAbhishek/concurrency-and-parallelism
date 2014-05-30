package businesslogic.impl;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import configuration.DataStore;
import configuration.Response;
import configuration.VariableFormatParserConfiguration;
import constants.Delimiters;
import businesslogic.BusinessLogic;

public class VariableFormatDelimitedParser extends BusinessLogic {
	private VariableFormatParserConfiguration configuration;
	private BlockingQueue inQueue;
	private BlockingQueue outQueue;
	private AtomicBoolean isPreviousBusinessLogicDone;
	private AtomicBoolean isCurrentBusinessLogicDone;
	
	public VariableFormatDelimitedParser(VariableFormatParserConfiguration configuration, String data, BlockingQueue inQueue, BlockingQueue outQueue, AtomicBoolean isPreviousBusinessLogicDone, AtomicBoolean isCurrentBusinessLogicDone){
		super(data);
		this.configuration = configuration;
		this.inQueue = inQueue;
		this.outQueue = outQueue;
		this.isPreviousBusinessLogicDone = isPreviousBusinessLogicDone;
		this.isCurrentBusinessLogicDone = isCurrentBusinessLogicDone;
	}
	

	public void setData(String data) {
		this.data = data;
	}

	@Override
	public void run() {
		DataStore dataStore;
		try {
			
			while (!(isPreviousBusinessLogicDone.get() && inQueue.isEmpty())) {
				dataStore = (DataStore) inQueue.poll(1, TimeUnit.NANOSECONDS);
				if (dataStore != null) {
					int size = dataStore.getData().size();
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
		List<String> records = Arrays.asList(data.split(configuration.getRecordSeperator()));
		List<String> output = new ArrayList<String>();
		for(String record : records){
			List<String> fields = Arrays.asList(record.split(configuration.getFieldSeperator()));
			if(configuration.getIndexedFieldsRequired().length != 0){
				if(configuration.getIndexedFieldsRequired()[0] == 0){
					output.addAll(fields);
				}else{
					for(int index : configuration.getIndexedFieldsRequired())
						output.add(fields.get(index-1));
				}
			}
		}
		return output;
	}

}
