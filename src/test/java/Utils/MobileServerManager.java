package Utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;

public class MobileServerManager {

		public static void startEmulator() throws Exception {
	
	    String avdName = "Medium_Phone_API_36.1";
	    String sdkRoot = "/Users/admmin/Library/Android/sdk";
	    String emulatorPath = sdkRoot + "/emulator/emulator";
	    String adbPath = sdkRoot + "/platform-tools/adb";
	
	    // Reset adb (IMPORTANT)
	    Runtime.getRuntime().exec(adbPath + " kill-server");
	    Thread.sleep(3000);
	    Runtime.getRuntime().exec(adbPath + " start-server");
	    Thread.sleep(3000);
	
	    // Kill any existing emulator
	    Runtime.getRuntime().exec(adbPath + " emu kill");
	    Thread.sleep(5000);
	
	    // Start emulator (cold boot)
	    Runtime.getRuntime().exec(
	            emulatorPath +
	            " -avd " + avdName +
	            " -no-snapshot -no-boot-anim"
	    );
	
	    // Wait for device connection
	    Runtime.getRuntime().exec(adbPath + " wait-for-device");
	    Thread.sleep(5000);
	
	    // Wait for boot completion (with timeout)
	    int retries = 60; // ~3 minutes
	
	    while (retries > 0) {
	
	        Process p = Runtime.getRuntime().exec(
	                adbPath + " shell getprop sys.boot_completed"
	        );
	
	        BufferedReader br = new BufferedReader(
	                new InputStreamReader(p.getInputStream())
	        );
	
	        String line = br.readLine();
	
	        if (line != null && line.trim().equals("1")) {
	            System.out.println("Emulator fully booted.");
	            return;
	        }
	
	        retries--;
	        Thread.sleep(3000);
	    }
	
	    throw new RuntimeException("Emulator boot timeout");
	}



		public static void waitForDevice() throws Exception {
		
		    Runtime.getRuntime().exec(
		            "/Users/admmin/Library/Android/sdk/platform-tools/adb wait-for-device"
		    );
		
		    Thread.sleep(5000);
		}


		public static void startAppium() throws Exception {

    String nodePath = "/Users/admmin/.nvm/versions/node/v20.20.0/bin";
    String appiumPath = nodePath + "/appium";
    String androidHome = "/Users/admmin/Library/Android/sdk";

    ProcessBuilder pb = new ProcessBuilder(
            appiumPath,
            "--use-plugins=inspector"
    );


    Map<String, String> env = pb.environment();

    // Node
    env.put("PATH", nodePath + ":" + env.get("PATH"));

    // Android SDK (THIS WAS MISSING)
    env.put("ANDROID_SDK_ROOT", androidHome);
    env.put("ANDROID_HOME", androidHome);
    env.put("ANDROID_SDK", androidHome);   // <-- ADD THIS LINE
    env.put("PATH", androidHome + "/platform-tools:" + env.get("PATH"));
    env.put("PATH", androidHome + "/emulator:" + env.get("PATH"));

    // Debug (must NOT be null)
    System.out.println("JAVA ANDROID_HOME = " + env.get("ANDROID_HOME"));
    System.out.println("JAVA ANDROID_SDK_ROOT = " + env.get("ANDROID_SDK_ROOT"));

    pb.redirectErrorStream(true);
    Process p = pb.start();

    new Thread(() -> {
        try (BufferedReader reader =
                     new BufferedReader(new InputStreamReader(p.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("[APPIUM] " + line);
            }
        } catch (Exception ignored) {}
    }).start();

    Thread.sleep(8000);
}
		

	    public static void stopAll() throws Exception {
	
	        Runtime.getRuntime().exec("pkill -f emulator");
	        Runtime.getRuntime().exec("pkill -f appium");
	    }
}
