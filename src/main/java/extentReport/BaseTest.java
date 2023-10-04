package extentReport;

import java.io.*;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.collections4.map.HashedMap;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import com.assertthat.selenium_shutterbug.core.Capture;
import com.assertthat.selenium_shutterbug.core.Shutterbug;
import com.assertthat.selenium_shutterbug.utils.file.UnableSaveSnapshotException;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import dataDriven.DataDriven;
import dataDriven.WriteDataExcel;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BaseTest {
	public static WebDriver driver;
	public static String screenshotsSubFolderName;
	public static ExtentReports extentReports;
	public static ExtentTest extentTest;

	DataDriven d = new DataDriven();
	Date date = new Date();
	WriteDataExcel writeExl = new WriteDataExcel();
	String fileDate = date.toString().replace(":", "_").replace(" ", "_");

	@BeforeSuite
	public void initialiseExtentReports() {
		// ADL
		ExtentSparkReporter sparkReporter_all = new ExtentSparkReporter("GuidingCareAssignCareMember.html");
		sparkReporter_all.config().setReportName("Guiding Care: Assign Care Member Report");
		extentReports = new ExtentReports();
		extentReports.attachReporter(sparkReporter_all);

		extentReports.setSystemInfo("OS", System.getProperty("os.name"));
		extentReports.setSystemInfo("Java Version", System.getProperty("java.version"));
		// extentReports.setSystemInfo("Environment", "Test Environment");

	}

	@AfterSuite
	public void generateExtentReports() throws Exception {
		extentReports.flush();
	}

	@Parameters("browserName")
	@BeforeMethod
	public void setup(ITestContext context, @Optional("chrome") String browserName)
			throws IOException, InterruptedException {
		switch (browserName.toLowerCase()) {
		case "chrome":

			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
			driver.manage().window().maximize();

			break;

		case "edge":

			WebDriverManager.edgedriver().setup();
			driver = new EdgeDriver();
			driver.manage().window().maximize();
			break;
		}

		Capabilities capabilities = ((RemoteWebDriver) driver).getCapabilities();
		String device = capabilities.getBrowserName() + " "
				+ capabilities.getBrowserVersion().substring(0, capabilities.getBrowserVersion().indexOf("."));
		// String author = context.getCurrentXmlTest().getParameter("author");

		extentTest = extentReports.createTest(context.getName());
		Markup m = MarkupHelper.createLabel(device.toUpperCase(), ExtentColor.BLUE);

		// extentTest.assignAuthor(author);
		extentTest.assignDevice(device.toUpperCase());

		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	@AfterMethod
	public void checkStatus(Method m, ITestResult result) throws IOException {
		if (result.getStatus() == ITestResult.FAILURE) {
			extentTest.fail(m.getName() + " has failed");
			extentTest.log(Status.FAIL, result.getThrowable(), MediaEntityBuilder
					.createScreenCaptureFromPath(captureScreenshot(m.getName() + fileDate + ".jpg")).build());

		}
		if (result.getStatus() == ITestResult.SKIP) {
			extentTest.skip(m.getName() + " has skipped");
			extentTest.log(Status.SKIP, result.getThrowable(), MediaEntityBuilder
					.createScreenCaptureFromPath(captureScreenshot(m.getName() + fileDate + ".jpg")).build());
		}

		else if (result.getStatus() == ITestResult.SUCCESS) {
			extentTest.pass(m.getName() + " has passed");
		}

		extentTest.assignCategory(m.getAnnotation(Test.class).groups());
		// driver.quit();
	}

	public void login() throws InterruptedException, IOException {
		// Open Login Page
		getLoginPage();
		// Enter Username
		sendKeys("TS002", "TestCase");
		// Enter Password
		sendKeys("TS003", "TestCase");
		// Click Login Button
		clickElement("TS004", "TestCase");
	}

	// Open Assign Care Member Page
	public void openAssignCareMemberPage() throws IOException, InterruptedException {
		// Click Manage Button
		clickElement("TS005", "TestCase");
		// Click on Assigned Manager
		clickElementJSExecutor("TS006", "TestCase");
		// Click on Assigned Status Dropdown
		clickElement("TS007", "TestCase");
		// Select UnAssigned
		clickElement("TS008", "TestCase");
		// Click Enrollment Search
		clickElement("TS009", "TestCase");
		// Type MLTC On Eligibility Level Value
		sendKeys("TS010", "TestCase");
		// Select All CheckBox
		clickElement("TS011", "TestCase");
		// Click Search All
		clickElement("TS012", "TestCase");
	}

	public void assignCareMember() throws InterruptedException, IOException, AWTException {
		Thread.sleep(5000);
		List<String> memberID = new ArrayList<String>();
		List<String> memberName = new ArrayList<String>();
		List<String> gender = new ArrayList<String>();
		List<String> dob = new ArrayList<String>();
		List<String> pLang = new ArrayList<String>();

		String testSheetName = "Sheet1";

		 List<WebElement> element =
		 driver.findElements(By.xpath("(//tbody[@role='rowgroup'])[4]/tr"));
		 System.out.println("ELEMENT SIZE IS: "+ element.size());
		// for (int i = 1; i <= element.size() - 1; i++) {
		 
		for (int i = 1; i <= 1; i++) {
			Thread.sleep(5000);
			
			// Click Member ID
			ArrayList TS013 = d.getData("TS013", "TestCase");
			Actions act = new Actions(driver);
			WebElement memID= driver.findElement(By.xpath("(//tbody[@role='rowgroup'])[4]/tr" + "[" + i + "]" + "/td[17]//..//../td[3]"));
			act.moveToElement(memID).perform();
			Thread.sleep(2000);
			memID.click();
			String log = (String) TS013.get(0) + " " + TS013.get(1);
			extentTest.log(Status.PASS, log,
					MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot((String) TS013.get(0) + i+".jpg")).build());
			
			Thread.sleep(2000);

			//Capture Data for Writing to Excel
			memberName.add(driver.findElement(By.xpath("//p[contains(text(),'Member Name')]/../following-sibling::div"))
					.getText());
			pLang.add(driver.findElement(By.xpath("//p[contains(text(),'Primary Language')]/../following-sibling::div"))
					.getText());
			gender.add(
					driver.findElement(By.xpath("//p[contains(text(),'Gender')]/../following-sibling::div")).getText());
			memberID.add(driver.findElement(By.xpath("//p[contains(text(),'Member ID')]/../following-sibling::div"))
					.getText());
			dob.add(driver.findElement(By.xpath("//p[contains(text(),'Date of Birth')]/../following-sibling::div"))
					.getText());

			ArrayList TS014 = d.getData("TS014", "TestCase");
			String prLang = driver.findElement(By.xpath((String) TS014.get(5))).getText();
			String log2 = (String) TS014.get(0) + " " + TS014.get(1);
			extentTest.log(Status.PASS, log2 + " Primary Language is: " + prLang,
					MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot((String) TS014.get(0) +i+ ".jpg")).build());

			Thread.sleep(2000);
			
			// Close popup
			clickElementJSExecutor("TS015", "TestCase");

			// If Primary Language is English
			if (prLang.contains((String) TS014.get(6))) {

				// Click Checkbox
				ArrayList list = d.getData("TS018", "TestCase");
				WebElement element1 = driver.findElement(By.xpath("(//tbody[@role='rowgroup'])[4]/tr" + "[" + i + "]" + "/td[17]//..//../td[1]"));
				((JavascriptExecutor) driver).executeScript("arguments[0].click();", element1);
//				Thread.sleep(5000);
//				String log1 = (String) list.get(0) + " " + list.get(1);
//				extentTest.log(Status.PASS, log1,
//						MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log1 + ".jpg")).build());
				
				Thread.sleep(5000);
				
				// Type Care Staff Name
				sendKeys("TS016", "TestCase");
				Thread.sleep(5000);
				
				// Select Option
				clickElement("TS017", "TestCase");
				
				// Click Checkbox
				//ArrayList list = d.getData("TS018", "TestCase");
				WebElement checkbox = driver.findElement(By.xpath("(//tbody[@role='rowgroup'])[4]/tr" + "[" + i + "]" + "/td[17]//..//../td[1]"));
				checkbox.click();
				System.out.println("checkbox clicked");
				Thread.sleep(5000);
				String log1 = (String) list.get(0) + " " + list.get(1);
				extentTest.log(Status.PASS, log1,
						MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot((String) list.get(0) + i+".jpg")).build());

				Thread.sleep(5000);
		
				// Click Assign Button
				clickElement("TS019", "TestCase");
				Thread.sleep(5000);

				System.out.println("Care Member for English Language Assigned Successfully");
			}

			else {				
				System.out.println("Primary Language is not English, Care Member Not Assigned");
				extentTest.log(Status.PASS, "Primary Language is not English, Care Member Not Assigned",
						MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot("NotEnglish" + i+".jpg")).build());
			}
		}
		// Write to Excel
		//writeExl.writeIntoExcel(memberID, memberName, gender, dob, pLang, testSheetName);
		System.out.println("MEMBER ID-->" + memberID);
	}

	public void getLoginPage() throws IOException, InterruptedException {
		ArrayList list = d.getData("TS001", "TestCase");
		driver.get((String) list.get(6));
		Thread.sleep(5000);
		String log = (String) list.get(0) + " " + list.get(1);
		extentTest.log(Status.PASS, log,
				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot((String) list.get(0) + ".jpg")).build());
	}

	public void clickElement(String rowNum, String testSheetName) throws IOException, InterruptedException {
		ArrayList list = d.getData(rowNum, testSheetName);
		WebElement element = driver.findElement(By.xpath((String) list.get(5)));
		element.click();
		Thread.sleep(5000);
		String log = (String) list.get(0) + " " + list.get(1);
		extentTest.log(Status.PASS, log,
				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot((String) list.get(0)+ ".jpg")).build());
	}

	public void clickElementJSExecutor(String rowNum, String testSheetName) throws IOException, InterruptedException {
		ArrayList list = d.getData(rowNum, testSheetName);
		WebElement element = driver.findElement(By.xpath((String) list.get(5)));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
		Thread.sleep(5000);
		String log = (String) list.get(0) + " " + list.get(1);
		extentTest.log(Status.PASS, log,
				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot((String) list.get(0) + ".jpg")).build());
	}

	public void sendKeys(String rowNum, String testSheetName) throws IOException {
		ArrayList list = d.getData(rowNum, testSheetName);
		WebElement element = driver.findElement(By.xpath((String) list.get(5)));
		element.sendKeys((String) list.get(6));
		String log = (String) list.get(0) + " " + list.get(1);
		extentTest.log(Status.PASS, log,
				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot((String) list.get(0) + ".jpg")).build());
	}

	// Screenshot
	public String captureScreenshot(String screenShotName) throws IOException {
		// Shutterbug 
		Files.createDirectories(Paths.get(System.getProperty("user.dir") + "/screenshots/"));
		BufferedImage image = Shutterbug.shootPage(driver, Capture.FULL, true).getImage();
		String dest = "./screenshots/" + screenShotName;
		writeImage(image, "PNG", new File(dest));
		return dest;
	}

	public static void writeImage(BufferedImage imageFile, String extension, File fileToWriteTo) {
		try {
			ImageIO.write(imageFile, extension, fileToWriteTo);
		} catch (IOException e) {
			throw new UnableSaveSnapshotException(e);
		}
	}
}
