package tests;

import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;

import static org.testng.Assert.assertTrue;

public class FilteringTests {

    protected String ACCESS_KEY = "";

    protected DesiredCapabilities capabilities = new DesiredCapabilities();
    protected IOSDriver driver;
    protected SeeTestReporter reporter = new SeeTestReporter();

    @BeforeMethod
    public void setUp(Method method) throws MalformedURLException {
        capabilities.setCapability("testName", method.getName());
        capabilities.setCapability("accessKey", ACCESS_KEY);
        capabilities.setCapability("deviceQuery", "@os='ios' and contains(@name, 'iPhone 13')");
        capabilities.setCapability("app", "cloud:com.experitest.ExperiBank");
        capabilities.setCapability("bundleId", "com.experitest.ExperiBank");

        reporter.init(capabilities); // Corporate Standard
        reporter.init(capabilities, "BUILD_NUMBER", "isLocalExecution");
        reporter.init(capabilities, "BUILD_NUMBER", "isJenkinsExecution", "true", "false");

        driver = new IOSDriver(new URL("https://uscloud.experitest.com/wd/hub"), capabilities);
    }

    @Test
    public void positive_test() throws InterruptedException {
        driver.findElement(By.name("usernameTextField")).sendKeys("company");
        driver.findElement(By.name("passwordTextField")).sendKeys("company");
        driver.findElement(By.name("loginButton")).click();

        Thread.sleep(3000);
        Boolean isLogoutButtonPresent = driver.findElement(By.name("logoutButton")).isDisplayed();
        assertTrue(isLogoutButtonPresent);
    }

    @Test
    public void negative_test() throws InterruptedException {
        driver.findElement(By.name("usernameTextField")).sendKeys("company");
        driver.findElement(By.name("passwordTextField")).sendKeys("company");
        driver.findElement(By.name("loginButton")).click();

        Thread.sleep(3000);
        Boolean isLogoutButtonPresent = driver.findElement(By.name("invalidElement")).isDisplayed();
        assertTrue(isLogoutButtonPresent);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        try {
            if (result.isSuccess()) {
                reporter.setReportStatus(driver, "Passed", "Test Name: " + result.getName());
            } else {
                String cause = reporter.captureFailureCause(result);
                reporter.addPropertyForReporting(driver, "failureCause", cause);
                reporter.setReportStatus(driver, "Failed", cause);
            }
        } catch (Exception e) {
            System.out.println("Could not pass relevant test information in TearDown. Ending session gracefully.");
            e.printStackTrace();
        }

        boolean isCapabilityPresent = reporter.isCapabilityPresent(driver, capabilities, "isManual");
        assertTrue(isCapabilityPresent, "isManual capability is not present.");
        driver.quit();
    }

}
