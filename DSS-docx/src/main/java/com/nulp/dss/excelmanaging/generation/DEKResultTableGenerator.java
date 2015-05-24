package com.nulp.dss.excelmanaging.generation;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;

import com.nulp.dss.excelmanaging.container.DEKResultContainer;
import com.nulp.dss.excelmanaging.container.generator.DEKResultContainerGenerator;

public class DEKResultTableGenerator {
	private static final Logger LOG = Logger.getLogger(DEKResultTableGenerator.class);

	public void generate(Workbook wb, int graduationId) {

		List<DEKResultContainer> containers = new DEKResultContainerGenerator().generate(graduationId);
		if (containers == null){
			LOG.warn("Graduation do not exists! Graduation id - " + graduationId);
			return;
		}
		
		Sheet sheet = wb.getSheetAt(0);
		Row row;
		Cell cell;
		
		for (DEKResultContainer container: containers){
			CellReference cr = new CellReference(container.getPosition());
			row = sheet.getRow(cr.getRow());
			cell = row.getCell(cr.getCol());

			cell.setCellType(Cell.CELL_TYPE_BLANK);
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell.setCellValue(container.getValue());
		}
		
		runAllFormulas(wb);
	}

	private void runAllFormulas(Workbook wb) {
		FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
		
		for (int sn = 0; sn < wb.getNumberOfSheets(); sn++) {
			Sheet sheet = wb.getSheetAt(sn);
			for (Row row : sheet) {
				for (Cell cell : row) {
					if (cell.getCellType() == Cell.CELL_TYPE_FORMULA){
						evaluator.evaluateFormulaCell(cell);
					}
				}
			}
		}
	}
}
