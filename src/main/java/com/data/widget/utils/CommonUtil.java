package com.data.widget.utils;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.swing.JTextField;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.beust.jcommander.internal.Nullable;

public class CommonUtil {

	public static WebDriver driver = null;
	public static List<String> windowHandlers;
	public static WebElement webElement;
	public static WebElement DragwebElement;
	public static List<WebElement> webelements;
	public static int defaultBrowserTimeOut;
	public static Date printDate = new Date();
	public static String parentWindowHandle = null;
	private static int windowCount = 1;
	public static Properties commonProperties = null;
	static String screenShotPath;
	static String dateData = printDate.toString().replace(':', '_').trim();
	static String folderName = dateData;
	static Connection con = null;

	public static String filePath = System.getProperty("user.dir") + System.getProperty("file.separator") + "Reports"
			+ System.getProperty("file.separator") + "extentreport" + ".html";

	@SuppressWarnings({ "deprecation" })
	public static void openBrowser(String browserType) {

		try {
			if (browserType.equalsIgnoreCase("firefox")) {

				try {
					driver = new FirefoxDriver();
				} catch (Exception e) {

					e.printStackTrace();
				}

			} else if (browserType.equalsIgnoreCase("iexplorer")) {
				System.out.println("iexplorer");
				try {
					System.setProperty("webdriver.ie.driver", "E:/Cisco-2016/IEDriverServer/IEDriverServer.exe");

					DesiredCapabilities caps = DesiredCapabilities.internetExplorer();
					caps.setCapability(InternetExplorerDriver.INITIAL_BROWSER_URL, "");

					driver = new InternetExplorerDriver(caps);

				} catch (Exception e) {

					e.printStackTrace();
				}

			} else if (browserType.equalsIgnoreCase("chrome")) {

				String DriverPath = System.getProperty("user.dir") + System.getProperty("file.separator") + "drivers"
						+ System.getProperty("file.separator") + "chromedriver.exe";
				ChromeOptions options = new ChromeOptions();
				options.addArguments("test-type");
				options.addArguments("start-maximized");
				options.addArguments("--js-flags=--expose-gc");
				options.addArguments("--enable-precise-memory-info");
				options.addArguments("--disable-popup-blocking");
				options.addArguments("--disable-default-apps");
				options.addArguments("test-type=browser");
				options.addArguments("disable-infobars");
				System.setProperty("webdriver.chrome.driver", DriverPath);
				driver = new ChromeDriver(options);
				driver.manage().window().maximize();
			}

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	public static void createFolder() {
		System.out.println(printDate.toString().trim());
		String dateData = printDate.toString().replace(':', '_').trim();
		String folderName = "Execute" + dateData;
		System.out.println(folderName);
		String FolderPath = System.getProperty("user.dir") + System.getProperty("file.separator") + "ScreenShots";
		screenShotPath = FolderPath + "/" + folderName;

		File file = new File(screenShotPath);
		file.mkdir();
		// return screenShotPath;
	}

	public static String TakeScreenShot(String methodName) {
		File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		String screenpath = screenShotPath;
		String screenshotname = null;
		try {

			// now copy the screenshot to desired location using copyFile
			// //method
			FileUtils.copyFile(src, new File(screenpath + "/" + methodName + ".png"));
			screenshotname = screenpath + "/" + methodName + ".png";
		}

		catch (IOException e) {
			System.out.println(e.getMessage());

		}
		// String screenpath;
		return screenshotname;
	}

	public static void windowmaximize() {
		driver.manage().window().maximize();

	}

	public static String getTextFromTextBox(String locator) {
		WebElement element;
		String text = "NO VALUE RETRIVED";
		try {
			element = findElement(locator);

			text = element.getAttribute("value").trim();
			// System.out.println(" Element text :::" + text);
		} catch (Exception e) {
		}
		element = null;
		return text;
	}

	// Click the html element
	public static void clickWebElement(String locator) {
		WebElement element;
		try {
			Actions a = new Actions(driver);
			element = findElement(locator);
			/* WebElement field=CommonUtil.findElement(locator); */
			a.moveToElement(element).click().perform();
			System.out.println("Element is successfully clicked");
		} catch (Exception e) {
		}
	}

	public static boolean isElementPrsent(String locator) {

		WebElement element = CommonUtil.findElement(locator);
		if (element.isDisplayed()) {

			return true;
		} else {

			return false;
		}
	}

	public static boolean isElementEnabled(String locator) {

		WebElement element = CommonUtil.findElement(locator);
		if (element.isEnabled()) {

			return true;
		} else {

			return false;
		}
	}

	public static void moveToElement(String locator) {
		WebElement element;
		try {
			Actions a = new Actions(driver);
			element = findElement(locator);

			/* WebElement field=CommonUtil.findElement(locator); */
			a.moveToElement(element).build().perform();
			System.out.println("Moved to element Successfully");
			sleep(1);
		} catch (Exception e) {

		}
	}

	public static void closeBrowser() {
		if (driver != null)
			driver.close();
	}

	public static void quitBrowser() {
		if (driver != null)
			driver.quit();
	}

	public static boolean openUrl(String url) {

		try {
			if (driver != null)
				System.out.println(url);
			if (clearCache() == true) {
				driver.get(url);
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {

			e.printStackTrace();
			return false;
		}
	}

	public static String getUrl() {
		String currentUrl = null;
		try {
			if (driver != null) {
				currentUrl = driver.getCurrentUrl();
			} else {
				System.out.println("Driver is not Found");
			}
		} catch (Exception e) {

			e.printStackTrace();

		}
		return currentUrl;

	}

	public static void logs(String url) {
		driver.manage().logs().get(LogType.PROFILER).getAll();
		List<LogEntry> entries = driver.manage().logs().get(LogType.PROFILER).getAll();
		System.out.println(entries.size() + " " + LogType.PROFILER + " log entries found");
		for (LogEntry entry : entries) {
			System.out.println(new Date(entry.getTimestamp()) + " " + entry.getLevel() + " " + entry.getMessage());
		}
	}

	public static boolean clearCache() {
		try {
			if (driver != null)
				driver.manage().deleteAllCookies();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static String getTitle() {
		String text = null;
		try {

			if (driver != null)
				text = driver.getTitle();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return text;
	}

	public static String getSource() {
		String source = null;
		try {

			if (driver != null)
				source = driver.getPageSource();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return source;
	}

	public static void refreshPage() {
		try {
			if (driver != null)
				driver.navigate().refresh();
			sleep(2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void waitForPageToStartLoading() {
		try {
			Object result = ((JavascriptExecutor) driver).executeScript("return document['readyState']");
			int iCount = 0;
			while (!result.toString().equalsIgnoreCase("loading") && iCount < 10) {
				Thread.sleep(1000);
				result = ((JavascriptExecutor) driver).executeScript("return document['readyState']");
				iCount++;
			}
		} catch (Exception e) {
			System.out.println("Exception from page load : " + e.getMessage());
		}
	}

	public static void waitForPageload() {

		try {

			Object result = ((JavascriptExecutor) driver).executeScript("return document['readyState']");
			int iCount = 0;
			while (!result.toString().equalsIgnoreCase("compStringe") && iCount < 10) {

				sleep(5);
				result = ((JavascriptExecutor) driver).executeScript("return document['readyState']");
				iCount++;
				System.out.println("Browser Status in While Loop::" + result.toString() + "::");
			}
			System.out.println("Page Load is compStringed");
		} catch (Exception e) {
			System.out.println("Exception from page load : " + e.getMessage());
		}
	}

	public static void enterText(String field, String value) {

		try {
			webElement = findElement(field);
			if (webElement.isDisplayed()) {
				webElement.clear();
				webElement.sendKeys(value);
			}
		} catch (NoSuchElementException e) {
			System.out.println("Error occured whlie entering the value into textbox" + value + " *** "
					+ getErroMsg(e.getMessage()));
			String error = "Error occured whlie entering the value into textbox:" + value;
			error = error.replaceAll("'", "\"");

			e.printStackTrace();

		} catch (Exception e) {
			System.out.println(
					"Error occured whlie pressing the enter key" + value + " *** " + getErroMsg(e.getMessage()));
			String error = "Error occured whlie pressing the enter";
			error = error.replaceAll("'", "\"");

			e.printStackTrace();

		}
	}

	public static void clickEnter(String field, String value) {

		try {
			webElement = findElement(field);
			if (webElement.isDisplayed()) {
				webElement.clear();
				webElement.sendKeys(value, Keys.ENTER);
			}
		} catch (Exception e) {
			System.out.println(
					"Error occured whlie pressing the enter key" + value + " *** " + getErroMsg(e.getMessage()));
			String error = "Error occured whlie pressing the enter";
			error = error.replaceAll("'", "\"");

			e.printStackTrace();

		}
	}

	public static void ScrollByUseingElement(String locator) {
		try {
			String[] arrLocator = locator.split("#");
			// String locatorTag = arrLocator[0].trim();
			String objectLocator = arrLocator[1].trim();
			JavascriptExecutor je = (JavascriptExecutor) driver;
			// webElement = findElement(locator);
			WebElement element = driver.findElement(By.xpath(objectLocator));
			je.executeScript("arguments[0].scrollIntoView(true);", element);
		} catch (Exception e) {

		}
	}

	public static void ScrollByCoordinates(int x) {
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("scroll(0," + x + ");");
	}

	public static void scrollDown() {

		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("scroll(0, 250);");
	}

	public static void scrollDownFull() {

		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("scroll(0, 950);");
	}

	public static void centerScrollDown() {

		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("scroll(0, 450);");
	}

	public static void scrollUp() {

		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("scroll(0, -150);");
	}

	// Using VisibleText
	public static void selectValueFromDropDownBox(String field, String value) {
		try {
			webElement = findElement(field);
			if (webElement.isDisplayed()) {
				Select select = new Select(webElement);
				select.selectByVisibleText(value);
			}
		} catch (Exception e) {
			System.out.println("Error occured whlie select the value from dropdownbox " + value + " *** "
					+ getErroMsg(e.getMessage()));
			String error = "Error occured whlie select the value from dropdownbox :" + value;
			error = error.replaceAll("'", "\"");

			e.printStackTrace();

		}
	}

	public static int countOfItemsInDropdown(String field) {
		int count = 0;
		try {
			webElement = findElement(field);
			if (webElement.isDisplayed()) {
				Select select = new Select(webElement);
				List<WebElement> options = select.getOptions();
				count = options.size();
				System.out.println("Number of items in Dropdown are: " + count);
			}
		} catch (Exception e) {
			System.out.println(
					"Error occured while getting the count of items from dropdownbox " + getErroMsg(e.getMessage()));
			String error = "Error occured while getting the count of items from dropdownbox  :";
			error = error.replaceAll("'", "\"");

			e.printStackTrace();
		}
		return count;
	}

	// Using Value
	public static void selectValueFromDropDownBoxUsingValue(String field, String value) {
		try {
			webElement = findElement(field);
			if (webElement.isDisplayed()) {
				Select select = new Select(webElement);
				select.selectByValue(value);
			}
		} catch (Exception e) {
			System.out.println("Error occured whlie select the value from dropdownbox " + value + " *** "
					+ getErroMsg(e.getMessage()));
			String error = "Error occured whlie select the value from dropdownbox :" + value;
			error = error.replaceAll("'", "\"");

			e.printStackTrace();

		}
	}

	public static String email() {
		String value = null;
		try {
			printDate = new Date();
			;
			String date = printDate.toString();
			String date_value = date.replace(":", "");
			System.out.println(date_value);
			String[] temp = date_value.split(" ");
			// System.out.println(temp[1] + temp[3] + temp[5] + "_" + " test
			// this");
			value = temp[1] + temp[2] + temp[3] + temp[5];
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

	public static boolean isAlertPresent() {

		try {
			driver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException Ex) {
			// Ex.printStackTrace();
			return false;
		}
	}

	// It will wait seconds
	public static void sleep(int time) {

		try {
			Thread.sleep(time * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void click(String field) {
		try {
			WebElement element = findElement(field);
			if (element != null) {
				element.click();
			}
		} catch (Exception e) {
			System.out.println(
					"Error occured whlie click on the elements " + field + " *** " + getErroMsg(e.getMessage()));
			String error = "Error occured whlie click on the element :" + field;
			error = error.replaceAll("'", "\"");

			e.printStackTrace();

		}
	}

	public static void longClick(String field, int time) {
		try {
			WebElement element = findElement(field);
			if (element != null)
				element.click();
			sleep(time);
		} catch (Exception e) {
			System.out.println(
					"Error occured whlie click on the element " + field + " *** " + getErroMsg(e.getMessage()));
			String error = "Error occured whlie click on the element :" + field;
			error = error.replaceAll("'", "\"");

			e.printStackTrace();

		}
	}

	public static void isChecked(String field) {

		try {
			WebElement element = findElement(field);
			if (element != null)

				if (!element.isSelected()) {
					element.click();
				}

		} catch (Exception e) {
			System.out.println(
					"Error occured whlie selecting the check box " + field + " *** " + getErroMsg(e.getMessage()));
			String error = "Error occured whlie selecting the check box :" + field;
			error = error.replaceAll("'", "\"");

			e.printStackTrace();

		}

	}

	public static void isUnChecked(String field) {

		try {
			WebElement element = findElement(field);
			if (element != null)

				if (element.isSelected()) {
					element.click();
				}

		} catch (Exception e) {
			System.out.println(
					"Error occured whlie unchecking  the check box " + field + " *** " + getErroMsg(e.getMessage()));
			String error = "Error occured whlie unchecking  the check box :" + field;
			error = error.replaceAll("'", "\"");

			e.printStackTrace();

		}

	}

	public static String getText(String locator) {
		WebElement element;
		String text = "NO VALUE RETRIVED";
		try {
			element = findElement(locator);
			if (element != null)
				text = element.getText().trim();
		} catch (Exception e) {
			e.printStackTrace();
		}
		element = null;
		return text;
	}

	public static String getLink(String locator) {
		WebElement element;
		String text = "NO VALUE RETRIVED";
		try {
			element = findElement(locator);
			if (element != null)
				text = element.getAttribute("href");
		} catch (Exception e) {
			e.printStackTrace();
		}
		element = null;
		return text;
	}

	public static String getTextUsingXpath(String locator, int i) {
		WebElement element;
		/* System.out.println("HI"); */
		String text = "NO VALUE RETRIVED";
		String[] loc = locator.split("\\[" + "\\[" + "ChromeDriver:");
		System.out.println(loc.length);
		String[] arrLocator = loc[i].split("xpath:");
		System.out.println(arrLocator.length);
		// String locatorTag = arrLocator[0].trim();
		String objectLocator = arrLocator[1].trim();
		String temp = objectLocator.substring(0, objectLocator.length() - 2);
		String actLocator = "xpath#" + temp + "[" + i + "]";

		try {
			element = forEachfindElement(actLocator);
			if (element != null)
				text = element.getText().trim();
		} catch (Exception e) {
			e.printStackTrace();
		}
		element = null;
		return text;
	}

	public static String getAttribute(String locator, String attribute) {
		WebElement element;
		String text = "NO VALUE RETRIVED";
		try {
			element = findElement(locator);
			if (element != null)
				text = element.getAttribute(attribute).trim();
		} catch (Exception e) {
		}
		element = null;
		return text;

	}

	/*
	 * This method will return true when the radio button or check box is
	 * selected. return : boolean
	 */
	public static boolean isElementSelected(String locator) {
		WebElement element = findElement(locator);
		if (element.isDisplayed()) {
			return true;
		} else {
			return false;
		}
	}

	public static String getSelectedDropDownValue(String dropDownLocator) {
		String selectedValue = "";
		try {
			WebElement element = findElement(dropDownLocator);
			Select selectBox = new Select(element);
			selectedValue = selectBox.getFirstSelectedOption().getText();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return selectedValue;
	}

	public static WebElement findElement(String locator) {

		if (locator != null) {

			String[] arrLocator = locator.split("#");
			String locatorTag = arrLocator[0].trim();
			String objectLocator = arrLocator[1].trim();
			try {
				if (locatorTag.equalsIgnoreCase("id")) {
					webElement = driver.findElement(By.id(objectLocator));
				} else if (locatorTag.equalsIgnoreCase("name")) {
					webElement = driver.findElement(By.name(objectLocator));
				} else if (locatorTag.equalsIgnoreCase("xpath")) {
					webElement = driver.findElement(By.xpath(objectLocator));
				} else if (locatorTag.equalsIgnoreCase("linkText")) {
					webElement = driver.findElement(By.linkText(objectLocator));
				} else if (locatorTag.equalsIgnoreCase("class")) {
					webElement = driver.findElement(By.className(objectLocator));
				} else {
					String error = "Please Check the Given Locator Syntax :" + locator;
					System.out.println("Please Check the Given Locator Syntax : " + locator);
					error = error.replaceAll("'", "\"");

					return null;
				}
			} catch (Exception exception) {
				String error = "Please Check the Given Locator Syntax :" + locator;
				error = error.replaceAll("'", "\"");

				exception.printStackTrace();

				return null;
			}
		}
		return webElement;
	}

	public static Boolean findElementBoolean(String locator) {

		if (locator != null) {

			String[] arrLocator = locator.split("#");
			String locatorTag = arrLocator[0].trim();
			String objectLocator = arrLocator[1].trim();
			try {
				if (locatorTag.equalsIgnoreCase("id")) {
					webElement = driver.findElement(By.id(objectLocator));
				} else if (locatorTag.equalsIgnoreCase("name")) {
					webElement = driver.findElement(By.name(objectLocator));
				} else if (locatorTag.equalsIgnoreCase("xpath")) {
					webElement = driver.findElement(By.xpath(objectLocator));
				} else if (locatorTag.equalsIgnoreCase("linkText")) {
					webElement = driver.findElement(By.linkText(objectLocator));
				} else if (locatorTag.equalsIgnoreCase("class")) {
					webElement = driver.findElement(By.className(objectLocator));
				} else {
					String error = "Please Check the Given Locator Syntax :" + locator;
					System.out.println("Please Check the Given Locator Syntax : " + locator);
					error = error.replaceAll("'", "\"");

					return null;
				}
			} catch (Exception exception) {
				String error = "Please Check the Given Locator Syntax :" + locator;
				error = error.replaceAll("'", "\"");

				exception.printStackTrace();

				return false;
			}
		}
		return true;
	}

	/* For For Each Method verifiation */

	public static WebElement forEachfindElement(String locator) {

		if (locator != null) {

			String[] arrLocator = locator.split("#");
			String locatorTag = arrLocator[0].trim();
			String objectLocator = arrLocator[1].trim();
			try {
				if (locatorTag.contains("id")) {
					webElement = driver.findElement(By.id(objectLocator));
				} else if (locatorTag.contains("name")) {
					webElement = driver.findElement(By.name(objectLocator));
				} else if (locatorTag.contains("xpath")) {
					webElement = driver.findElement(By.xpath(objectLocator));
				} else if (locatorTag.contains("linkText")) {
					webElement = driver.findElement(By.linkText(objectLocator));
				} else if (locatorTag.contains("class")) {
					webElement = driver.findElement(By.className(objectLocator));
				} else {
					String error = "Please Check the Given Locator Syntax :" + locator;
					System.out.println("Please Check the Given Locator Syntax : " + locator);
					error = error.replaceAll("'", "\"");

					return null;
				}
			} catch (Exception exception) {
				String error = "Please Check the Given Locator Syntax :" + locator;
				error = error.replaceAll("'", "\"");

				exception.printStackTrace();

				return null;
			}
		}
		return webElement;
	}

	public static List<WebElement> findElements(String locator) {

		if (locator != null) {
			String[] arrLocator = locator.split("#");
			String locatorTag = arrLocator[0].trim();
			String objectLocator = arrLocator[1].trim();

			if (locatorTag.equalsIgnoreCase("id")) {
				webelements = driver.findElements(By.id(objectLocator));
			} else if (locatorTag.equalsIgnoreCase("name")) {
				webelements = driver.findElements(By.name(objectLocator));
			} else if (locatorTag.equalsIgnoreCase("xpath")) {
				webelements = driver.findElements(By.xpath(objectLocator));
			} else if (locatorTag.equalsIgnoreCase("linkText")) {
				webelements = driver.findElements(By.linkText(objectLocator));
			} else if (locatorTag.equalsIgnoreCase("class")) {
				webelements = driver.findElements(By.className(objectLocator));
			} else {
				System.out.println("Please Check the Locator Syntax Given :" + locator);
				return null;
			}
		}
		return webelements;
	}

	public static void multipleSelectionInDropdown(String locator) {
		if (locator != null) {
			String[] arrLocator = locator.split("#");
			String locatorTag = arrLocator[0].trim();
			String objectLocator = arrLocator[1].trim();
		}
		int count = countOfItemsInDropdown("locator");
		for (int i = 0; i < count; i++) {

		}

	}

	public static void closeCriticalAlert(String criticalAlert) {

		WebElement webElement = findElement(criticalAlert);
		if (webElement.isDisplayed()) {

			driver.findElement(By.xpath(".//*[@id='ok']")).click();
		}
	}

	public static void switchWindow() {

		waitForWindowToAppear();
		String mainWindowHandle = driver.getWindowHandle();
		Set<?> sWindows = driver.getWindowHandles();
		Iterator<?> ite = sWindows.iterator();
		while (ite.hasNext()) {
			String popupHandle = ite.next().toString();
			if (!popupHandle.contains(mainWindowHandle)) {
				driver.switchTo().window(popupHandle);
			}
		}
		waitForPageload();
	}

	public static void switchWindows() {
		sleep(2);
		for (String winHandle : driver.getWindowHandles()) {
			driver.switchTo().window(winHandle);
		}
	}

	public static String getErroMsg(String msg) {

		int a = msg.indexOf("(");
		return msg.substring(0, a);
	}

	public static void navigateUrl(String url) {

		try {
			driver.get(url);
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	public static int getNumberOfRowsInTable(String tableId) {
		// Grab the table
		WebElement table = findElement(tableId);
		// Now get all the TR elements from the table
		List<WebElement> allRows = table.findElements(By.tagName("tr"));
		System.out.println("NUMBER OF ROWS IN THIS TABLE = " + allRows.size());
		return allRows.size();

	}

	public static void handleAlert() {
		try {
			driver.switchTo().alert().accept();
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	public static String getNumbersFromString(String text) {
		Pattern p = Pattern.compile("\\d+");
		Matcher m = p.matcher(text);
		while (m.find()) {
			return m.group().trim();
		}

		return null;
	}

	public static void switchToOriginalWindow() {
		// String s1 = getParentWindow();
		driver.switchTo().window(getParentWindow());
	}

	public static void parseChars(String Stringter, Robot robot) {
		for (int i = 0; i < Stringter.length(); i++) {
			char chary = Stringter.charAt(i);
			typeCharacter(Character.toString(chary), robot);
		}
	}

	public static void typeCharacter(String Stringter, Robot robot) {

		if (Character.isLetterOrDigit(Stringter.charAt(0))) {
			try {
				boolean upperCase = Character.isUpperCase(Stringter.charAt(0));
				String variableName = "VK_" + Stringter.toUpperCase();
				KeyEvent ke = new KeyEvent(new JTextField(), 0, 0, 0, 0, ' ');
				Class<? extends KeyEvent> clazz = ke.getClass();
				Field field = clazz.getField(variableName);
				int keyCode = field.getInt(ke);
				robot.delay(80);
				if (upperCase)
					robot.keyPress(KeyEvent.VK_SHIFT);
				robot.keyPress(keyCode);
				robot.keyRelease(keyCode);
				if (upperCase)
					robot.keyRelease(KeyEvent.VK_SHIFT);
			} catch (Exception e) {
				System.out.println(e);
			}
		} else {
			if (Stringter.equals("!")) {
				robot.keyPress(KeyEvent.VK_SHIFT);
				robot.keyPress(KeyEvent.VK_1);
				robot.keyRelease(KeyEvent.VK_1);
				robot.keyRelease(KeyEvent.VK_SHIFT);
			} else if (Stringter.equals("@")) {
				robot.keyPress(KeyEvent.VK_SHIFT);
				robot.keyPress(KeyEvent.VK_2);
				robot.keyRelease(KeyEvent.VK_2);
				robot.keyRelease(KeyEvent.VK_SHIFT);
			} else if (Stringter.equals("#")) {
				robot.keyPress(KeyEvent.VK_SHIFT);
				robot.keyPress(KeyEvent.VK_3);
				robot.keyRelease(KeyEvent.VK_3);
				robot.keyRelease(KeyEvent.VK_SHIFT);
			} else if (Stringter.equals("#")) {
				robot.keyPress(KeyEvent.VK_SHIFT);
				robot.keyPress(KeyEvent.VK_3);
				robot.keyRelease(KeyEvent.VK_3);
				robot.keyRelease(KeyEvent.VK_SHIFT);
			} else if (Stringter.equals("$")) {
				robot.keyPress(KeyEvent.VK_SHIFT);
				robot.keyPress(KeyEvent.VK_4);
				robot.keyRelease(KeyEvent.VK_4);
				robot.keyRelease(KeyEvent.VK_SHIFT);
			} else if (Stringter.equals("%")) {
				robot.keyPress(KeyEvent.VK_SHIFT);
				robot.keyPress(KeyEvent.VK_5);
				robot.keyRelease(KeyEvent.VK_5);
				robot.keyRelease(KeyEvent.VK_SHIFT);
			} else if (Stringter.equals("^")) {
				robot.keyPress(KeyEvent.VK_SHIFT);
				robot.keyPress(KeyEvent.VK_6);
				robot.keyRelease(KeyEvent.VK_6);
				robot.keyRelease(KeyEvent.VK_SHIFT);
			} else if (Stringter.equals("&")) {
				robot.keyPress(KeyEvent.VK_SHIFT);
				robot.keyPress(KeyEvent.VK_7);
				robot.keyRelease(KeyEvent.VK_7);
				robot.keyRelease(KeyEvent.VK_SHIFT);
			} else if (Stringter.equals("*")) {
				robot.keyPress(KeyEvent.VK_SHIFT);
				robot.keyPress(KeyEvent.VK_8);
				robot.keyRelease(KeyEvent.VK_8);
				robot.keyRelease(KeyEvent.VK_SHIFT);
			} else if (Stringter.equals("=")) {
				robot.keyPress(KeyEvent.VK_EQUALS);
				robot.keyRelease(KeyEvent.VK_EQUALS);
			} else if (Stringter.equals(" ")) {
				robot.keyPress(KeyEvent.VK_SPACE);
				robot.keyRelease(KeyEvent.VK_SPACE);
			} else if (Stringter.equals("/")) {
				robot.keyPress(KeyEvent.VK_BACK_SLASH);
				robot.keyRelease(KeyEvent.VK_BACK_SLASH);
			} else if (Stringter.equals("\\")) {
				robot.keyPress(KeyEvent.VK_BACK_SLASH);
				robot.keyRelease(KeyEvent.VK_BACK_SLASH);
			} else if (Stringter.equals("_")) {
				robot.keyPress(KeyEvent.VK_SHIFT);
				robot.keyPress(KeyEvent.VK_MINUS);
				robot.keyRelease(KeyEvent.VK_MINUS);
				robot.keyRelease(KeyEvent.VK_SHIFT);
			} else if (Stringter.equals(":")) {
				robot.keyPress(KeyEvent.VK_SHIFT);
				robot.keyPress(KeyEvent.VK_SEMICOLON);
				robot.keyRelease(KeyEvent.VK_SEMICOLON);
				robot.keyRelease(KeyEvent.VK_SHIFT);
			} else if (Stringter.equals(";")) {
				robot.keyPress(KeyEvent.VK_SEMICOLON);
				robot.keyRelease(KeyEvent.VK_SEMICOLON);
			} else if (Stringter.equals(",")) {
				robot.keyPress(KeyEvent.VK_COMMA);
				robot.keyRelease(KeyEvent.VK_COMMA);
			} else if (Stringter.equals("?")) {
				robot.keyPress(KeyEvent.VK_SHIFT);
				robot.keyPress(KeyEvent.VK_SLASH);
				robot.keyRelease(KeyEvent.VK_SLASH);
				robot.keyRelease(KeyEvent.VK_SHIFT);
			} else if (Stringter.equals(" ")) {
				robot.keyPress(KeyEvent.VK_SPACE);
				robot.keyRelease(KeyEvent.VK_SPACE);
			} else if (Stringter.equals(".")) {
				robot.keyPress(KeyEvent.VK_PERIOD);
				robot.keyRelease(KeyEvent.VK_PERIOD);
			}

		}
	}

	public static void selectMouseOverOption(String field) {

		Actions action = new Actions(driver);
		WebElement element = CommonUtil.findElement(field);
		action.moveToElement(element).perform();
	}

	public static String getParentWindow() {
		String main_window = driver.getWindowHandle();
		return main_window;
	}

	public static void switchToNewWindow() {
		for (String winHandle : driver.getWindowHandles()) {
			driver.switchTo().window(winHandle); // switch focus of WebDriver to
													// the next found window
													// handle (that's your newly
													// opened window)
		}
	}

	public static void switchToFrame(String locator) {
		WebElement element = CommonUtil.findElement(locator);
		driver.switchTo().frame(element);
	}

	public static void switchToFrame() {
		driver.switchTo().frame(0);
	}

	public static void doubleclick(String field) {
		webElement = CommonUtil.findElement(field);
		Actions builder = new Actions(driver);
		Action doubleClick = builder.doubleClick(webElement).build();
		doubleClick.perform();
	}

	public static void DragAndDrop(String DragXpath, String DropXpath) {
		WebElement DropWebElement;
		DragwebElement = CommonUtil.findElement(DragXpath);
		DropWebElement = CommonUtil.findElement(DropXpath);
		Actions dragAndDrop = new Actions(driver);
		dragAndDrop.dragAndDrop(DragwebElement, DropWebElement).build().perform();
	}

	/*
	 * public static void closeAllBrowsers(){ driver.quit(); }
	 */
	public static void waitForWindowToAppear() {

		for (int j = 0; j <= 15; j++) {

			int noOfWindows = driver.getWindowHandles().size();

			if (windowCount != noOfWindows) {

				windowCount = noOfWindows;
				System.out.println("window count :" + windowCount);
				/*
				 * for (String winHandle : driver.getWindowHandles()) {
				 * driver.switchTo().window(winHandle); }
				 */
				break;
			} else {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public static void waitForPageLoaded() {
		ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver).executeScript("return document.readyState").toString()
						.equals("compStringe");
			}
		};
		try {
			Thread.sleep(1000);
			WebDriverWait wait = new WebDriverWait(driver, 30);
			wait.until(expectation);
		} catch (Throwable error) {
			// Assert.fail("Timeout waiting for Page Load Request to
			// compStringe.");
		}
	}

	public static void getDimensions() {
		System.out.println("*********Height********" + driver.manage().window().getSize().getHeight());
		System.out.println("*********Weight********" + driver.manage().window().getSize().getWidth());
	}

	@SuppressWarnings("unused")
	public static boolean waitForLoadElement(String field) {
		WebElement element = null;
		String objectLocator = null;
		if (field != null) {

			String[] arrLocator = field.split("#");
			String locatorTag = arrLocator[0].trim();
			objectLocator = arrLocator[1].trim();
			try {
				if (locatorTag.equalsIgnoreCase("id")) {
					element = (new WebDriverWait(driver, 10))
							.until(ExpectedConditions.visibilityOfElementLocated(By.id(objectLocator)));
				} else if (locatorTag.equalsIgnoreCase("name")) {
					element = (new WebDriverWait(driver, 10))
							.until(ExpectedConditions.visibilityOfElementLocated(By.name(objectLocator)));
				} else if (locatorTag.equalsIgnoreCase("xpath")) {
					element = (new WebDriverWait(driver, 10))
							.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(objectLocator)));
				} else if (locatorTag.equalsIgnoreCase("linkText")) {
					element = (new WebDriverWait(driver, 10))
							.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(objectLocator)));
				} else if (locatorTag.equalsIgnoreCase("class")) {
					element = (new WebDriverWait(driver, 10))
							.until(ExpectedConditions.visibilityOfElementLocated(By.className(objectLocator)));
				}
			} catch (Exception e) {
				return false;
			}
			return true;
		}
		if (true) {
			return true;
		} else
			return false;
	}

	// @SuppressWarnings("null")
	private static int[] roMakeCRCTable() {
		int c;
		int crcTable[] = new int[256];
		for (int n = 0; n < 256; n++) {
			c = n;
			for (int k = 0; k < 8; k++) {
				// tslint:disable-next-line:no-bitwise
				if (c != 0) {
					c = (0xEDB88320 ^ (c >>> 1));
				} else {
					c = (c >>> 1);
				}
				// c = ((c) ? (0xEDB88320 ^ (c >>> 1)) : (c >>> 1));
			}
			crcTable[n] = c;
		}
		return crcTable;
	}

	public static int roCrc32(String value) {
		int[] crcTable = roMakeCRCTable();
		// tslint:disable-next-line:no-bitwise
		int crc = 0 ^ (-1);
		for (int i = 0; i < value.length(); i++) {
			// tslint:disable-next-line:no-bitwise
			crc = (crc >>> 8) ^ crcTable[(crc ^ value.charAt(i)) & 0xFF];
		}
		// tslint:disable-next-line:no-bitwise
		return (crc ^ (-1)) >>> 0;
	}

	/*
	 * private static String roMakeCRCTable() { // TODO Auto-generated method
	 * stub return null; }
	 */

	/*
	 * public static Object runDisplay(String value) throws ScriptException,
	 * javax.script.ScriptException {
	 * 
	 * @SuppressWarnings("restriction") ScriptEngineManager manager = new
	 * ScriptEngineManager(); ScriptEngine engine =
	 * manager.getEngineByName("javascript"); // ScriptEngine engine = new //
	 * ScriptEngineManager().getEngineByName("nashorn"); try { String path =
	 * System.getProperty("user.dir") + System.getProperty("file.separator") +
	 * "src\\main\\java\\com\\S4\\Regression\\Utils\\Script.js";
	 * System.out.println(path); //
	 * "D:\Rollick_Automation\S4_workSpace\S4_Regression\src\main\java\com\S4\Regression\Utils\Script.js";
	 * engine.eval(new FileReader(path)); Invocable invocable = (Invocable)
	 * engine; Object result; result = invocable.invokeFunction("roCrc32",
	 * value); System.out.println(result);
	 * System.out.println(result.getClass()); return result; } catch (Exception
	 * e) { e.printStackTrace(); } return null; }
	 */

	public Object executeJs(String js, @Nullable String roCrc32, Object... args) {
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("javascript");
		try {
			Object res = engine.eval(js);
			/* if(StringUtils.toString()){ */
			if (engine instanceof Invocable) {
				Invocable invoke = (Invocable) engine;
				res = invoke.invokeFunction(roCrc32, args);
			}
			/* } */
			return res;
		} catch (Exception e) {

		}
		return null;
	}

}
// End Class