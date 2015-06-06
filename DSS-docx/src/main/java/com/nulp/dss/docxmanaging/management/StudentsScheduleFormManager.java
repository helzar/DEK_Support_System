package com.nulp.dss.docxmanaging.management;

import java.io.File;

import com.nulp.dss.docxmanaging.container.DataContainer;
import com.nulp.dss.docxmanaging.container.generator.StudentsScheduleFormContainerGenerator;
import com.nulp.dss.docxmanaging.generation.tablegenerators.HeaderRowTableCreator;

public class StudentsScheduleFormManager {
	private static final String TEMPLATE_FILE_PATH = "input_templates\\Students_schedule_template.docx";
	private static final String BASE_OUTPUT_FILE_PATH = "output\\students_schedule";
		
	/**
	 * 
	 * @param graduationId
	 * @param reviewerId
	 * @return final document path, or null if graduation do not exist
	 * @throws Exception
	 */
	public String generateDocuments(int graduationId) throws Exception{
		String outputPath = BASE_OUTPUT_FILE_PATH + "\\DEK_schedule_" + graduationId + ".docx";
		prepareOutputDirectory(BASE_OUTPUT_FILE_PATH, outputPath);
		
		DataContainer container = new StudentsScheduleFormContainerGenerator().getPaymentFormContainer(graduationId);
		if (container != null){
			new DocxManager().generateDocuments(TEMPLATE_FILE_PATH, outputPath, container, HeaderRowTableCreator.class);
			return outputPath;
		}
		else{
			return null;
		}
	}
	
	private void prepareOutputDirectory(String outputDirectoryPath, String outputFilePath){
		File outputDirectoryFile = new File(outputDirectoryPath);
		if (!outputDirectoryFile.exists()){
			outputDirectoryFile.mkdirs();
		}
		else{
			File outputFile = new File(outputFilePath);
			if (outputFile.exists()){
				outputFile.delete();
			}
		}
	}
}
