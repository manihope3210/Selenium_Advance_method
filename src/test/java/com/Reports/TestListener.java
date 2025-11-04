package com.Reports;

import java.io.File;
import java.io.IOException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import com.aventstack.extentreports.*;
import com.google.common.io.Files;
import com.SeleniumBase.Base;

public class TestListener extends Base implements ITestListener {

    public static ExtentReports extent = ExtentManager.getInstance();
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    public static ExtentTest getTest() {
        return test.get();
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest extentTest = extent.createTest(result.getMethod().getMethodName());
        test.set(extentTest);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        test.get().pass("✅ Test Passed Successfully!");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        test.get().fail("❌ Test Failed: " + result.getThrowable());
        try {
            File src = ((TakesScreenshot) dr).getScreenshotAs(OutputType.FILE);
            String path = "./reports/screenshots/" + result.getMethod().getMethodName() + ".png";
            File dest = new File(path);
            dest.getParentFile().mkdirs();
            Files.copy(src, dest);
            test.get().addScreenCaptureFromPath(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        test.get().skip("⚠️ Test Skipped: " + result.getThrowable());
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
    }
}
