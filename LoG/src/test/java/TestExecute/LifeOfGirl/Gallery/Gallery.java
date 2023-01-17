package TestExecute.LifeOfGirl.Gallery;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

public class Gallery {
  @Test
  public void f() throws InterruptedException {
	  System.setProperty("webdriver.chrome.driver","D:\\New folder\\chromedriver.exe");
		WebDriver driver= new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("https://www.lifeofgirl.com/");
		Thread.sleep(4000);
		driver.findElement(By.xpath("//a[text()='Gallery']")).click();
		driver.findElement(By.xpath("(//img[@alt=\"personal-safety\"])[2]")).click();
		Thread.sleep(4000);
		driver.close();

  }
  
}
