package businesslogic.impl;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import configuration.DataStore;


public class DataFileWriter implements Runnable {
	private String outPath;
	private BlockingQueue lastBlockingQueue;
	private AtomicBoolean isWorkerDone;
	
	public DataFileWriter(String outPath, BlockingQueue postProcessingData, AtomicBoolean isWorkerDone) {
		this.outPath = outPath;
		this.lastBlockingQueue = postProcessingData;
		this.isWorkerDone = isWorkerDone;
	}

	@Override
	public void run() {
		BufferedWriter bw = null;
		DataStore dataStore;
		try {
			bw = new BufferedWriter(new FileWriter(outPath));
			while (!(isWorkerDone.get() && lastBlockingQueue.isEmpty())) {
				dataStore = (DataStore) lastBlockingQueue.poll(1,
						TimeUnit.NANOSECONDS);
				if (dataStore != null) {
					int size = dataStore.getData().size();
					List<String> data = dataStore.getData();
					for (int i = 0; i < size; i++) {
						// System.out.println(response.output.get(i).split("\t")[0]);
						bw.write(data.get(i));
					}
				}
			}
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
}
