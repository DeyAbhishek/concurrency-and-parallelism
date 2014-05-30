package businesslogic;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;

import businesslogic.BusinessLogic;
import configuration.Request;
import configuration.Response;
import configuration.XMLParserConfiguration;

public class XMLParser extends BusinessLogic {
	private XMLParserConfiguration configuration;
	private String data;
	private BlockingQueue inQueue;
	private BlockingQueue outQueue;
	private AtomicBoolean isPreviousBusinessLogicDone;
	private AtomicBoolean isCurrentBusinessLogicDone;

	public XMLParser(XMLParserConfiguration configuration, String data, BlockingQueue inQueue, BlockingQueue outQueue, AtomicBoolean isPreviousBusinessLogicDone, AtomicBoolean isCurrentBusinessLogicDone) {
		super(data);
		this.configuration = configuration;
		this.inQueue = inQueue;
		this.outQueue = outQueue;
		this.isPreviousBusinessLogicDone = isPreviousBusinessLogicDone;
		this.isCurrentBusinessLogicDone = isCurrentBusinessLogicDone;
	}

	@Override
	public void run() {
		execute();
	}
	
	@Override
	public List<String> execute() {
		try{
			// Create a "parser factory" for creating SAX parsers
			SAXParserFactory spfac = SAXParserFactory.newInstance();

			// Now use the parser factory to create a SAXParser object
			SAXParser sp = spfac.newSAXParser();

			// Create an instance of this class; it defines all the handler methods
			XMLParserHandler handler = new XMLParserHandler();

			// Finally, tell the parser to parse the input and notify the handler
			// sp.parse("bank.xml", handler);
			sp.parse(new InputSource(new StringReader(data)), handler);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return new ArrayList<String>();
	}
	
	public String execute(String input) {
		this.data = input;
		List<String> output = execute();
		return output.get(0);
	}

}
