package com.nulp.dss.docxmanaging.generation;

import java.lang.reflect.InvocationTargetException;

import javax.xml.bind.JAXBElement;

import org.apache.log4j.Logger;
import org.docx4j.jaxb.Context;
import org.docx4j.wml.ContentAccessor;
import org.docx4j.wml.P;
import org.docx4j.wml.R;
import org.docx4j.wml.SdtBlock;
import org.docx4j.wml.SdtRun;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.Text;

import com.nulp.dss.docxmanaging.container.DataContainer;
import com.nulp.dss.docxmanaging.generation.tablegenerators.DocxTableGenerator;
import com.nulp.dss.docxmanaging.generation.tablegenerators.HeaderRowTableCreator;

public class DocxGenerator {
	private static final Logger LOG = Logger.getLogger(DocxGenerator.class);
	private ObjectFactory factory = Context.getWmlObjectFactory();
	
	private DocxTableGenerator docxTableGenerator;
	private DataContainer dataContainer;
	
	private static final String DEFAULT_TEXT_IF_TAG_NAME_DO_NOT_EXIST = "!TAG_NAME_DO_NOT_EXIST!";
	private Boolean isCreatingTableStrategyEnabled = false;
	private String tableTagName;
	
	
	public DocxGenerator(DataContainer dataContainer, Class<? extends DocxTableGenerator> docxTableGeneratorClass) {
		if (docxTableGeneratorClass != null){
			try {
				this.docxTableGenerator = docxTableGeneratorClass.getDeclaredConstructor(ObjectFactory.class).newInstance(factory);
				LOG.info("As DocxTableGenerator strategy is chosen - \"" + docxTableGeneratorClass + "\"");
			} catch (InstantiationException | IllegalAccessException
					| IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException e) {
				LOG.warn("\"" + docxTableGeneratorClass + "\" class have no required constructor! "
						+ "As DocxTableGenerator strategy is chosen default \"HeaderRowTableCreator\"");
				this.docxTableGenerator = new HeaderRowTableCreator(factory);
				e.printStackTrace();
			}
		}
		else {
			this.docxTableGenerator = new HeaderRowTableCreator(factory);
			LOG.info("As DocxTableGenerator strategy is chosen default \"HeaderRowTableCreator\"");
		}
		this.dataContainer = dataContainer;
	}

	public void replaceText(ContentAccessor contentAccessor) throws Exception {
		if (isCreatingTableStrategyEnabled){
			docxTableGenerator.createTable(contentAccessor, dataContainer, tableTagName);
			isCreatingTableStrategyEnabled = false;
			return;
		}
		
		for (int i = 0; i < contentAccessor.getContent().size(); i++){
			Object element = contentAccessor.getContent().get(i);
			
			if (element instanceof ContentAccessor){
				replaceText((ContentAccessor) element);
			}
			else if (element instanceof JAXBElement) {
				Object innerElement = ((JAXBElement<?>) element).getValue();

				if (innerElement instanceof ContentAccessor){
					replaceText((ContentAccessor) innerElement);
				}
				/** is a sample text tag*/
				else if (innerElement instanceof SdtRun){
					processSdtRunTagElement((SdtRun)innerElement, contentAccessor, i);
					continue;
				}
				else if (innerElement instanceof SdtBlock){
					LOG.warn("Internal sdtBlock was found! Tag key - \"" + ((SdtBlock)innerElement).getSdtPr().getTag().getVal() + "\"");
				}
			}
			/** is a paragraph tag*/
			else if (element instanceof SdtBlock){
				processSdtBlockTagElement((SdtBlock)element, contentAccessor, i);
				continue;
			}
		}
	}
	
	/**
	 * SdtRun is normal ContentControl element
	 * if can not read R - set default and log
	 */
	private void processSdtRunTagElement(SdtRun sdtRun, ContentAccessor contentAccessor, int elementIndex){
		LOG.info("Tag name - \"" + sdtRun.getSdtPr().getTag().getVal() + "\"");

		String tagName = sdtRun.getSdtPr().getTag().getVal();
		Text text = getTextElementAndTableConfig(tagName);

		R run = null;
		for (Object obj: sdtRun.getSdtContent().getContent()){
			if (obj instanceof R){
				run = factory.createR();
				run.setRPr(((R)obj).getRPr());
				break;
			}
		}
		if (run == null){
			run = factory.createR();
			LOG.warn("Can not read run of SdtRun! Tag key - \"" + sdtRun.getSdtPr().getTag().getVal() + "\"");
		}

		run.getContent().add(text);
		contentAccessor.getContent().set(elementIndex, run);
	}
	
	/**
	 * SdtBlock is ContentControl element what is an paragraph in root docx element, and/or it can content other elements
	 * if can not read R or P - set default and log
	 */
	private void processSdtBlockTagElement(SdtBlock sdtBlock, ContentAccessor contentAccessor, int elementIndex){
		LOG.info("Tag name - \"" + sdtBlock.getSdtPr().getTag().getVal() + "\"");
		
		String tagName = sdtBlock.getSdtPr().getTag().getVal();
		Text text = getTextElementAndTableConfig(tagName);
		
		R run = null;
		P paragraph = null;
		for (Object p: sdtBlock.getSdtContent().getContent()){
			if (p instanceof P){
				P sdtBlockParagraph = (P) p;
				paragraph = factory.createP();
				paragraph.setPPr(sdtBlockParagraph.getPPr());
				for (Object r: sdtBlockParagraph.getContent()){
					if (r instanceof R){
						run = factory.createR();
						run.setRPr(((R) r).getRPr());
					}
				}
			}
		}
		if (run == null){
			run = factory.createR();
			LOG.warn("Can not read run of sdtBlock! Tag key - \"" + sdtBlock.getSdtPr().getTag().getVal() + "\"");
		}
		if (paragraph == null){
			paragraph = factory.createP();
			LOG.warn("Can not read paragraph of sdtBlock! Tag key - \"" + sdtBlock.getSdtPr().getTag().getVal() + "\"");
		}

		run.getContent().add(text);
		paragraph.getContent().add(run);
		contentAccessor.getContent().set(elementIndex, paragraph);
	}

	
	private Text getTextElementAndTableConfig(String tagName){
		Text text = factory.createText();
		if (dataContainer.isTagNameATable(tagName)){
			isCreatingTableStrategyEnabled = true;
			tableTagName = tagName;
			text.setValue("");
		} else{
			isCreatingTableStrategyEnabled = false;
			setTagText(text, tagName);
		}
		return text;
	}

	private void setTagText(Text text, String tagName){
		String tagText = dataContainer.getTagString(tagName);
		if (tagText != null){
			text.setValue(tagText);
		}
		else{
			LOG.warn("Tag name do not exist! Name - \"" + tagName + "\"");
			text.setValue(DEFAULT_TEXT_IF_TAG_NAME_DO_NOT_EXIST);
		}
	}
	
}
