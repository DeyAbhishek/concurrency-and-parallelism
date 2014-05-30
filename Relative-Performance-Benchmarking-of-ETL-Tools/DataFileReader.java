package businesslogic.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import configuration.DataStore;
import constants.ReadWrite;


public class DataFileReader implements Runnable {
	private String inPath;
	private BlockingQueue firstBlockingQueue;
	private AtomicBoolean isReaderDone;

	public DataFileReader(String inPath, BlockingQueue preProcessingData, AtomicBoolean isReaderDone) {
		this.inPath = inPath;
		this.firstBlockingQueue = preProcessingData;
		this.isReaderDone = isReaderDone;
	}

	@Override
	public void run() {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(inPath));
			String line;
			int i = 0;
			DataStore dataStore = new DataStore();
			while ((line = br.readLine()) != null) {
				if (i < ReadWrite.DATA_QUANTUM) {
					dataStore.add(line);
					i++;
				} else {
					firstBlockingQueue.put(dataStore);
					i = 0;
					dataStore = new DataStore();
					dataStore.add(line);
					i++;
				}
			}
			br.close();
			isReaderDone.set(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
