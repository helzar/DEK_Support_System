package com.nulp.dss.docxmanaging.generation.tablegenerators;

import org.docx4j.wml.ContentAccessor;

import com.nulp.dss.docxmanaging.container.DataContainer;

public interface DocxTableGenerator {
	void createTable(ContentAccessor contentAccessor, DataContainer dataContainer, String tableTagName);
}
