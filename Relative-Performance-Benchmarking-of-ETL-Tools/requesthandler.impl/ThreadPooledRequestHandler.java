package requesthandler.impl;

import java.net.Socket;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

import businesslogic.BusinessLogic;
import businesslogic.impl.DataFileReader;
import businesslogic.impl.DataFileWriter;
import configuration.HandlerConfiguration;
import configuration.Request;
import configuration.Response;
import constants.ReadWrite;
import factory.BusinessLogicFactory;
import requesthandler.RequestHandler;

public class ThreadPooledRequestHandler extends RequestHandler {
	private Socket socket;
	private ExecutorService threadPool;
	
	public ThreadPooledRequestHandler(BusinessLogicFactory businessLogicFactory, Socket socket, ExecutorService threadPool){
		super(businessLogicFactory);
		this.socket = socket;
		this.threadPool = threadPool;
	}
	
	@Override
	public void run() {
		try {
			String requestString = readRequest(socket);
			Request req = new Request(requestString);
			conf = req.getHandlerConfiguration();
			Response resp;
			if(conf.isDataInRequest()){
				resp = processInDataRequest(req);
			}else{
				processRequestWithoutData(req);
				resp = new Response("DONE");
			}
			writeResponse(socket, req.getId(), resp);
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void  processRequestWithoutData(Request request){
		BlockingQueue outQueue = new ArrayBlockingQueue(ReadWrite.QUEUE_SIZE);
		AtomicBoolean isCurrentDone = new AtomicBoolean(false);

		DataFileReader fileReader = new DataFileReader(request.getInpath(),
				outQueue, isCurrentDone);
		Thread readerThread = new Thread(fileReader);
		threadPool.submit(readerThread);

		List<String> businessLogics = conf.getBusinessLogics();
		List<String> businessLogicConfigurationString = conf
				.getBusinessLogicConfigurationStrings();
		Thread businessLogicThread;
		for (int i = 0; i < businessLogics.size(); i++) {
			BlockingQueue inQueue = outQueue;
			outQueue = new ArrayBlockingQueue(ReadWrite.QUEUE_SIZE);
			AtomicBoolean isPreviousDone = isCurrentDone;
			isCurrentDone = new AtomicBoolean(false);
			BusinessLogic businessLogicInstance = businessLogicFactory
					.getBusinessLogicInstance(businessLogics.get(i),
							businessLogicConfigurationString.get(i), null, inQueue, outQueue,
							isPreviousDone, isCurrentDone);
			businessLogicThread = new Thread(businessLogicInstance);
			threadPool.submit(businessLogicThread);
		}
		
		DataFileWriter fileWriter = new DataFileWriter(request.getOutpath(),
				outQueue, isCurrentDone);
		fileReader.run();
	}
}
