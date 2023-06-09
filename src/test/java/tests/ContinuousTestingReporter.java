package tests;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestResult;

public class ContinuousTestingReporter {

    protected DesiredCapabilities capabilities = new DesiredCapabilities();

    public void init() {}

    /**
     * Example of Standard Capabilities that will be set across Organization
     *
     * "BUILD_NUMBER" is an Environment Variable from Jenkins.
     * This is where we can hard code in certain behaviors and environment variables we would like to validate
     * against for all tests.
     */
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


    // Example of how we can capture the Error Message from StackTrace provided by the Test Execution
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

    /**
     * When running Appium based Scripts, the values provided in the Desired Capabilities becomes properties for Filtering Test Results.
     * However - We may need to add additional properties depending on the state of the Test or how it ended.
     * This code example allows us to add properties mid-test.
     */
    public void addPropertyForReporting(AppiumDriver driver, String property, String value) {
        driver.executeScript("seetest:client.addTestProperty(\"" + property + "\", \"" + value + "\")");
    }

    // Marks entire Test as either Passed / Failed / Skipped, and sent custom message, such as StackTrace
    public void setReportStatus(AppiumDriver driver, String status, String message) {
        driver.executeScript("seetest:client.setReportStatus(\"" + status + "\", \"" + status + "\", \"" + message + "\")");
    }

    // This adds a custom line in the Report Steps. Status is either "true" meaning Step Passed, or "false" meaning Step Failed
    public void addReportStep(AppiumDriver driver, String input, String status) {
        driver.executeScript("seetest:client.report(\"" + input + "\", \"" + status + "\")");
    }

}
