package tests;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestResult;

public class SeeTestReporter {

    protected DesiredCapabilities capabilities = new DesiredCapabilities();
    protected AppiumDriver driver;

    public void init() {}

    // Example of Standard Capabilities that will be set across Organization
    public DesiredCapabilities init(DesiredCapabilities capabilities) {
        this.capabilities = capabilities;

        if (System.getenv("BUILD_NUMBER") != null && !System.getenv("BUILD_NUMBER").isEmpty()) {
            capabilities.setCapability("isManual", "false");
        } else {
            capabilities.setCapability("isManual", "true");
        }

        return capabilities;
    }

    // Example of custom Capabilities that will be set per Test Execution, with hard coded values (true / false)
    public DesiredCapabilities init(DesiredCapabilities capabilities, String environmentVariable, String capabilityName) {
        this.capabilities = capabilities;

        if (System.getenv(environmentVariable) != null && !System.getenv(environmentVariable).isEmpty()) {
            capabilities.setCapability(capabilityName, "false");
        } else {
            capabilities.setCapability(capabilityName, "true");
        }

        return capabilities;
    }

    // Example of custom Capabilities that will be set per Test Execution, with flexible values provided by users
    public DesiredCapabilities init(DesiredCapabilities capabilities, String environmentVariable, String capabilityName, String valueIfConditionMet, String valueIfConditionNotMet) {
        this.capabilities = capabilities;

        if (System.getenv(environmentVariable) != null && !System.getenv(environmentVariable).isEmpty()) {
            capabilities.setCapability(capabilityName, valueIfConditionMet);
        } else {
            capabilities.setCapability(capabilityName, valueIfConditionNotMet);
        }

        return capabilities;
    }

    // Used to capture Failure Cause Message in case the Test fails
    public String captureFailureCause(ITestResult result) {
        String cause = null;

        try {
            String[] fullCauseFailure = result.getThrowable().getMessage().split("\\R");
            cause = fullCauseFailure[0];
        } catch (Exception e) {
            System.out.println("captureFailureCause - Unable to capture Failure Cause");
            e.printStackTrace();
        }

        return cause;
    }

    // Used for Validation purposes, to see if a certain Capability exists as part of the Test Execution.
    // If the capability being looked for doesn't exist, appropriate property will be added for the purpose of filtering.
    public boolean isCapabilityPresent(AppiumDriver driver, DesiredCapabilities capabilities, String capabilityName) {
        if (!isCapabilityPresent(capabilities, capabilityName)) {
            driver.executeScript("seetest:client.addTestProperty", "failedAudit", "true");
        }
        return isCapabilityPresent(capabilities, capabilityName);
    }

    public boolean isCapabilityPresent(DesiredCapabilities capabilities, String capabilityName) {
        return capabilities.getCapability(capabilityName) != null;
    }

    public void addPropertyForReporting(AppiumDriver driver, String property, String value) {
        driver.executeScript("seetest:client.addTestProperty(\"" + property + "\", \"" + value + "\")");
    }

    public void setReportStatus(AppiumDriver driver, String status, String message) {
        driver.executeScript("seetest:client.setReportStatus(\"" + status + "\", \"" + status + "\", \"" + message + "\")");
    }

    // Status is either "true" or "false"
    //
    public void addReportStep(AppiumDriver driver, String input, String status) {
        driver.executeScript("seetest:client.report(\"" + input + "\", \"" + status + "\")");
    }

}
