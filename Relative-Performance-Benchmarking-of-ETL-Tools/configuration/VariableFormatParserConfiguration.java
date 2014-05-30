package configuration;

import constants.Delimiters;

public class VariableFormatParserConfiguration {
	private String recordSeperator = Delimiters.LINE_DELIMITER;
	private String fieldSeperator;
	private int[] indexedFieldsRequired;
	
	public VariableFormatParserConfiguration(String configurationString){
		String[] params = configurationString.split(Delimiters.FIELD_DELIMITER);
		this.fieldSeperator = params[0];
		indexedFieldsRequired = new int[params.length-1];
		for(int i=1;i<params.length;i++)
			indexedFieldsRequired[i-1] = Integer.parseInt(params[i]);
	}

	public String getRecordSeperator() {
		return recordSeperator;
	}

	public void setRecordSeperator(String recordSeperator) {
		this.recordSeperator = recordSeperator;
	}

	public String getFieldSeperator() {
		return fieldSeperator;
	}

	public void setFieldSeperator(String fieldSeperator) {
		this.fieldSeperator = fieldSeperator;
	}

	public int[] getIndexedFieldsRequired() {
		return indexedFieldsRequired;
	}

	public void setIndexedFieldsRequired(int[] indexedFieldsRequired) {
		this.indexedFieldsRequired = indexedFieldsRequired;
	}
}
