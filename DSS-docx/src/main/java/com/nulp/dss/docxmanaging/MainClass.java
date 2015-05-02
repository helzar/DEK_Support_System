package com.nulp.dss.docxmanaging;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.nulp.dss.docxmanaging.datacontainers.DataContainerForTableFillerImpl;
import com.nulp.dss.docxmanaging.docparsing.DocxGenerator;
import com.nulp.dss.docxmanaging.docparsing.DocxManager;
import com.nulp.dss.docxmanaging.tablegenerators.TableFiller;
import com.nulp.dss.docxparser.StudentFileReader;

public class MainClass {
	private static final Logger LOG = Logger.getLogger(MainClass.class);

	public static void main(String[] args) throws Exception {
		LOG.info("START!!!");
		
//		new DocxManager().generateDocx("input_templates\\testTempl.docx", "output\\testOutputFile.docx", new DataContainerTestImpl(), null);
		
//		new DocxManager().generateDocx("input_templates\\testTemplForTableFiller.docx", "output\\testOutputFileForTableFiller.docx",
//				new DataContainerForTableFillerImpl(), TableFiller.class);

		new DocxManager().margeDocx("input_templates/testTemplForTableFiller.docx", "input_templates/testTemplForTableFiller.docx",
				"output/margedDoc.docx");
		
		LOG.info("END!!!");
	}
	
}
