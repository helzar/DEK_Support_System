package com.nulp.dss.docxmanaging.tablegenerators;

import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.bind.JAXBElement;

import org.apache.log4j.Logger;
import org.docx4j.jaxb.Context;
import org.docx4j.wml.ContentAccessor;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.P;
import org.docx4j.wml.R;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.Tc;
import org.docx4j.wml.Text;
import org.docx4j.wml.Tr;

import com.nulp.dss.docxmanaging.datacontainers.DataContainer;

/**
 * Requires SdtBlock tag and a header table row in template after tag
 * Copies stiles from header row
 * @author Vova
 *
 */
public class HeaderRowTableCreator implements DocxTableGenerator{
	private static final Logger LOG = Logger.getLogger(HeaderRowTableCreator.class);
	private ObjectFactory factory = Context.getWmlObjectFactory();

	public HeaderRowTableCreator(ObjectFactory factory) {
		this.factory = factory;
	}

	// Tbl -> List[JAXBElement -> Tc]
	@Override
	public void createTable(ContentAccessor contentAccessor, DataContainer dataContainer, String tableTagName){
		LOG.info("Table creation is started. Tag name - \"" + tableTagName + "\"");
		
		if (!(contentAccessor instanceof Tbl) || (((Tbl)contentAccessor).getContent().size() != 1)){
			LOG.warn("Table tag have no header table after it! Tag name - \"" + tableTagName + "\"");
			return;
		}
		
		Tbl table = (Tbl)contentAccessor;
		int columnSize = ((ContentAccessor)(table.getContent().get(0))).getContent().size();
		Iterator<ArrayList<String>> rowIterator = dataContainer.getTableIterator(tableTagName);

		if (columnSize != dataContainer.getTableColumnSize(tableTagName)){
			LOG.warn("Table header size is not correct! Template header size - \"" + columnSize + "\", "
					+ "Actual (DataContainer) size - \"" + dataContainer.getTableColumnSize(tableTagName) + "\", "
					+ "Tag name - \"" + tableTagName + "\"");
			return;
		}
		
		buildTable(table, rowIterator);
		LOG.info("Table creation is finished. Tag name - \"" + tableTagName + "\"");
	}
	
	private void buildTable(Tbl table, Iterator<ArrayList<String>> rowIterator){
		Tr headerRow = (Tr) table.getContent().get(0);
		P[] propertieParagraphs = getPropertieParagraphs(headerRow);
		
		while (rowIterator.hasNext()){
			ArrayList<String> nextRow = rowIterator.next();
			Tr tr = factory.createTr();

			for (int i = 0; i < propertieParagraphs.length; i++){
				addTableCell(tr, propertieParagraphs[i], nextRow.get(i));
			}

			table.getContent().add(tr);
		}
	}
	
	private void addTableCell(Tr tableRow, P paragraphWithProperties, String content) {
		Tc tableCell = factory.createTc();
		P p = factory.createP();
		R r = factory.createR();
		Text text = factory.createText();
		text.setValue(content);
		
		setParagraphProperties(paragraphWithProperties, p);
		setRunPropertiesFromFirstRunInParagraph(paragraphWithProperties, r);
		
		r.getContent().add(text);
		p.getContent().add(r);
		tableCell.getContent().add(p);
		tableRow.getContent().add(tableCell);
	}
	
	private void setParagraphProperties(P sourceParagraph, P destinationParagraph){
		if (sourceParagraph != null){
			destinationParagraph.setPPr(sourceParagraph.getPPr());
		}
	}
	
	private void setRunPropertiesFromFirstRunInParagraph(P sourceParagraph, R destinationRun){
		if (sourceParagraph != null){
			for (Object o: sourceParagraph.getContent()){
				if (o instanceof R){
					destinationRun.setRPr(((R) o).getRPr());
				}
			}
		}
	}
	
	/**
	 * get first paragraphs from all cells in row. If first element is not a paragraph then set NULL
	 * 
	 */
	private P[] getPropertieParagraphs(Tr headerRow){
		P[] propertieParagraphs = new P[headerRow.getContent().size()];
		for (int i = 0; i < headerRow.getContent().size(); i++){
			Object innerElement = ((JAXBElement<?>) headerRow.getContent().get(i)).getValue();
			Tc tc = (Tc)innerElement;
			
			if (tc.getContent().size() > 0){
				Object o = tc.getContent().get(0);
				if (o instanceof P){
					propertieParagraphs[i] = (P)tc.getContent().get(0);
				} else{
					propertieParagraphs[i] = null;
				}
			} else{
				propertieParagraphs[i] = null;
			}
		}
		
		return propertieParagraphs;
	}
}
