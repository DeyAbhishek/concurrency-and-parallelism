package factory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import businesslogic.BusinessLogic;
import businesslogic.impl.MimickedBusinessLogic;
import businesslogic.impl.VariableFormatDelimitedParser;
import businesslogic.impl.XMLParser;
import configuration.MimickedBusinessLogicConfiguration;
import configuration.Request;
import configuration.Response;
import configuration.VariableFormatParserConfiguration;
import configuration.XMLParserConfiguration;
import constants.BusinessLogics;

public class BusinessLogicFactory {
	public BusinessLogicFactory() {

	}

	public BusinessLogic getBusinessLogicInstance(String businessLogic,
			String businessLogicConfigurationString, String data, BlockingQueue inQueue, BlockingQueue outQueue, AtomicBoolean isPreviousBusinessLogicDone, AtomicBoolean isCurrentBusinessLogicDone) {
		BusinessLogic businessLogicInstance = null;
		if (businessLogic
				.equalsIgnoreCase(BusinessLogics.BusinessLogicImpl.xmlparsing
						.toString()))
			businessLogicInstance = new XMLParser(new XMLParserConfiguration(
					businessLogicConfigurationString), data, inQueue, outQueue, isPreviousBusinessLogicDone, isCurrentBusinessLogicDone);
		else if (businessLogic
				.equalsIgnoreCase(BusinessLogics.BusinessLogicImpl.variableformatparsing
						.toString()))
			businessLogicInstance = new VariableFormatDelimitedParser(
					new VariableFormatParserConfiguration(
							businessLogicConfigurationString), data, inQueue, outQueue, isPreviousBusinessLogicDone, isCurrentBusinessLogicDone);
		else if (businessLogic
				.equalsIgnoreCase(BusinessLogics.BusinessLogicImpl.mimickedbusinesslogic
						.toString()))
			businessLogicInstance = new MimickedBusinessLogic(new MimickedBusinessLogicConfiguration(businessLogicConfigurationString), data, inQueue, outQueue, isPreviousBusinessLogicDone, isCurrentBusinessLogicDone);
		return businessLogicInstance;
	}
}
