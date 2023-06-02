package tests;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class ReportHandling {

    protected DesiredCapabilities capabilities = new DesiredCapabilities();

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

}
