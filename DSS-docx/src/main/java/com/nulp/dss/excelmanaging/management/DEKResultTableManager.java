package com.nulp.dss.excelmanaging.management;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.nulp.dss.docxmanaging.container.DataContainer;
import com.nulp.dss.docxmanaging.container.generator.ScheduleFormContainerGenerator;
import com.nulp.dss.excelmanaging.generation.DEKResultTableGenerator;
import com.nulp.dss.model.enums.QuarterEnum;

public class DEKResultTableManager {
	private static final Logger LOG = Logger.getLogger(DEKResultTableManager.class);
	private static final String WINTER_TEMPLATE_FILE_PATH = "input_templates\\DEK_winter_results_table_template.xls";
	private static final String WINTER_BASE_OUTPUT_FILE_PATH = "output\\DEK_results_table\\winter";
	private static final String SUMMER_TEMPLATE_FILE_PATH = "input_templates\\DEK_summer_results_table_template.xls";
	private static final String SUMMER_BASE_OUTPUT_FILE_PATH = "output\\DEK_results_table\\summer";

	public String generate(int graduationId, QuarterEnum quarter) throws Exception{
		if (quarter == null){
			LOG.warn("Quarter is NULL!");
		}
		
		String baseOutputFilePath = getBaseOutputFilePath(quarter);
		String templateFilePath = getTemplateFilePath(quarter);
		String outputPath = baseOutputFilePath + "\\DEK_results_table_" + graduationId + ".xls";
		prepareOutputDirectory(baseOutputFilePath, outputPath);
		
		DataContainer container = new ScheduleFormContainerGenerator().getPaymentFormContainer(graduationId);
		if (container != null){
			generate(templateFilePath, outputPath, graduationId);
			return outputPath;
		}
		else{
			return null;
		}
	}
	
	private String getTemplateFilePath(QuarterEnum quarter) {
		if (quarter.equals(QuarterEnum.summer)){
			return SUMMER_TEMPLATE_FILE_PATH;
		}
		else {
			return WINTER_TEMPLATE_FILE_PATH;
		}
	}

	private String getBaseOutputFilePath(QuarterEnum quarter) {
		if (quarter.equals(QuarterEnum.summer)){
			return SUMMER_BASE_OUTPUT_FILE_PATH;
		}
		else {
			return WINTER_BASE_OUTPUT_FILE_PATH;
		}
	}

	private void generate(String inputTemplateFileName, 
			String outputFileName, int graduationId) throws Exception {
		File inputTemplateFile = new File(inputTemplateFileName);
		File outputFile = new File(outputFileName);
		generateDocuments(inputTemplateFile, outputFile, graduationId);
	}
	
	private void generateDocuments(File inputTemplateFile, File outputFile, int graduationId) throws Exception {
		
		LOG.info("Excel document generation started. Input template path - \""
				+ inputTemplateFile + "\"" + "Output file path - \""
				+ outputFile + "\"");
		checkTampleteFileValid(inputTemplateFile);
		Workbook wb = WorkbookFactory.create(inputTemplateFile);
		new DEKResultTableGenerator().generate(wb, graduationId);
		try (FileOutputStream out = new FileOutputStream(outputFile)){
			wb.write(out);
		}
		
		LOG.info("Excel document generation finished!");
	}
	
	private void checkTampleteFileValid(File inputTemplateFile)
			throws Exception {
		if (!inputTemplateFile.isFile() || !inputTemplateFile.exists()) {
			throw new IOException("Input docx template file is not valid - "
					+ inputTemplateFile.getAbsolutePath());
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
