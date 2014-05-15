package android.ui.auto.framework;

import java.util.Properties;

public class GlobalContent {
	public static Properties properties;

	public static String getConfig(String key) {
		if (properties != null) {
			String value = properties.getProperty(key);
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
}
