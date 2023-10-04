package assignCareMember;

import java.awt.AWTException;
import java.io.IOException;
import java.util.ArrayList;

import org.testng.annotations.Test;

import dataDriven.VerifyAllData;
import extentReport.BaseTest;
import extentReport.BaseTest;

public class MemberDataCapture extends BaseTest {
	VerifyAllData v = new VerifyAllData();



	@Test
	public void memberData() throws IOException, InterruptedException, AWTException {
		
		login();
		openAssignCareMemberPage();
		assignCareMember();

	}
}
