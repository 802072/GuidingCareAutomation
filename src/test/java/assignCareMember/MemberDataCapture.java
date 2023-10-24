package assignCareMember;

import java.awt.AWTException;
import java.io.IOException;

import org.openqa.selenium.By;
import org.testng.annotations.Test;

import extentReport.BaseTest;

public class MemberDataCapture extends BaseTest {

	@Test
	public void memberData() throws IOException, InterruptedException, AWTException {

		login();
		//Verify Page Title
		verifyPageTitle("TS004A", "TestCase");
		//Verify User Name
		assertEquals("TS004B", "TestCase");
		//Open Assign Care Member Page
		openAssignCareMemberPage();
		//Log Total Members Before Member Assign
		captureTotalMemberNumber(); 
		//Assign Care Member, if ENG
		assignCareMember();
		//Log Total Members After Member Assign
		captureTotalMemberNumber(); 

	}
}
