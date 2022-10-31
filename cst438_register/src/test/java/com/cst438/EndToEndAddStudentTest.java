package com.cst438;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cst438.domain.Student;
import com.cst438.domain.StudentRepository;

@SpringBootTest
public class EndToEndAddStudentTest {
	public static final String CHROME_DRIVER_FILE_LOCATION = "C:/Users/jokke/Desktop/chromedriver.exe";

	public static final String URL = "http://localhost:3000";

	public static final String TEST_STUDENT_EMAIL = "test_student@csumb.edu";

	public static final String TEST_STUDENT_NAME = "Test Student"; 

	public static final int SLEEP_DURATION = 1000; // 1 second.
	
	@Autowired
	StudentRepository studentRepository;
	
	@Test
	public void addNewStudentAndCheck() throws Exception {
		Student temp = null;
		
		do {
			temp = studentRepository.findByEmail(TEST_STUDENT_EMAIL);
			if (temp != null)
				studentRepository.delete(temp);
		}while(temp != null);
		
		System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
		WebDriver driver = new ChromeDriver();
		// Puts an Implicit wait for 10 seconds before throwing exception
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		try {
			
			driver.get(URL);
			Thread.sleep(SLEEP_DURATION);
			
			WebElement we = driver.findElement(By.xpath("//a[@id='addStudent']"));
			we.click();
			Thread.sleep(SLEEP_DURATION);
			
			driver.findElement(By.xpath("//input[@id='email']")).sendKeys(TEST_STUDENT_EMAIL);
			
			driver.findElement(By.xpath("//input[@id='name']")).sendKeys(TEST_STUDENT_NAME);
			
			driver.findElement(By.xpath("//button[@id='addButton']")).click();
			
			Thread.sleep(SLEEP_DURATION);
			
			String text = driver.findElement(By.xpath("//p[@id='message']")).getText();
			
			assertEquals("Student was added successfully", text);
			
			
			// checks if student that was added can't be added again
			driver.findElement(By.xpath("//button[@id='addButton']")).click();
			
			Thread.sleep(SLEEP_DURATION);
			
			text = driver.findElement(By.xpath("//p[@id='message']")).getText();
			
			assertEquals("Student with email already exists", text);
			
			Thread.sleep(SLEEP_DURATION);
			
		}catch (Exception e){
			throw e;
		}finally {
			
			// cleans up database
			Student e = studentRepository.findByEmail(TEST_STUDENT_EMAIL);
			if (e != null) {
				studentRepository.delete(e);
			}
			driver.quit();
		}
	}
}
