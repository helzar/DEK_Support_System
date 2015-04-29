package com.nulp.dss.docxmanaging.datacontainers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class DataContainerTestImpl implements DataContainer {
	private static final String CREATING_TABLE_STATEGY_TAG_NAME = "creating_tabl_starategy";
	private static final int MAX_ROWS = 5;
	private static final int COLLUMN_NUMBER = 5;
	
	@Override
	public boolean isTagNameATable(String tagName) {
		if (tagName.equals(CREATING_TABLE_STATEGY_TAG_NAME)){
			return true;
		} else{
			return false;
		}
	}

	@Override
	public String getTagString(String tagName) {
		return "!New California Republic!";
	}

	@Override
	public Iterator<ArrayList<String>> getTableIterator(String tableTagName) {
		if (tableTagName.equals(CREATING_TABLE_STATEGY_TAG_NAME)){
			return new TestTableIterator();
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
	
	class TestTableIterator implements Iterator<ArrayList<String>>{
		private int counter = 0;

		@Override
		public boolean hasNext() {
			if (counter < MAX_ROWS){
				return true;
			}
			else{
				return false;
			}
		}

		@Override
		public ArrayList<String> next() {
			if (counter >= MAX_ROWS){
				throw new NoSuchElementException();
			}
			else{
				ArrayList<String> list = new ArrayList<String>(5);
				
				for (int i = 0; i < COLLUMN_NUMBER; i++){
					list.add((char)('A' + i) + "" + counter);
				}
				
				counter++;
				return list;
			}
		}
		
	}

}
