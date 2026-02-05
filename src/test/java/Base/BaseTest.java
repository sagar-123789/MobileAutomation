package Base;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import Utils.MobileServerManager;

import java.net.URL;
import java.time.Duration;

public class BaseTest {

    protected static AndroidDriver driver;

    // @BeforeClass
    public static void setup() throws Exception {

        MobileServerManager.startEmulator();
        MobileServerManager.waitForDevice();
        MobileServerManager.startAppium();

        UiAutomator2Options options = new UiAutomator2Options();

        options.setPlatformName("Android");
        options.setAutomationName("UiAutomator2");
        options.setDeviceName("emulator-5554");
        // options.setAvd("Medium_Phone_API_36.1");
        options.setApp("/Users/admmin/Downloads/app-release.apk");
        options.setAutoGrantPermissions(true);

        URL url = new URL("http://127.0.0.1:4723");

        driver = new AndroidDriver(url, options);
        
        System.out.println("SESSION CREATED SUCCESSFULLY");


        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    // @AfterClass
    public static void tearDown() throws Exception {

        driver.quit();
        MobileServerManager.stopAll();
    }
}
