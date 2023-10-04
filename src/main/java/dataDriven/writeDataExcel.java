package dataDriven;

import java.awt.AWTException;
import java.io.File;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class writeDataExcel {

	// XSSFWorkbook workbook;
	XSSFSheet sh;

	public void writeIntoExcel(List<String> memberID, List<String> memberName, List<String> gender, List<String> dob,
			List<String> pLang, String testSheetName) throws IOException, InterruptedException, AWTException {
		String filePath = System.getProperty("user.dir") + "\\src\\test\\resources\\testCases\\MemberData.xlsx";
		FileInputStream ip = new FileInputStream(filePath);

		Workbook wb = WorkbookFactory.create(ip);

		Sheet sh = wb.getSheet(testSheetName);
		int rowcount = 0;
		Row rowHeader = sh.createRow(rowcount++);
		rowHeader.createCell(0).setCellValue("Member ID");
		rowHeader.createCell(1).setCellValue("Member Name");
		rowHeader.createCell(2).setCellValue("Gender");
		rowHeader.createCell(3).setCellValue("DOB");
		rowHeader.createCell(4).setCellValue("Primary Language");

		List<String> memberIDList = memberID;
		for (int i = 0; i < memberIDList.size(); i++) {
			Row row = sh.getRow(i + 1);
			Cell cell = row.createCell(0);
			cell.setCellValue(memberIDList.get(i));
		}

		List<String> nameList = memberName;
		for (int i = 0; i < nameList.size(); i++) {
			Row row = sh.getRow(i + 1);
			Cell cell = row.createCell(1);
			cell.setCellValue(nameList.get(i));
		}

		List<String> genderList = gender;
		for (int i = 0; i < genderList.size(); i++) {
			Row row = sh.getRow(i + 1);
			Cell cell = row.createCell(2);
			cell.setCellValue(genderList.get(i));
		}

		List<String> dobList = dob;
		for (int i = 0; i < dobList.size(); i++) {
			Row row = sh.getRow(i + 1);
			Cell cell = row.createCell(3);
			cell.setCellValue(dobList.get(i));
		}

		List<String> pLangList = pLang;
		for (int i = 0; i < pLangList.size(); i++) {
			Row row = sh.getRow(i + 1);
			Cell cell = row.createCell(4);
			cell.setCellValue(pLangList.get(i));
		}

		ip.close();

		try {

			FileOutputStream os = new FileOutputStream(filePath);
			wb.write(os);
			os.close();
			System.out.println("Data written to excel successfully");

		} catch (Exception e) {
		}
	}

}
