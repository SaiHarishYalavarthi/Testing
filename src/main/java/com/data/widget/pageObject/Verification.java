package com.data.widget.pageObject;

import javax.swing.text.DefaultEditorKit.CopyAction;

import com.data.widget.excelUtils.DataPojoClass;
import com.data.widget.test.Testsuite;
import com.data.widget.utils.CommonUtil;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.LogStatus;

public class Verification extends Testsuite {
	//static ExtentReports logger = ExtentReports.get(Verification.class);

	public static String linkVerification(DataPojoClass pojo) {
		//logger.startTest(pojo.getDealerName());
		String copy = "empty";
		String temp = pojo.getZip();
		if (temp.length() == 4) {
			temp = "0" + temp;

		}
		
		CommonUtil.clickEnter(elementProperties.getProperty("google.link.input"), pojo.getDealerName() + " " + temp);
		CommonUtil.sleep(1);
		String websiteUrl = null;
		if (CommonUtil.findElementBoolean(elementProperties.getProperty("google.website.button")) == true) {
			websiteUrl = CommonUtil.getLink(elementProperties.getProperty("google.website.button"));
		//	logger.log(LogStatus.PASS, "URL:-" + "<a href=" + websiteUrl + " > " + websiteUrl + " </a>");
			/*CommonUtil.openUrl(websiteUrl);
			CommonUtil.sleep(1);
			String source = CommonUtil.getSource();

			if (source.contains("copyright")) {
				String[] copyrght = source.split("copyright");
				String[] copy1 = copyrght[1].split("/>");
				System.out.println(copy1[0].toString());
				copy = copy1[0].toString();
				logger.log(LogStatus.PASS, copy1[0].toString());
			}*/
		} else {
			//logger.log(LogStatus.WARNING, "Website not Found ");
			websiteUrl = "No website found for this Dealer";
		}
	String retvalue = websiteUrl + "_" + copy;
		return retvalue;
	}
}
