package com.nulp.dss.docxmanaging.container;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.nulp.dss.docxmanaging.container.DataContainerForTableFillerImpl.TestTableIterator;

public class PaymentFormContainer implements DataContainer {
	private static final String CREATING_TABLE_STATEGY_TAG_NAME = "students_table";
	private static final int COLLUMN_NUMBER = 10;
	private Map<String, String> tagsMap;
	private List<ArrayList<String>> table; 

	
	public PaymentFormContainer(Map<String, String> tagsMap,
			List<ArrayList<String>> table) {
		super();
		this.tagsMap = tagsMap;
		this.table = table;
	}

	@Override
	public boolean isTagNameATable(String tagName) {
		if (tagName.equals(CREATING_TABLE_STATEGY_TAG_NAME)){
			return true;
		} else{
			return false;
		}
	}

	/**
	 * @return 
	 * 		empty string - if tag do not exist
	 * 		'-' - if field (in DB) is empty string or null
	 */
	@Override
	public String getTagString(String tagName) {
		if (!tagsMap.containsKey(tagName)){
			return null;
		}
		else {
			if (tagsMap.get(tagName) == null || tagsMap.get(tagName).isEmpty()){
				return "-";
			}
			else {
				return tagsMap.get(tagName);
			}
		}
	}

	@Override
	public Iterator<ArrayList<String>> getTableIterator(String tableTagName) {
		if (tableTagName.equals(CREATING_TABLE_STATEGY_TAG_NAME)){
			return table.iterator();
		}
		else{
			return null;
		}
	}

	@Override
	public int getTableColumnSize(String tableTagName) {
		if (tableTagName.equals(CREATING_TABLE_STATEGY_TAG_NAME)){
			return COLLUMN_NUMBER;
		}
		else{
			return 0;
		}
	}

	@Override
	public String toString() {
		return "PaymentFormContainer [tagsMap=" + tagsMap + ", \ntable=" + table
				+ "]";
	}
}
