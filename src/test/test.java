package test;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import test.Util;


public class test {
	private WebDriver driver;
	private String baseUrl; // baseUrl of preprod RightEye
	String[] TestName;
	
	@BeforeTest
	public void setUp() throws Exception {

//		System.setProperty("webdriver.gecko.driver", Util.DRIVER_PATH);
		driver = new SafariDriver();
//		driver.manage().window().maximize();
		baseUrl = Util.BASE_URL;
		Thread.sleep(1000);
//		driver.manage().timeouts().implicitlyWait(Util.WAIT_TIME, TimeUnit.SECONDS);
		driver.get(baseUrl);
		driver.findElement(By.name("username")).clear();
		driver.findElement(By.name("username")).sendKeys(Util.USER_NAME);
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys(Util.PASSWD);
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		
		WebDriverWait wait=new WebDriverWait(driver, 30);
		
		WebElement CompanyManagement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a[href=\"#/companylist\"]")));
		CompanyManagement.click();
		// click company management link	
		WebElement SearchCompany = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type='search']")));
		//wait until search key is visible
		SearchCompany.sendKeys(Util.COMPANY_NAME);
		driver.findElement(By.xpath("//button[@id='btnSearch']")).click();
		// find RightEye Demos
		
		WebElement Assessments = wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Assessments")));
		//wait until Assessments link is visible
		Assessments.click();
		Thread.sleep(2000);
		
		WebElement changePage = driver.findElement(By.xpath("//*[@id='content']/div[1]/div[1]/div/div[2]/table/tfoot/tr/td/div/nav/ul/li[8]/a"));
		changePage.click();
		Thread.sleep(2000);
		//go to page 8
		
	
		String[] AllTests = Util.readAllTestName(Util.FILE_PATH, Util.FILE_NAME,Util.SHEET_NAME);
		//read the first column from Excel
		String assessmentNote = AllTests[0];
		System.out.println("assessmentNote:" + assessmentNote);
		WebElement findViewStandard = Util.findViewStandardLink(driver,assessmentNote);
		//find WebElement 'ViewStandard' through Note
		WebElement ViewStandard = wait.until(ExpectedConditions.visibilityOf(findViewStandard));
		//wait until 'ViewStandard' is visible
		ViewStandard.click();
			
		WebElement ParticipantDetails = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='content']/div/div/div/div[3]/div[4]/div[1]/div[1]/h2")));
	}	
	
		@Test(priority = 0)
		public void verifyTestName() throws Exception{
			String[] AllTests = Util.readAllTestName(Util.FILE_PATH, Util.FILE_NAME,Util.SHEET_NAME);
			List<WebElement> tests = driver.findElements(By.xpath("//*[@id='content']/div/div/div/div[3]/div[4]/following-sibling::div//h2"));
			List<String> TestNameArray = new ArrayList<>();
			//Actual Test Number equals TestNumber+1
			int index=0;
			System.out.println(AllTests[17]);
			for(int i=0;i<tests.size();){
				TestNameArray.add(tests.get(i).getText());
				Assert.assertEquals(tests.get(i).getText(),AllTests[index+1]);
				//verify all the tests can be seen in the report in correct order
				System.out.println("actual:"+tests.get(i).getText()+"  expected:"+AllTests[index+1]);
				index++;
				if(tests.get(i).getText().equals("Pupillary Distance")){
					i += 2;
				}
				else if(tests.get(i).getText().equals("Reading")){
					i += 3; 
				}
				else{
					i++;
				}		
			}
			
			TestName = new String[TestNameArray.size()]; 
			for(int i=0;i<TestName.length;i++){
				TestName[i] = TestNameArray.get(i);
			}
		}
	
		
		@Test(priority = 1)
	public void verifyCircuitSmoothPursuit() throws ParseException{
			String value;
			double val;
			double SmoothPursuit = 0.00;
			Double SumTo100 = 0.00;
			Double SumSmoothPursuit = 0.00;
			
			
			for(int col=2;col<=6;col+=2){
				for(int row=4;row<=6;row++){
					value = driver.findElement(By.xpath("//*[@id='content']/div/div/div/div[3]/div[6]/div/div[4]/table/tbody/tr["+row+"]/td["+col+"]")).getText();
					NumberFormat f = NumberFormat.getNumberInstance();
					Number num = f.parse(value);
					value = num.toString();
					val = Double.parseDouble(value);
					if(row==4){
						SmoothPursuit = val;
					}
					SumTo100 += val;
				}
				if(SumTo100<99.50 || SumTo100>100.50){
					System.out.println("Check CSP Sum Col"+col);
					//verify sum of smooth pursuit, saccade, fixation equals 100
				}
				SumTo100 = 0.00;
				
				for(int row=14;row<=16;row++){
					value = driver.findElement(By.xpath("//*[@id='content']/div/div/div/div[3]/div[6]/div/div[4]/table/tbody/tr["+row+"]/td["+col+"]")).getText();
					NumberFormat f = NumberFormat.getNumberInstance();
					Number num = f.parse(value);
					value = num.toString();
					val = Double.parseDouble(value);
					SumSmoothPursuit += val;
				}
		
				if(SumSmoothPursuit< (SmoothPursuit-0.50) || SumSmoothPursuit> (SmoothPursuit+0.50)){
					System.out.println("Check CSP Smooth Pursuit Col"+col);
					//verify sum of on target smooth pursuit, predictive smooth pursuit, latent smooth pursuit 
					//equals smooth pursuit
				}
				
				SumSmoothPursuit = 0.00;
				
			}
	}
		
		@Test(priority = 2)
	public void verifyHorizontalSmoothPursuit() throws ParseException{
			String value;
			double val;
			Double SumTo100 = 0.00;
			for(int col=2;col<=6;col+=2){
				for(int row=4;row<=6;row++){
					value = driver.findElement(By.xpath("//*[@id='content']/div/div/div/div[3]/div[7]/div/div[3]/table/tbody/tr["+row+"]/td["+col+"]")).getText();
					NumberFormat f = NumberFormat.getNumberInstance();
					Number num = f.parse(value);
					value = num.toString();
					val = Double.parseDouble(value);
					SumTo100 += val;
				}
				if(SumTo100<99.50 || SumTo100>100.50){
					System.out.println("Check HSP Sum Col"+col);
					//verify sum of smooth pursuit, saccade, fixation equals 100
				}
				SumTo100 = 0.00;
			}
		}

		@Test(priority = 3)
	public void verifyVerticalSmoothPursuit() throws ParseException{
			String value;
			double val;
			Double SumTo100 = 0.00;
			for(int col=2;col<=6;col+=2){
				for(int row=4;row<=6;row++){
					value = driver.findElement(By.xpath("//*[@id='content']/div/div/div/div[3]/div[8]/div/div[3]/table/tbody/tr["+row+"]/td["+col+"]")).getText();
					NumberFormat f = NumberFormat.getNumberInstance();
					Number num = f.parse(value);
					value = num.toString();
					val = Double.parseDouble(value);
					SumTo100 += val;
				}
				if(SumTo100<99.50 || SumTo100>100.50){
					System.out.println("Check VSP Sum Col"+ col);
					//verify sum of smooth pursuit, saccade, fixation equals 100
				}
				SumTo100 = 0.00;
			}
		}
		
		@Test(priority =4)
	public void verifyChoiceReactionTime(){
			String value;
			double val;
			Double SumToRT = 0.00;
			Double ReactionTime  = 0.00;
			
			for(int row=3;row<=6;row++){
				value = driver.findElement(By.xpath("//*[@id='content']/div/div/div/div[3]/div[18]/div/div[2]/table/tbody/tr["+row+"]/td[2]")).getText();
				value = value.toString().split(" ")[0];
				val = Double.parseDouble(value);
				if(row!=6) SumToRT += val;
				else ReactionTime = val;
			}
			if(SumToRT<ReactionTime-1.00 || SumToRT>ReactionTime+1.00){
				System.out.println("Check CRT Reaction Time");
				//verify sum of saccadic latency, visual reaction speed, processing speed 
				//equals reaction time
			}	
		}
		
		@Test(priority =5)
		public void verifyDiscriminateReactionTime(){
				String value;
				double val;
				Double SumToRT = 0.00;
				Double ReactionTime  = 0.00;
				
				for(int row=3;row<=6;row++){
					value = driver.findElement(By.xpath("//*[@id='content']/div/div/div/div[3]/div[19]/div[1]/div[2]/table/tbody/tr["+row+"]/td[2]")).getText();
					value = value.toString();
					value = value.toString().split(" ")[0];
					val = Double.parseDouble(value);
					if(row!=6) SumToRT += val;
					else ReactionTime = val;
				}
				if(SumToRT<ReactionTime-1.00 || SumToRT>ReactionTime+1.00){
					System.out.println("Check DRT Reaction Time");
					//verify sum of saccadic latency, visual reaction speed, processing speed 
					//equals reaction time
				}
			}
		
	@AfterTest
	public void Exit(){
		driver.quit();
	}
	
}
