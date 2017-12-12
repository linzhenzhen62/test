package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


public class Util {

	public static final int WAIT_TIME = 30; // Delay time to wait the website
	// launch completely
	public static final String BASE_URL = "https://portal.righteye.io";
	public static final String USER_NAME = "zhenzhen@righteye.com";
	public static final String PASSWD = "Admin@123";
	public static final String COMPANY_NAME = "RightEye Demos";
	
	public static final String FILE_PATH = "/Users/Document/Documents/workspace/test/src/test"; // File Path
	public static final String FILE_NAME = "testtobii.xlsx"; // Sheet name
	public static final String SHEET_NAME = "Sheet1"; // Name of data table

	public static WebElement findViewStandardLink(WebDriver driver, String assessmentNote) {
		String note;
		List<WebElement> rows = driver.findElements(By.xpath("//*[@id='content']/div[1]/div[1]/div/div[2]/table/tbody[2]/tr/td[1]"));
		WebElement ViewStandard = null;
		for(int i=1;i<=rows.size();i++){

			note = driver.findElement(By.xpath("//*[@id='content']/div[1]/div[1]/div/div[2]/table/tbody[2]/tr["+i+"]/td[6]")).getText();	
			if(note.equals(assessmentNote)){
				ViewStandard = driver.findElement(By.xpath("//*[@id='content']/div[1]/div[1]/div/div[2]/table/tbody[2]/tr["+i+"]/td[5]/a[2]"));
				break;
			}
		}
		return ViewStandard;		
	}
	
	
    public static String[] readAllTestName(String filePath,String fileName,String sheetName) throws IOException{
    	//Create an object of File class to open xlsx file
    	File file = new File(filePath+"/"+fileName);
    	FileInputStream inputStream = new FileInputStream(file);

    	Workbook RightEyeWorkbook = null;
    	//Find the file extension by splitting file name in substring  and getting only extension name
    	String fileExtensionName = fileName.substring(fileName.indexOf("."));

    	//Check condition if the file is xlsx file
    	if(fileExtensionName.equals(".xlsx")){
    		//If it is xlsx file then create object of XSSFWorkbook class
    		RightEyeWorkbook = new XSSFWorkbook(inputStream);
    	}

    	//Check condition if the file is xls file
    	else if(fileExtensionName.equals(".xls")){
    		//If it is xls file then create object of XSSFWorkbook class
    		RightEyeWorkbook = new HSSFWorkbook(inputStream);
    	}

    	//Read sheet inside the workbook by its name

    	Sheet RightEyeSheet = RightEyeWorkbook.getSheet(sheetName);

    	//Find number of rows in excel file
    	int rowCount = RightEyeSheet.getLastRowNum();
    	String[] AllTestName = new String[rowCount+1];
    
    	//Create a loop over all the rows of excel file to read it
    	int ci=0;
    	for (int i=0;i< rowCount+1;i++,ci++) {

    		Row row = RightEyeSheet.getRow(i);
    		//Create a loop to print cell values in a row
    		//Print Excel data in console
    		AllTestName[ci] = row.getCell(0).getStringCellValue();
    		System.out.println("Data from Excel: "+AllTestName[ci]);
    	}
    	return AllTestName;
    }
}