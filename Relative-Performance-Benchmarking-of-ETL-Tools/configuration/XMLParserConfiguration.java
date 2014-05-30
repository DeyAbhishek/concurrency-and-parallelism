package configuration;

import java.lang.reflect.Array;
import java.util.ArrayList;

import constants.Delimiters;

public class XMLParserConfiguration {
	private String startTag;
	private String endTag;
	private String[] outputValues;
	
	public String getStartTag() {
		return startTag;
	}

	public void setStartTag(String startTag) {
		this.startTag = startTag;
	}

	public String getEndTag() {
		return endTag;
	}

	public void setEndTag(String endTag) {
		this.endTag = endTag;
	}

	public String[] getOutputValues() {
		return outputValues;
	}

	public void setOutputValues(String[] outputValues) {
		this.outputValues = outputValues;
	}

	public XMLParserConfiguration(String configurationString){
		String[] params = configurationString.split(Delimiters.FIELD_DELIMITER);
		startTag = params[0];
		endTag = params[1];
		System.arraycopy(params, 2, outputValues, 0, params.length-2);
	}
}
