package com.nulp.dss.docxmanaging.container;

import java.util.ArrayList;
import java.util.Iterator;

public interface DataContainer {

	boolean isTagNameATable(String tagName);
	
	/**
	 * 
	 * @param tagName
	 * @return null - than tag do not exist
	 */
	String getTagString(String tagName);
	
	Iterator<ArrayList<String>> getTableIterator(String tableTagName);
	int getTableColumnSize(String tableTagName);
}
