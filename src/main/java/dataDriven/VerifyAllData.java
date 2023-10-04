package dataDriven;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import extentReport.BaseTest;

public class VerifyAllData extends BaseTest{
	
	
	public void getAllRecords() throws InterruptedException {
		
		List<WebElement> elements=driver.findElements(By.xpath("(//tbody[@role='rowgroup'])[4]/tr"));
		Map<String,String> map=new HashedMap<String,String>();
		List<String> memberName=new ArrayList<String>();
		List<String> gender=new ArrayList<String>();
		List<String> memberID=new ArrayList<String>();
		List<String> dob=new ArrayList<String>();
		List<String> pLang=new ArrayList<String>();
		System.out.println("elements.size()"+elements.size());
		String pageinationStr=driver.findElement(By.xpath("//span[@class='k-pager-info k-label']")).getText();
		String totalPageConutStr=pageinationStr.substring(10, 13);
		int totalPageCount=Integer.valueOf(totalPageConutStr);
		int totalPagination=totalPageCount/50;
		int pageStrCount=pageinationStr.length();
		System.out.println("pageStrCount"+pageStrCount+">>>>  pageinationStr"+pageinationStr + "totalPageConutStr>>   "+totalPageConutStr);
		
		for(int j=0;j<totalPagination;j++) {
		for(int i=1;i<=elements.size()-1;i++) {
			
			WebElement ele=driver.findElement(By.xpath("(//tbody[@role='rowgroup'])[4]/tr"+"["+i+"]"+"/td[17]"));
			if(ele.getText().trim().equals("N/A")) {
				System.out.println("If block");
				Thread.sleep(2000);
				String firstName=driver.findElement(By.xpath("(//tbody[@role='rowgroup'])[4]/tr"+"["+i+"]"+"/td[17]//..//../td[4]")).getText();
				String lastName=driver.findElement(By.xpath("(//tbody[@role='rowgroup'])[4]/tr"+"["+i+"]"+"/td[17]//..//../td[5]")).getText();		
				map.put(firstName, lastName);
				Thread.sleep(2000);
				//JavascriptExecutor js=(JavascriptExecutor)driver;
				//js.executeScript("return arguments[0].scrollIntoView();", "(//tbody[@role='rowgroup'])[4]/tr"+"["+i+"]"+"/td[17]//..//../td[3]");
				Actions act=new Actions(driver);
				act.moveToElement(driver.findElement(By.xpath("(//tbody[@role='rowgroup'])[4]/tr"+"["+i+"]"+"/td[17]//..//../td[3]"))).perform();
				Thread.sleep(2000);
				driver.findElement(By.xpath("(//tbody[@role='rowgroup'])[4]/tr"+"["+i+"]"+"/td[17]//..//../td[3]")).click();
				Thread.sleep(2000);
				memberName.add(driver.findElement(By.xpath("//p[contains(text(),'Member Name')]/../following-sibling::div")).getText());
				gender.add(driver.findElement(By.xpath("//p[contains(text(),'Gender')]/../following-sibling::div")).getText());
				memberID.add(driver.findElement(By.xpath("//p[contains(text(),'Member ID')]/../following-sibling::div")).getText());
				dob.add(driver.findElement(By.xpath("//p[contains(text(),'Date of Birth')]/../following-sibling::div")).getText());
				pLang.add(driver.findElement(By.xpath("//p[contains(text(),'Primary Language')]/../following-sibling::div")).getText());
				Thread.sleep(2000);
				
				Thread.sleep(2000);
				driver.findElement(By.xpath("//a[@id='closePopUp']")).click();
				System.out.println("Closed the popup");
				Thread.sleep(2000);
				
				
			}
			
			
			System.out.println("Elements list NA  "+ele.getText());
			System.out.println("Elements list  "+elements.get(i).getText());
		}
		}
		System.out.println(memberName+">>"+gender+">>"+memberID+">>"+dob+">>"+pLang);
		System.out.println("Name list "+map);
		driver.findElement(By.xpath("//span[normalize-space()='arrow-e']")).click();
	}

}
