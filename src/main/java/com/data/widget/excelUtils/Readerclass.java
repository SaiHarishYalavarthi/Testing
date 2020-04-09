package com.data.widget.excelUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.data.widget.utils.PropertiesFileReader;

public class Readerclass {
	public static Properties elementProperties = null;
	public static Properties commonProperties = null;

	static {
		elementProperties = PropertiesFileReader.getInstance().readProperties("element.properties");
		commonProperties = PropertiesFileReader.getInstance().readProperties("common.properties");

		// report = new ExtentReports(CommonUtil.filePath);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List GetData(String testname) {
		FileInputStream fileIn;
		Properties prop = new Properties();
		List<List> sheetData = null;
		// POIFSFileSystem fs;
		try {
			prop.load(new FileInputStream(System.getProperty("user.dir") + System.getProperty("file.separator")
					+ "TestData" + System.getProperty("file.separator") + "Resource.properties"));

			// test file is located in your project path
			String path = System.getProperty("user.dir") + System.getProperty("file.separator") + "TestData"
					+ System.getProperty("file.separator") + "Boat Target List.xlsx";

			fileIn = new FileInputStream(path);

			// read file
			// fs = new POIFSFileSystem(fileIn);
			XSSFWorkbook workbook = new XSSFWorkbook(fileIn);
			XSSFSheet sheet = workbook.getSheetAt(0);
			String columnWanted = prop.getProperty("column");
			Integer statusColumnNo = 0;

			// output all not null values to the list
			sheetData = new ArrayList<List>();

			Row firstRow = sheet.getRow(0);

			for (Cell cell : firstRow) {
				if (cell.getStringCellValue().equals(columnWanted)) {
					statusColumnNo = cell.getColumnIndex();
					System.out.println(cell.getStringCellValue());
				}
			}

			if (statusColumnNo != null) {

				for (Row row : sheet) {
					Cell statuscolumnData = row.getCell(statusColumnNo);

					if (statuscolumnData == null || statuscolumnData.getCellType() == Cell.CELL_TYPE_BLANK) {
						// Nothing in the cell in this row, skip it
					} else {
						if (statuscolumnData.getStringCellValue().equalsIgnoreCase(prop.getProperty("statusvalue"))) {
							// Nothing in the cell in this row, skip it
						} else {

							Row r = statuscolumnData.getRow();
							Iterator rowCell = r.cellIterator();
							List data = new ArrayList();
							while (rowCell.hasNext()) {
								XSSFCell hssCell = (XSSFCell) rowCell.next();

								// Converts int to string during excel reading
								hssCell.setCellType(Cell.CELL_TYPE_STRING);
								System.out.print(hssCell.getStringCellValue() + "\n");
								// System.out.println(
								// hssCell.getStringCellValue());
								data.add(hssCell);
								// String test = data.add(hssCell);
							}

							for (Object s : data) {
								if (s.toString().equals("Yes")) {
									System.out.println(data + "\t");
									sheetData.add(data);
								}
							}

						}
					}
				}
				// System.out.println(columnNo);
			}

			else {
				System.out.println("could not find column " + columnWanted + " in first row of " + fileIn.toString());
			}
			for (int i = 0; i < sheetData.size(); i++) {
				List list = (List) sheetData.get(i);
				for (int j = 0; j < list.size(); j++) {
					XSSFCell rollick = (XSSFCell) list.get(j);
					System.out.println("--------------------------------");
					System.out.print(rollick.getRichStringCellValue().getString());
					if (j < list.size() - 1) {
						// System.out.print(" ");
					}
				}
				System.out.println("  ");
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Your Enter file is not Existed in Directory");
		}
		return sheetData;
	}

	@SuppressWarnings("rawtypes")
	public static List writeData(String test) {

		Properties prop = new Properties();
		List<List> sheetData = null;
		// POIFSFileSystem fs;
		try {
			prop.load(new FileInputStream(System.getProperty("user.dir") + System.getProperty("file.separator")
					+ "TestData" + System.getProperty("file.separator") + "Resource.properties"));

			// test file is located in your project path
			String path = System.getProperty("user.dir") + System.getProperty("file.separator") + "excelReport"
					+ System.getProperty("file.separator") + "Report.xlsx";

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("reslut");

			Row row = sheet.createRow(1);
			Cell cell = row.createCell(1);
			cell.setCellType(Cell.CELL_TYPE_STRING);
			cell.setCellValue(test);
			FileOutputStream outFile = new FileOutputStream(path);
			workbook.write(outFile);
			outFile.close();
			System.out.println("END OF WRITING DATA IN EXCEL");
		} catch (IOException e) {

		}
		return sheetData;
	}

	public static void writeData1(LinkedList<?> test) throws FileNotFoundException, IOException {
		Properties prop = new Properties();
		DataWriterClass pojoWrite = null;
		prop.load(new FileInputStream(System.getProperty("user.dir") + System.getProperty("file.separator") + "TestData"
				+ System.getProperty("file.separator") + "Resource.properties"));

		// test file is located in your project path
		String path = System.getProperty("user.dir") + System.getProperty("file.separator") + "excelReport"
				+ System.getProperty("file.separator") + "Report.xlsx";
		XSSFWorkbook workbook = new XSSFWorkbook();

		// Create a blank sheet
		XSSFSheet sheet = workbook.createSheet("Dealer");
		Map<String, Object[]> title = new TreeMap<String, Object[]>();
		title.put("S.NO",
				new Object[] { "S.No", "Dealer Name", "Zip Code", "Phone Number", "Website", "Copy Right" });
		Set<String> keyset1 = title.keySet();
		for (String key1 : keyset1) {
			Row row = sheet.createRow(0);
			Object[] objTitle = title.get(key1);
			int cellNum = 0;
			for (Object obj : objTitle) {
				Cell cell = row.createCell(cellNum++);
				if (obj instanceof String)
					cell.setCellValue((String) obj);
				else if (obj instanceof Integer)
					cell.setCellValue((Integer) obj);
			}
		}
		// This data needs to be written (Object[])
		int count = test.size();
		System.out.println("No of records:- " + " " + count);
		for (int a = 0; a < count; a++) {
			pojoWrite = (DataWriterClass) test.get(a);
			Map<String, Object[]> data = new TreeMap<String, Object[]>();
			data.put(pojoWrite.getSNo(), new Object[] { pojoWrite.getSNo(), pojoWrite.getDealerName(),
					pojoWrite.getZip(), pojoWrite.getTelephoneNumber(), pojoWrite.getUrl(), pojoWrite.getCopyright() });
			

			// Iterate over data and write to sheet
			Set<String> keyset = data.keySet();
			int rownum = a + 1;
			for (String key : keyset) {
				Row row = sheet.createRow(rownum++);
				Object[] objArr = data.get(key);
				int cellnum = 0;
				for (Object obj : objArr) {
					Cell cell = row.createCell(cellnum++);
					if (obj instanceof String)
						cell.setCellValue((String) obj);
					else if (obj instanceof Integer)
						cell.setCellValue((Integer) obj);
				}
			}
		}

		FileOutputStream outFile = new FileOutputStream(path);
		workbook.write(outFile);
		outFile.close();
		System.out.println("END OF WRITING DATA IN EXCEL");
	}

	public static void writeXLSFile(String fileName) throws IOException {

		String excelFileName = System.getProperty("user.dir") + System.getProperty("file.separator") + "excelReport"
				+ System.getProperty("file.separator") + fileName;

		String sheetName = "Test";// name of sheet
		File file = new java.io.File(excelFileName);
		// file.mkdirs(); // wrong!
		file.getParentFile().mkdirs(); // correct!
		if (!file.exists()) {
			file.createNewFile();
		}

		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet(sheetName);

		// iterating r number of rows
		for (int r = 0; r < 5; r++) {
			Row row = sheet.createRow(r);

			// iterating c number of columns
			for (int c = 0; c < 5; c++) {
				Cell cell = row.createCell(c);

				cell.setCellValue("Cell " + r + " " + c);
			}
		}

		file = new java.io.File(excelFileName);
		// file.mkdirs(); // wrong!
		file.getParentFile().mkdirs(); // correct!
		if (!file.exists()) {
			file.createNewFile();
		}

		FileOutputStream fileOut = new FileOutputStream(file);

		// write this workbook to an Outputstream.
		wb.write(fileOut);
		fileOut.flush();
		fileOut.close();
	}

}
