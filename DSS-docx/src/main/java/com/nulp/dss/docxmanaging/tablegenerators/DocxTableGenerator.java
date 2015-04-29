package com.nulp.dss.docxmanaging.tablegenerators;

import org.docx4j.wml.ContentAccessor;

import com.nulp.dss.docxmanaging.datacontainers.DataContainer;

public interface DocxTableGenerator {
	void createTable(ContentAccessor contentAccessor, DataContainer dataContainer, String tableTagName);
}
