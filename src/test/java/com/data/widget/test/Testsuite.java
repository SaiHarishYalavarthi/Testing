package com.data.widget.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import com.data.widget.excelUtils.DataPojoClass;
import com.data.widget.excelUtils.DataWriterClass;
import com.data.widget.excelUtils.Dataproviderclass;
import com.data.widget.excelUtils.Readerclass;
import com.data.widget.pageObject.Verification;
import com.data.widget.utils.CommonUtil;
import com.data.widget.utils.PropertiesFileReader;
import com.relevantcodes.extentreports.ExtentReports;

import junit.framework.TestSuite;

public class Testsuite {
	public static Properties elementProperties;
	public static Properties commonProperties;
	public static String url = null;
	public static String sheetName = null;

	static {
		elementProperties = PropertiesFileReader.getInstance().readProperties("element.properties");
		commonProperties = PropertiesFileReader.getInstance().readProperties("common.properties");
	}

	//static ExtentReports logger = ExtentReports.get(TestSuite.class);

	@BeforeTest
	public void Setup() throws FileNotFoundException, IOException {
		//logger.init(CommonUtil.filePath, true);
		CommonUtil.createFolder();

	}

	public static LinkedList<?> dealerurl(LinkedList<?> pojo1) throws Exception {
		DataWriterClass pojoWrite = null;
		//logger.init(CommonUtil.filePath, true);
		CommonUtil.openBrowser("chrome");
		CommonUtil.openUrl("https://www.google.com/");
		LinkedList<Object> adddata = new LinkedList<Object>();
		DataPojoClass pojo = null;
		int count = pojo1.size();
		System.out.println("No of records:- " + " " + count);
		for (int a = 0; a < count; a++) {
			pojo = (DataPojoClass) pojo1.get(a);
			Map<String,Object[]> data1 = null;
			System.out.println(a);
			if (pojo.getStatus().equals("Yes")) {
				url = Verification.linkVerification(pojo);
				String[] urlCopyright = url.split("_");
				int num = a + 1;
				String sno = Integer.toString(num);
				data1 = new TreeMap<String, Object[]>();
				data1.put(sno, new Object[] {pojo.getDealerName(),pojo.getZip(),pojo.getTelephoneNumber(),urlCopyright[0].toString(), urlCopyright[1].toString()});
				pojoWrite = new DataWriterClass();
				pojoWrite.setSNo(sno);
				pojoWrite.setDealerName(pojo.getDealerName());
				pojoWrite.setZip(pojo.getZip());
				pojoWrite.setTelephoneNumber(pojo.getTelephoneNumber());
				pojoWrite.setUrl(urlCopyright[0].toString());
				pojoWrite.setCopyright(urlCopyright[1].toString());
			}
			adddata.add(pojoWrite);
			

		}
		return  adddata;

	}

	@Test(dataProvider = "data-provider", dataProviderClass = Dataproviderclass.class, enabled = true)
	public void dealerTest(LinkedList<?> pojo1) throws Exception {

		LinkedList<?> test = dealerurl(pojo1);
		Readerclass.writeData1(test);

	}

	@AfterTest
	public void terminateBrowser() {
		CommonUtil.quitBrowser();

	}
}
