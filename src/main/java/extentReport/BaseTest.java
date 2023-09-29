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
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

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
	String fileDate = date.toString().replace(":", "_").replace(" ", "_");

	@BeforeSuite
	public void initialiseExtentReports() {
		// ADL
		ExtentSparkReporter sparkReporter_all = new ExtentSparkReporter("GuidingCareMemberData.html");
		sparkReporter_all.config().setReportName("Guiding Care: Capture Member Data Values");
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
		driver.quit();
	}

	public void login() throws InterruptedException, IOException {



		// Open Login Page
		ArrayList TS001 = d.getData("TS001", "BT");
		driver.get((String) TS001.get(6));
		Thread.sleep(5000);
		String log1 = (String) TS001.get(0) + " " + TS001.get(1);
		extentTest.log(Status.PASS, log1,
				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log1 + ".jpg")).build());

		// Enter Username
		ArrayList TS002 = d.getData("TS002", "BT");
		WebElement userName = driver.findElement(By.xpath((String) TS002.get(5)));
		userName.sendKeys((String) TS002.get(6));
		String log2 = (String) TS002.get(0) + " " + TS002.get(1);
		extentTest.log(Status.PASS, log2,
				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log2 + ".jpg")).build());

		// Enter Password
		ArrayList TS003 = d.getData("TS003", "BT");
		WebElement pwd = driver.findElement(By.xpath((String) TS003.get(5)));
		pwd.sendKeys((String) TS003.get(6));
		String log3 = (String) TS003.get(0) + " " + TS003.get(1);
		extentTest.log(Status.PASS, log3,
				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log2 + ".jpg")).build());

		// Click Login Button
		ArrayList TS004 = d.getData("TS004", "BT");
		WebElement signIn = driver.findElement(By.xpath((String) TS004.get(5)));

		signIn.click();
		Thread.sleep(5000);
		String log4 = (String) TS004.get(0) + " " + TS004.get(1);
		extentTest.log(Status.PASS, log4,
				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log4 + ".jpg")).build());

		// Click Manage Button
		ArrayList TS005 = d.getData("TS005", "BT");
		WebElement manageButton = driver.findElement(By.xpath((String) TS005.get(5)));
		manageButton.click();
		Thread.sleep(5000);
		String log5 = (String) TS005.get(0) + " " + TS005.get(1);
		extentTest.log(Status.PASS, log5,
				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log5 + ".jpg")).build());

		// Click on Assigned Manager
		ArrayList TS006 = d.getData("TS006", "BT");
		WebElement assignedManagerButton = driver.findElement(By.xpath((String) TS006.get(5)));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", assignedManagerButton);
		//assignedManagerButton.click();
		Thread.sleep(5000);
		String log6 = (String) TS006.get(0) + " " + TS006.get(1);
		extentTest.log(Status.PASS, log6,
				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log6 + ".jpg")).build());

		// Click on Assigned Status Dropdown
		ArrayList TS007 = d.getData("TS007", "BT");
		WebElement assignedStatusDropdown = driver.findElement(By.xpath((String) TS007.get(5)));
		assignedStatusDropdown.click();
		Thread.sleep(5000);
		String log7 = (String) TS007.get(0) + " " + TS007.get(1);
		extentTest.log(Status.PASS, log7,
				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log7 + ".jpg")).build());

		// Select UnAssigned
		ArrayList TS008 = d.getData("TS008", "BT");
		WebElement unassigned = driver.findElement(By.xpath((String) TS008.get(5)));
		unassigned.click();
		Thread.sleep(5000);
		String log8 = (String) TS008.get(0) + " " + TS008.get(1);
		extentTest.log(Status.PASS, log8,
				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log8 + ".jpg")).build());

		// Click Enrollment Search
		ArrayList TS009 = d.getData("TS009", "BT");
		WebElement enrollmentSearch = driver.findElement(By.xpath((String) TS009.get(5)));
		enrollmentSearch.click();
		Thread.sleep(5000);
		String log9 = (String) TS009.get(0) + " " + TS009.get(1);
		extentTest.log(Status.PASS, log9,
				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log9 + ".jpg")).build());

		// Type MLTC On Eligibility Level Value
		ArrayList TS010 = d.getData("TS010", "BT");
		WebElement eligibilityLevelValue = driver.findElement(By.xpath((String) TS010.get(5)));
		eligibilityLevelValue.sendKeys((String) TS010.get(6));
		String log10 = (String) TS010.get(0) + " " + TS010.get(1);
		extentTest.log(Status.PASS, log10,
				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log10 + ".jpg")).build());

		//Select All CheckBox
		ArrayList TS011 = d.getData("TS011", "BT");
		WebElement selectAll = driver.findElement(By.xpath((String) TS011.get(5)));
		selectAll.click();
		String log11 = (String) TS011.get(0) + " " + TS011.get(1);
		extentTest.log(Status.PASS, log11,
				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log11 + ".jpg")).build());
		
		//Click Search All
		ArrayList TS012 = d.getData("TS012", "BT");
		WebElement searchALL = driver.findElement(By.xpath((String) TS012.get(5)));
		searchALL.click();
		String log12 = (String) TS012.get(0) + " " + TS012.get(1);
		extentTest.log(Status.PASS, log12,
				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot(log12 + ".jpg")).build());
	}

	// Screenshot
	public String captureScreenshot(String screenShotName) throws IOException {
		// Shutterbug Working Code
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
