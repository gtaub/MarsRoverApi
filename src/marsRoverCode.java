import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;

public class marsRoverCode {
	final static String DATE_FORMAT = "YYMMDD";

	public static void main(String[] args) throws IOException, AWTException, InterruptedException, ClassNotFoundException {
		 String current = new java.io.File( "." ).getCanonicalPath();
	        System.out.println("Current dir:"+current);
	        
		//Read Data from file
		String file = current + "/src/inputFile";
		BufferedReader br = new BufferedReader(new FileReader(file)); 
		
		String urlStart = "https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?earth_date=";
		String earthDT ="";
		String apiKey = "&api_key=ufUgKakiBpPXcHDh4kH2FePMffkgcSloyXrgCnSW";

		WebDriver driver = null;
		System.setProperty("webdriver.chrome.driver", current +"/src/Driver/chromedriver");
		
		  String st; 
		 int startPosition = 0;
		 String startOfUrl ="";
		 
		  while ((st = br.readLine()) != null) 
		  {
			if (isValidDate(st)) {  
				driver = new ChromeDriver();
			    System.out.println(st);
			    String year = st.substring(6);
			    String month = st.substring(0, 2);
			    String day = st.substring(3, 5);
			    earthDT = year + "-" +month + "-" + day;
				//driver.get("https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?earth_date=2015-6-3&api_key=ufUgKakiBpPXcHDh4kH2FePMffkgcSloyXrgCnSW");
			  
				   driver.get(urlStart + earthDT + apiKey);
			  
				String pageSource = driver.getPageSource();	
	
				int imgSrc = pageSource.indexOf("img_src"); 
				startPosition = imgSrc;
					
					// Variable to keep track of Starting point of the Page Source data
					startOfUrl = pageSource.substring(imgSrc+10);
					
					// Keep track of the physical place location in pageSource.
					startPosition = startPosition +10;
					
					// Numerical representation of location
					int endOfUrl = startOfUrl.indexOf(",");
					
	//				Finding the URL to use to open picture 
					String url = startOfUrl.substring(0, endOfUrl -1);
					System.out.print(url);
					
					// Get the Name of the file by traversing through the forward slashes
					int index = url.indexOf("/");
					int counter = 0;
					while(index >= 0) {
						counter = index;
					    index = url.indexOf("/", index+1);
					    
					}
					String filename= url.substring(counter+1);
					
					// Open picture.
					driver.get(url);
					addScreenshot(driver, filename,current);
					driver.quit();
			}else {
				System.out.println("Date provided is not a valid date.");
			}
			
		  } // end of file reading while  
		  br.close();	
		  System.out.println("Process Completed");
	}
	
  // Used to take screenshot of image and saved to drive
	public static void addScreenshot(WebDriver driver, String imageName, String path) throws IOException,ClassNotFoundException, InterruptedException 
	{
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(scrFile, new File(path +"//" + imageName));
	}

	private static boolean isValidDate(String input) {
        String formatString = "MM/dd/yyyy";

        try {
            SimpleDateFormat format = new SimpleDateFormat(formatString);
            format.setLenient(false);
            format.parse(input);
        } catch (ParseException e) {
            return false;
        } catch (IllegalArgumentException e) {
            return false;
        }

        return true;
    }
}
