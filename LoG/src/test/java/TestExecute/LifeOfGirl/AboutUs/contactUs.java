package TestExecute.LifeOfGirl.AboutUs;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

public class contactUs {
  @Test
  public void f() throws InterruptedException {
	  System.setProperty("webdriver.chrome.driver","D:\\New folder\\chromedriver.exe");
		WebDriver driver= new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("https://www.lifeofgirl.com/");
		
		Thread.sleep(4000);
		driver.findElement(By.xpath("//span[text()='About us ']")).click();
		driver.findElement(By.xpath("//button[text()='Contact Us ']")).click();
		Thread.sleep(5000);
		driver.findElement(By.xpath("//*[@id=\"mat-input-5\"]")).sendKeys("ssssssssssss");
		driver.findElement(By.xpath("//*[@id=\"mat-input-6\"]")).sendKeys("xxxxxxx");
		driver.findElement(By.xpath("//*[@id=\"mat-input-7\"]")).sendKeys("xxxxx");
		driver.findElement(By.xpath("//*[@id=\"mat-input-8\"]")).sendKeys("LifeOfGirl");
		
  }
}
