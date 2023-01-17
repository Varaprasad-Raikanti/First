package TestExecute.LifeOfGirl.AboutUs;

import org.openqa.selenium.By;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

public class ContactUs_map {
  @Test
  
  public void f() throws InterruptedException {
	  System.setProperty("webdriver.chrome.driver","C:\\\\Users\\\\varap\\\\Downloads\\\\chromedriver_win32\\\\chromedriver.exe");
			WebDriver driver= new ChromeDriver();
			driver.manage().window().maximize();
			driver.get("https://www.lifeofgirl.com/");
			Thread.sleep(4000);
			driver.findElement(By.xpath("//span[text()='About us ']")).click();
			driver.findElement(By.xpath("//button[text()='Contact Us ']")).click();
			Thread.sleep(5000);
		//WebElement iframe=	driver.findElement(By.xpath("(//iframe[@frameborder=\"0\"])[1]"));
		driver.switchTo().frame(1);
		Thread.sleep(4000);
		driver.findElement(By.xpath("//button[@aria-label='Zoom in']")).click();
		Thread.sleep(4000);
	driver.switchTo().parentFrame();
		//driver.close();
		
  }
}
