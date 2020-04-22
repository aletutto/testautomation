import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.touch.LongPressOptions;
import io.appium.java_client.touch.offset.ElementOption;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class CalculatorTest {
  private AndroidDriver<MobileElement> androidDriver;

  @BeforeClass
  public void setUp() throws IOException {
    File classpathRoot = new File(System.getProperty("user.dir"));
    File appDir = new File(classpathRoot, "src/test/resources/");
    File app = new File(appDir.getCanonicalPath(), "Google_Calculator.apk");

    DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
    desiredCapabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Android Emulator");
    desiredCapabilities.setCapability(MobileCapabilityType.APP, app.getAbsolutePath());

    URL appiumUrl = new URL("http://127.0.0.1:4723/wd/hub");
    androidDriver = new AndroidDriver<MobileElement>(appiumUrl, desiredCapabilities);
  }

  @AfterClass
  public void tearDown() {
    androidDriver.quit();
  }

  @Test
  public void testAddOperationResultIsShown() {
    AndroidElement digitThree = (AndroidElement) androidDriver.findElementById("digit_3");
    AndroidElement digitFive = (AndroidElement) androidDriver.findElementById("digit_5");
    AndroidElement digitEight = (AndroidElement) androidDriver.findElementById("digit_8");
    AndroidElement operationAdd = (AndroidElement) androidDriver.findElementById("op_add");
    AndroidElement equals = (AndroidElement) androidDriver.findElementById("eq");

    WebDriverWait wait = new WebDriverWait(androidDriver, 10);
    wait.until(ExpectedConditions.elementToBeClickable(digitThree));

    digitThree.click();
    digitFive.click();
    operationAdd.click();
    digitEight.click();
    equals.click();

    AndroidElement result = (AndroidElement) androidDriver.findElementById("result_final");
    wait.until(ExpectedConditions.visibilityOf(result));

    Assert.assertEquals(43, Integer.parseInt(result.getText()));
  }

  @Test
  public void testDivideByZeroExceptionIsShown() {
    AndroidElement digitThree = (AndroidElement) androidDriver.findElementById("digit_3");
    AndroidElement digitZero = (AndroidElement) androidDriver.findElementById("digit_0");
    AndroidElement operationDivide = (AndroidElement) androidDriver.findElementById("op_div");
    AndroidElement equals = (AndroidElement) androidDriver.findElementById("eq");

    WebDriverWait wait = new WebDriverWait(androidDriver, 10);
    wait.until(ExpectedConditions.elementToBeClickable(digitThree));

    digitThree.click();
    operationDivide.click();
    digitZero.click();
    equals.click();

    AndroidElement errorTextInResultPreview = (AndroidElement) androidDriver.findElementById("result_preview");
    wait.until(ExpectedConditions.visibilityOf(errorTextInResultPreview));

    Assert.assertEquals("Kein Teilen durch 0", errorTextInResultPreview.getText());
  }

  @Test
  public void testResetFormulaAndResultPreview() {
    AndroidElement delete = (AndroidElement) androidDriver.findElementById("del");
    AndroidElement formula = (AndroidElement) androidDriver.findElementById("formula");
    AndroidElement resultPreview = (AndroidElement) androidDriver.findElementById("result_preview");

    TouchAction touchAction = new TouchAction(androidDriver);
    touchAction.longPress(LongPressOptions.longPressOptions().withElement(ElementOption.element(delete)));
    touchAction.release();
    touchAction.perform();

    Assert.assertEquals("", formula.getText());
    Assert.assertEquals("", resultPreview.getText());
  }

  @Test
  public void testResultPreviewIsShown() throws InterruptedException {
    AndroidElement digitThree = (AndroidElement) androidDriver.findElementById("digit_3");
    AndroidElement digitFive = (AndroidElement) androidDriver.findElementById("digit_5");
    AndroidElement operationAdd = (AndroidElement) androidDriver.findElementById("op_add");

    WebDriverWait wait = new WebDriverWait(androidDriver, 10);
    wait.until(ExpectedConditions.elementToBeClickable(digitThree));

    digitThree.click();
    operationAdd.click();
    digitFive.click();

    AndroidElement resultPreview = (AndroidElement) androidDriver.findElementById("result_preview");
    wait.until(ExpectedConditions.visibilityOf(resultPreview));

    Assert.assertEquals(8, Integer.parseInt(resultPreview.getText()));
  }
}
