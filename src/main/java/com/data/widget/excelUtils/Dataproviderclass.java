package com.data.widget.excelUtils;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.testng.annotations.DataProvider;

public class Dataproviderclass {

	static String Entertestname = null;
	static int columnsize;

	@DataProvider(name = "data-provider")
	public static Object[][] dataProviderMethod(Method m) throws IOException {

		DataPojoClass pojo1 = null;
		List<Object> adddata = new LinkedList<Object>();
		List<?> listdata = Readerclass.GetData(m.getName());

		columnsize = listdata.size();

		System.out.println(columnsize);
		if (columnsize == 0) {
			System.out.println("this is methosd name======" + m.getName() + "=========NOT FOUND");
		}
		// List rowlist=(List) listdata.get(1);
		// int rowsize=rowlist.size();

		/*--------------------------------------------------------------------------
		 * Have to change the index value based on the excel sheets columns inserted
		 * -------------------------------------------------------------------------
		 */
		Object[][] retObjArr = new Object[columnsize][];

		for (int i = 0; i < columnsize; i++) {
			List<?> list = (List<?>) listdata.get(i);
			pojo1 = new DataPojoClass();
			System.out.println(list.size());
			for (int j = 0; j < list.size(); j++) {
				// System.out.println("Displayed from Data Provider class");
				XSSFCell rollick = (XSSFCell) list.get(j);
				System.out.println(rollick.getStringCellValue());
				if (j == 0) {
					pojo1.setDealerName(rollick.getRichStringCellValue().getString());
				} else if (j == 1) {
					pojo1.setZip(rollick.getRichStringCellValue().getString());
				} else if (j == 2) {
					pojo1.setTelephoneNumber(rollick.getRichStringCellValue().getString());
				} else if (j == 3) {
					pojo1.setStatus(rollick.getRichStringCellValue().getString());
				}	
			}
			adddata.add(pojo1);
		}
		// System.out.println("For loop in Data provider class ");
		System.out.println("Data is adding to Linkedlist");
		

		/*
		 * for(int i=0;i<columnsize;i++){ for(int j=0;j<rowsize;j++){
		 * System.out.print(retObjArr[i][j]); System.out.print(" ");
		 * 
		 * } System.out.println();
		 * 
		 * }
		 */
		// System.out.println("Data provider method in Data provider
		// class");
		return new Object[][] { { adddata } };

	}

}