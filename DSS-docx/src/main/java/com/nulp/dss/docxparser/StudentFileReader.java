package com.nulp.dss.docxparser;

import java.io.FileInputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Table;
import org.apache.poi.hwpf.usermodel.TableCell;
import org.apache.poi.hwpf.usermodel.TableRow;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.nulp.dss.dao.DiplomaDao;
import com.nulp.dss.dao.GraduationDao;
import com.nulp.dss.dao.GroupDao;
import com.nulp.dss.model.Diploma;
import com.nulp.dss.model.DiplomaInfo;
import com.nulp.dss.model.Graduation;
import com.nulp.dss.model.Group;
import com.nulp.dss.model.Review;
import com.nulp.dss.model.Student;
import com.nulp.dss.util.HibernateUtil;

//Клас Range, екстендить:
//CharacterRun, DocumentPosition, Paragraph, Section, Table, TableCell, TableRow 
public class StudentFileReader {
	
	private GraduationDao graduationDao = new GraduationDao();
	private GroupDao groupDao = new GroupDao();
	private DiplomaDao diplomaDao = new DiplomaDao();
	
	private final Pattern patternForGroupName = Pattern.compile("Група (.+)");
	
	private Graduation graduation = null;
	private Group group = null;
	private Student newStudent = null;
	private int newStudentCreationState = -1;
	private int studentPIPReaded = 0;
	private boolean isFirstRow = true;

	public void readFile(String fileName, Graduation graduation) {
		this.graduation = graduation;
		group = null;
		newStudent = null;
		isFirstRow = true;
		
		readDocument(fileName);
		graduationDao.update(graduation);
	}

	private void readDocument(String fileName) {
		
		POIFSFileSystem fs = null;
		
		try {
			fs = new POIFSFileSystem(new FileInputStream(fileName));
			HWPFDocument doc = new HWPFDocument(fs);
			readTables(doc);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void readTables(HWPFDocument doc) throws Exception {
		Range range = doc.getRange();

		for (int i = 0; i < range.numParagraphs(); i++) {
			Paragraph par = range.getParagraph(i);

			if (par.isInTable() && group != null) {
				Table table = range.getTable(par);
//				System.out.println(table.numRows());
				for (int rowIdx = 0; rowIdx < table.numRows(); rowIdx++) {

					newStudentCreationState = -1;
					TableRow row = table.getRow(rowIdx);
//					System.out.println("row " + (rowIdx + 1) + ", is table header: " + row.isTableHeader());
					for (int colIdx = 0; colIdx < row.numCells(); colIdx++) {
						TableCell cell = row.getCell(colIdx);
						
//						System.out.println("column " + (colIdx + 1) + ", text=");
						Paragraph localPar;
						for (int j = 0; j < cell.numParagraphs(); j++){
							localPar = cell.getParagraph(j);
							if (!isFirstRow && group != null){
								manageNewStudent(colIdx, j, localPar.text());
//								System.out.print("\tPar" + (j + 1) + " - " + localPar.text());
							}
							i++;
						}
//						System.out.println();
//						System.out.println("column " + (colIdx + 1) + ", text=" + cell.getParagraph(0).text());
					}
//					System.out.println();
					if (isFirstRow){
						isFirstRow = false;
					}
					i++;
				}
//				System.out.println("Index - " + i);
			}
			
			par = range.getParagraph(i);
			if (par.text().startsWith("Група ")){
				setNewGroup(par.text());
			}
		}
	}
	
	private void manageNewStudent(int columnIndex, int paragraphIndex, String text){
		if (columnIndex == 1){
			String[] splitedString = text.split(" ");
			if (splitedString.length > 0 && studentPIPReaded < 3 && !(splitedString.length == 1 && splitedString[0].length() <= 2) ){
				if (studentPIPReaded == 0){
					setNewStudent();
				}
				/** Remove \n in the end of paragraph */
				splitedString[splitedString.length - 1] = splitedString[splitedString.length - 1].substring(0, splitedString[splitedString.length - 1].length() - 1);
				for (int i = 0; i < splitedString.length; i++){
					if ( (i + studentPIPReaded) == 0 ) newStudent.setlName(splitedString[i]);
					if ( (i + studentPIPReaded) == 1 ) newStudent.setfName(splitedString[i]);
					if ( (i + studentPIPReaded) == 2 ) newStudent.setmName(splitedString[i]);
				}
				newStudentCreationState = 1;
				studentPIPReaded += splitedString.length;
			}
		}
		else if (newStudentCreationState == 1 && columnIndex == 2 && paragraphIndex == 0){
			newStudent.getDiploma().setTheme(text.substring(0, text.length() - 1));
			newStudentCreationState = 2;
			studentPIPReaded = 0;
		}
		else if (newStudentCreationState == 2 && columnIndex == 2 && paragraphIndex == 1){
			// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			// set head of diploma
			saveNewStudent();
			newStudentCreationState = -1;
		}
	}
	
	private void setNewStudent(){
		newStudent = new Student();
		Diploma newDiploma = new Diploma();
		DiplomaInfo newDiplomaInfo = new DiplomaInfo();
		Review newReview = new Review();
		
		newDiploma.setDiplomaInfo(newDiplomaInfo);
		newDiploma.setStudent(newStudent);
		newDiploma.setReview(newReview);
		
		newDiplomaInfo.setDiploma(newDiploma);
		newStudent.setDiploma(newDiploma);
		newReview.setDiploma(newDiploma);
	}
	
	private void saveNewStudent(){
		diplomaDao.save(newStudent.getDiploma());
		
		newStudent.setGroup(group);
		group.getStudents().add(newStudent);
	}
	
	private void setNewGroup(String groupName){
		groupName = cleanGroupName(groupName);
		if (groupName == null){
			group = null;
			return;
		}
		
		isFirstRow = true;
		for (Group localGroup: graduation.getGroups()){
			if (localGroup.getName().equals(groupName)){
				group = localGroup;
				HibernateUtil.lazyInitialize(group, group.getStudents());
				return;
			}
		}
		
		group = new Group();
		group.setName(groupName);
		groupDao.insert(group);
		
		graduation.getGroups().add(group);
		group.setGraduation(graduation);
		
		groupDao.update(group);
		HibernateUtil.lazyInitialize(group, group.getStudents());
	}

	private String cleanGroupName(String groupName){
		Matcher m = patternForGroupName.matcher(groupName);
		if (m.find()){
			return m.group(1);
		}
		return null;
	}
	
}