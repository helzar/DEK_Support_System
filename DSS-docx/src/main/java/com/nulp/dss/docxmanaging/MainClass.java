package com.nulp.dss.docxmanaging;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.nulp.dss.docxmanaging.container.DataContainerForTableFillerImpl;
import com.nulp.dss.docxmanaging.container.generator.PaymentFormContainerGenerator;
import com.nulp.dss.docxmanaging.generation.DocxGenerator;
import com.nulp.dss.docxmanaging.generation.tablegenerators.TableFiller;
import com.nulp.dss.docxmanaging.management.DocxManager;
import com.nulp.dss.docxmanaging.management.PaymentFormManager;
import com.nulp.dss.docxmanaging.management.ScheduleFormManager;
import com.nulp.dss.docxmanaging.reader.StudentFileReader;

public class MainClass {
	private static final Logger LOG = Logger.getLogger(MainClass.class);

	public static void main(String[] args) throws Exception {
		LOG.info("START!!!");
		
//		new DocxManager().generateDocx("input_templates\\testTempl.docx", "output\\testOutputFile.docx", new DataContainerTestImpl(), null);
		
//		new DocxManager().generateDocx("input_templates\\testTemplForTableFiller.docx", "output\\testOutputFileForTableFiller.docx",
//				new DataContainerForTableFillerImpl(), TableFiller.class);

//		new DocxManager().margeDocx("input_templates/testTemplForTableFiller.docx", "input_templates/testTemplForTableFiller.docx",
//				"output/margedDoc.docx");
		
//		System.out.println(new PaymentFormContainerGenerator().getPaymentFormContainer(28, 2));
//		new PaymentFormManager().generateDocuments(30, 2);
		
		new ScheduleFormManager().generateDocuments(33);

		LOG.info("END!!!");
	}
	
}
