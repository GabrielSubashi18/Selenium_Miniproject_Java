package org.example.Utils;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.WebDriver;
import org.example.Utils.ScreenshotUtil;

public class ScreenshotTestWatcher extends TestWatcher {
    private final WebDriver driver;

    public ScreenshotTestWatcher(WebDriver driver) {
        this.driver = driver;
    }

    @Override
    protected void failed(Throwable e, Description description) {
        String screenshotName = description.getMethodName() + "_" + System.currentTimeMillis();
        ScreenshotUtil.captureScreenshot(driver, screenshotName);
        System.err.println("Screenshot captured: " + screenshotName);
    }
}
