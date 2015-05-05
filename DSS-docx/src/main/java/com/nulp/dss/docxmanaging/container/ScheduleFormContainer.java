package com.nulp.dss.docxmanaging.container;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ScheduleFormContainer implements DataContainer {
	private static final String CREATING_TABLE_TAG_NAME = "schedule_table";
	private static final int COLLUMN_NUMBER = 5;
	private Map<String, String> tagsMap;
	private List<ArrayList<String>> table; 

	
	public ScheduleFormContainer(Map<String, String> tagsMap,
			List<ArrayList<String>> table) {
		super();
		this.tagsMap = tagsMap;
		this.table = table;
	}

	@Override
	public boolean isTagNameATable(String tagName) {
		if (tagName.equals(CREATING_TABLE_TAG_NAME)){
			return true;
		} else{
			return false;
		}
	}

	/**
	 * @return 
	 */
	@Override
	public String getTagString(String tagName) {
		return tagsMap.get(tagName);
	}

	@Override
	public Iterator<ArrayList<String>> getTableIterator(String tableTagName) {
		if (tableTagName.equals(CREATING_TABLE_TAG_NAME)){
			return table.iterator();
		}
		else{
			return null;
		}
	}

	@Override
	public int getTableColumnSize(String tableTagName) {
		if (tableTagName.equals(CREATING_TABLE_TAG_NAME)){
			return COLLUMN_NUMBER;
		}
		else{
			return 0;
		}
	}
}
