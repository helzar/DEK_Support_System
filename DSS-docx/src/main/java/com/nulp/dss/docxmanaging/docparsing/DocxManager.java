package com.nulp.dss.docxmanaging.docparsing;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import com.nulp.dss.docxmanaging.datacontainers.DataContainer;
import com.nulp.dss.docxmanaging.tablegenerators.DocxTableGenerator;

public class DocxManager {
	private static final Logger LOG = Logger.getLogger(DocxManager.class);

	/**
	 * 
	 * @param inputTemplateFileName
	 * @param outputFileName
	 * @param dataContainer
	 * @param docxTableGenerator  -  if null, than default is DocxTableGeneratorByHeaderRow
	 * @throws Exception
	 */
	public void generateDocx(String inputTemplateFileName,
			String outputFileName, DataContainer dataContainer,
			Class<? extends DocxTableGenerator> docxTableGeneratorClass) throws Exception {
		
		File inputTemplateFile = new File(inputTemplateFileName);
		File outputFile = new File(outputFileName);
		generateDocx(inputTemplateFile, outputFile, dataContainer,
				docxTableGeneratorClass);
	}

	private void generateDocx(File inputTemplateFile, File outputFile,
			DataContainer dataContainer, Class<? extends DocxTableGenerator> docxTableGeneratorClass)
			throws Exception {
		
		LOG.info("Document generation started. Input template path - \""
				+ inputTemplateFile + "\"" + "Output file path - \""
				+ outputFile + "\"");
		checkTampleteFileValid(inputTemplateFile);
		WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage
				.load(inputTemplateFile);
		new DocxGenerator(dataContainer, docxTableGeneratorClass)
				.replaceText(wordMLPackage.getMainDocumentPart());
		wordMLPackage.save(outputFile);
		LOG.info("Document generation finished!");
	}
	
	public void margeDocx(String firstFileName, String secondFileName, String margedFileName) throws Exception{
		LOG.info("Marging documents! Docs: \"" + firstFileName + "\", \"" + secondFileName + "\". Output in \"" + margedFileName + "\"");
		File firstFile = new File(firstFileName);
		File secondFile = new File(secondFileName);
		File margedFile = new File(margedFileName);
		WordprocessingMLPackage docDest = WordprocessingMLPackage.load(firstFile);
		WordprocessingMLPackage docSource = WordprocessingMLPackage.load(secondFile);
		append(docDest, docSource);
		docDest.save(margedFile);
		LOG.info("Documents are successfully merged!");
	}
	
	private static void append(WordprocessingMLPackage docDest, WordprocessingMLPackage docSource) {
		List<Object> objects = docSource.getMainDocumentPart().getContent();
	    for(Object o : objects){
	        docDest.getMainDocumentPart().getContent().add(o);
	    }
	}

	private void checkTampleteFileValid(File inputTemplateFile)
			throws Exception {
		if (!inputTemplateFile.isFile() || !inputTemplateFile.exists()) {
			throw new IOException("Input docx template file is not valid - "
					+ inputTemplateFile.getAbsolutePath());
		}
	}
}
