package com.data.widget.excelUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * This program illustrates how to update an existing Microsoft Excel document.
 * Append new rows to an existing sheet.
 *
 * @author www.codejava.net
 *
 */
public class ExcelFileUpdateExample1 {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void Read(int id, String Name, String URL) throws IOException {
		List<List> sheetData = null;
		FileInputStream fileIn;
		String path = System.getProperty("user.dir") + System.getProperty("file.separator") + "TestData"
				+ System.getProperty("file.separator") + "gfgcontribute.xlsx";
		File file = new java.io.File(path);
		file.getParentFile().mkdirs();
		if (!file.exists()) {
			file.createNewFile();
		} else {
			fileIn = new FileInputStream(path);
			XSSFWorkbook workbook = new XSSFWorkbook(fileIn);
			XSSFSheet sheet = workbook.getSheetAt(0);
			sheetData = new ArrayList<List>();
			Integer statusColumnNo = 0;

			Row firstRow = sheet.getRow(0);
			for (Cell cell : firstRow) {
				//System.out.println(cell.getNumericCellValue());
				if (!(cell.getStringCellValue()).isEmpty()) {
					statusColumnNo = cell.getColumnIndex();
					System.out.println(cell.getStringCellValue());
				}
			}
			if (statusColumnNo != null) {

				for (Row row : sheet) {
					Cell statuscolumnData = row.getCell(statusColumnNo );

					if (statuscolumnData == null || statuscolumnData.getCellType() == Cell.CELL_TYPE_BLANK) {
					} else {

							Row r = statuscolumnData.getRow();
							Iterator rowCell = r.cellIterator();
							List data = new ArrayList();
							while (rowCell.hasNext()) {
								XSSFCell hssCell = (XSSFCell) rowCell.next();
								hssCell.setCellType(Cell.CELL_TYPE_STRING);
								System.out.print(hssCell.getStringCellValue() + "\n");
								data.add(hssCell);
							}
							for (Object s : data) {
								if (!(s.toString()).isEmpty()) {
									System.out.println(data + "\t");
									sheetData.add(data);
								}
							}

						}
					}
				}
			

			//sheetData = new ArrayList<List>();
			int i = sheetData.size();
			int rownum = 0;
			int cellnum = 0;
			/*for (Iterator<Cell> iterator = firstRow.iterator(); iterator.hasNext();) {
				Cell cell = iterator.next();*/
				if (!(i==0))  {
					//sheet.createRow(1);
					String SNO = Integer.toString(id);
					Map<String, Object[]> data = new TreeMap<String, Object[]>();
					data.put(SNO, new Object[] { id, Name, URL });
					Set<String> keyset = data.keySet();
					
					/* * statusColumnNo = cell.getColumnIndex();
					 * System.out.println(cell.getStringCellValue());*/
					 
					for (String key : keyset) {
						Row row = sheet.createRow(rownum++);
						Object[] objArr = data.get(key);
						for (Object obj : objArr) {
							Cell cell1 = row.createCell(cellnum++);
							if (obj instanceof String)
								cell1.setCellValue((String) obj);
							else if (obj instanceof Integer)
								cell1.setCellValue((Integer) obj);
						}
					}

				}
			/*}*/
			try {
				
				FileOutputStream out = new FileOutputStream(new File(path));
				workbook.write(out);
				out.close();
				System.out.println(workbook);
			} catch (Exception e) {
				e.printStackTrace();
			}
		
		}
		}
	}