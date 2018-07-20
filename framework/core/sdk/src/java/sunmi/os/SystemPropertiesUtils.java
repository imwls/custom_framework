package sunmi.os;

/**
 * Gives access to the system properties store. The system properties store
 * contains a list of string key-value pairs.
 * @deprecated comment
 */
public class SystemPropertiesUtils {

	/**
	 * Get the value for the given key.
	 * 
	 * @return an empty string if the key isn't found
	 * @throws IllegalArgumentException
	 *             if the key exceeds 32 characters
	 */
	public static String get(String key) {
		return android.os.SystemProperties.get(key);
	}

	/**
	 * Get the value for the given key.
	 * 
	 * @return if the key isn't found, return def if it isn't null, or an empty
	 *         string otherwise
	 * @throws IllegalArgumentException
	 *             if the key exceeds 32 characters
	 */
	public static String get(String key, String def) {
		return android.os.SystemProperties.get(key, def);
	}

	/**
	 * Get the value for the given key, and return as an integer.
	 * 
	 * @param key
	 *            the key to lookup
	 * @param def
	 *            a default value to return
	 * @return the key parsed as an integer, or def if the key isn't found or cannot
	 *         be parsed
	 * @throws IllegalArgumentException
	 *             if the key exceeds 32 characters
	 */
	public static int getInt(String key, int def) {
		return android.os.SystemProperties.getInt(key, def);
	}

	/**
	 * Get the value for the given key, and return as a long.
	 * 
	 * @param key
	 *            the key to lookup
	 * @param def
	 *            a default value to return
	 * @return the key parsed as a long, or def if the key isn't found or cannot be
	 *         parsed
	 * @throws IllegalArgumentException
	 *             if the key exceeds 32 characters
	 */
	public static long getLong(String key, long def) {
		return android.os.SystemProperties.getLong(key, def);
	}

	/**
	 * Get the value for the given key, returned as a boolean. Values 'n', 'no',
	 * '0', 'false' or 'off' are considered false. Values 'y', 'yes', '1', 'true' or
	 * 'on' are considered true. (case sensitive). If the key does not exist, or has
	 * any other value, then the default result is returned.
	 * 
	 * @param key
	 *            the key to lookup
	 * @param def
	 *            a default value to return
	 * @return the key parsed as a boolean, or def if the key isn't found or is not
	 *         able to be parsed as a boolean.
	 * @throws IllegalArgumentException
	 *             if the key exceeds 32 characters
	 */
	public static boolean getBoolean(String key, boolean def) {
		return android.os.SystemProperties.getBoolean(key, def);
	}

	/**
	 * Set the value for the given key.
	 * 
	 * @throws IllegalArgumentException
	 *             if the key exceeds 32 characters
	 * @throws IllegalArgumentException
	 *             if the value exceeds 92 characters
	 */
	public static void set(String key, String val) {
		android.os.SystemProperties.set(key, val);
	}

}
