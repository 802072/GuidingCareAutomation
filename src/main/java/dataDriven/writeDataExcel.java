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

public class writeDataExcel {

	// XSSFWorkbook workbook;
	XSSFSheet sh;

	public void writeIntoExcel(ArrayList<String> UIlist, ArrayList<String> TSIDlist, String testSheetName)
			throws IOException, InterruptedException, AWTException {
		String filePath = System.getProperty("user.dir")+"\\src\\test\\resources\\testCases\\testDataWebUI.xlsx";
		FileInputStream ip = new FileInputStream(filePath);
		//XSSFWorkbook wb = new XSSFWorkbook(ip);
		
		Workbook wb = WorkbookFactory.create(ip);
		
//		if (wb.getNumberOfSheets() != 0) {
//	        for (int i = 0; i < wb.getNumberOfSheets(); i++) {
//	           if (wb.getSheetName(i).equals(testSheetName)) {
//	                sh = (XSSFSheet) wb.getSheet(testSheetName);
//	            } else sh = (XSSFSheet) wb.createSheet(testSheetName);
//	        }
//	    }
	  
	//	sh = (XSSFSheet) wb.createSheet(testSheetName);
	   
		Sheet sh = wb.getSheet(testSheetName);
		int rowcount = 0;
		Row rowHeader = sh.createRow(rowcount++);
		rowHeader.createCell(0).setCellValue("Test Step ID");
		rowHeader.createCell(1).setCellValue("UI Values");
		// rowHeader.createCell(2).setCellValue("DB Values");

		ArrayList<String> list1 = UIlist;
		for (int i = 0; i < list1.size(); i++) {
			Row row = sh.createRow(i + 1);
			Cell cell = row.createCell(1);
			cell.setCellValue(list1.get(i));
		}

		ArrayList<String> list2 = TSIDlist;
		for (int i = 0; i < list2.size(); i++) {
			Row row = sh.getRow(i + 1);
			Cell cell = row.createCell(0);
			cell.setCellValue(list2.get(i));
		}

		ip.close();

		try {

			FileOutputStream os = new FileOutputStream(filePath);
			wb.write(os);
			os.close();
			System.out.println("Successful");

		} catch (Exception e) {
		}
	}

}
