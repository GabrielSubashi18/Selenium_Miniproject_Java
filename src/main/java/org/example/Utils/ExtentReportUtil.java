package org.example.Utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ExtentReportUtil {
    private ExtentReports extentReports;
    private ExtentTest test;

    public ExtentReportUtil(String reportPath) {
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
        extentReports = new ExtentReports();
        extentReports.attachReporter(sparkReporter);
    }

    public void createTest(String testName) {
        test = extentReports.createTest(testName);
    }

    public void logInfo(String message) {
        if (test != null) {
            test.info(message);
        }
    }

    public void logPass(String message) {
        if (test != null) {
            test.pass(message);
        }
    }

    public void logFail(String message) {
        if (test != null) {
            test.fail(message);
        }
    }

    public ExtentTest getTest() {
        return test;
    }

    public ExtentReports getReport() {
        return extentReports;
    }

    public void captureScreenshot(WebDriver driver, String screenshotName) {
        if (driver instanceof TakesScreenshot) {
            TakesScreenshot ts = (TakesScreenshot) driver;
            File srcFile = ts.getScreenshotAs(OutputType.FILE);
            String destPath = "screenshots/" + screenshotName + ".png";
            try {
                Files.createDirectories(new File("screenshots").toPath());
                File destFile = new File(destPath);
                Files.copy(srcFile.toPath(), destFile.toPath());
                test.fail("Screenshot captured: ",
                        MediaEntityBuilder.createScreenCaptureFromPath(destPath).build());
            } catch (IOException e) {
                test.fail("Failed to capture screenshot: " + e.getMessage());
            }
        }
    }

    public void flushReport() {
        if (extentReports != null) {
            extentReports.flush();
        }
    }
}