package android.ui.auto.framework;

import java.util.Properties;

public class GlobalContent {
	public static Properties properties_android;
	public static Properties properties_ios;

	public static String getConfig(String key, String os) {
		if (os.equals("Android")) {
			if (properties_android != null) {
				String value = properties_android.getProperty(key);
				if (value == null || value.trim().isEmpty()) {
					return null;
				}
				try {
					value = new String(value.getBytes("ISO8859-1"), "UTF-8");
				} catch (Exception e) {
					// TODO: handle exception
				}
				return value.trim();
			}
			return null;
		} else if (os.equals("IOS")) {
			if (properties_ios != null) {
				String value = properties_ios.getProperty(key);
				if (value == null || value.trim().isEmpty()) {
					return null;
				}
				try {
					value = new String(value.getBytes("ISO8859-1"), "UTF-8");
				} catch (Exception e) {
					// TODO: handle exception
				}
				return value.trim();
			}
			return null;
		}
		return null;
	}
}
