package org.example.Utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Logger;

public class ScreenshotUtil {
    private static final Logger logger = Logger.getLogger(ScreenshotUtil.class.getName());

    public static void captureScreenshot(WebDriver driver, String fileName) {
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        Path destinationPath = Paths.get("screenshots", fileName + ".png");
        try {
            Files.createDirectories(destinationPath.getParent());
            Files.copy(screenshot.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
            logger.info("Screenshot saved to: " + destinationPath.toString());
        } catch (IOException e) {
            logger.severe("Failed to save screenshot: " + e.getMessage());
        }
    }
}