package com.nulp.dss.docxmanaging.management;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.nulp.dss.docxmanaging.container.DataContainer;
import com.nulp.dss.docxmanaging.container.generator.PaymentFormContainerGenerator;
import com.nulp.dss.docxmanaging.generation.tablegenerators.TableFiller;

public class PaymentFormManager {
	private static final String TEMPLATE_FILE_PATH = "input_templates\\payment_form_template.docx";
	private static final String BASE_OUTPUT_FILE_PATH = "output\\payment_forms";
		
	/**
	 * 
	 * @param graduationId
	 * @param reviewerId
	 * @return final document path
	 * @throws Exception
	 */
	public String generateDocuments(int graduationId, int reviewerId) throws Exception{
		String outputPath = BASE_OUTPUT_FILE_PATH + "\\" + graduationId + "-" + reviewerId + "\\";
		prepareOutputDirectory(outputPath);
		
		List<DataContainer> containers = new PaymentFormContainerGenerator().getPaymentFormContainer(graduationId, reviewerId);
		
		String documentPath;
		List<String> partPathList = new ArrayList<String>();
		for (int i = 0; i < containers.size(); i++){
			documentPath = outputPath + "part-" + i + ".docx";
			
			new DocxManager().generateDocuments(TEMPLATE_FILE_PATH, documentPath, containers.get(i), TableFiller.class);
			partPathList.add(documentPath);
		}
		
		String finalDocumentPath = outputPath + "final.docx";
		new DocxManager().margeDocuments(finalDocumentPath, partPathList);
		
		return finalDocumentPath;
	}
	
	public void prepareOutputDirectory(String outputPath){
		File outputFile = new File(outputPath);
		if (!outputFile.exists()){
			new File(outputPath).mkdirs();
		}
		else{
			for (File fl: outputFile.listFiles()){
				fl.delete();
			}
		}
	}
	
}
