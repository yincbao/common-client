package org.cpw.cache.util;

import java.text.StringCharacterIterator;

public class StringUtil {

	public final static String emptyString = "";
	public final static String spaces = "                                                                                                                                                                                                                                                               ";

	/**
	 * Private constuctor
	 */
	private StringUtil() {
	}

	public static boolean isEmpty(String str) {
		return str == null || str.trim().length() == 0;
	}

	public static int toInt(String s) throws NumberFormatException {
		if (isEmpty(s)) {
			return 0;
		} else {
			return Integer.parseInt(s);
		}
	}

	public static long toLong(String s) throws NumberFormatException {
		if (isEmpty(s)) {
			return 0;
		} else {
			return Long.parseLong(s);
		}
	}

	public static String replaceAll(String originalString, String oldSubstring, String newSubstring) {

		int indexPos = 0;

		indexPos = originalString.indexOf(oldSubstring);
		if (indexPos < 0) {
			return originalString;
		} else {
			StringBuffer replacedString = new StringBuffer();
			int endPos = 0;
			int startPos = 0;
			while (indexPos >= 0) {
				replacedString.append(originalString.substring(endPos, indexPos));
				replacedString.append(newSubstring);

				// get new index pos
				endPos = indexPos + oldSubstring.length();
				indexPos = originalString.indexOf(oldSubstring, endPos);
			}

			if (endPos < originalString.length()) {
				replacedString.append(originalString.substring(endPos, originalString.length()));
			}
			return replacedString.toString();
		}
	}

	public static String dbString(String value) {
		return StringUtil.replaceAll(value, "'", "''");
	}

	public static String emptyToNull(String value) {
		if (value == null || "".equals(value.trim())) {
			return null;
		}

		return value;
	}

	public static String nullToEmpty(String value) {
		if (value == null) {
			return "";
		}

		return value;
	}

	public static String nullToString(String value, String defaultValue) {
		if (value == null) {
			return defaultValue;
		}

		return value;
	}

	private static String internalHtmlSafeString(String s, boolean spaceForNull) {
		if (s == null || s.length() == 0) {
			return spaceForNull ? "&nbsp;" : s;
		} else {
			StringBuffer valueReturn = new StringBuffer();
			StringCharacterIterator itr = new StringCharacterIterator(s);
			char character = itr.current();
			while (character != StringCharacterIterator.DONE) {
				if (character == '<') {
					valueReturn.append("&lt;");
				} else if (character == '>') {
					valueReturn.append("&gt;");
				} else if (character == '&') {
					valueReturn.append("&amp;");
				} else if (character == '"') {
					valueReturn.append("&quot;");
				} else {
					valueReturn.append(character);
				}
				character = itr.next();
			}
			return valueReturn.toString();
		}
	}

	public static String htmlSafeString(String s) {
		return internalHtmlSafeString(s, true);
	}

	public static String htmlSafeNullString(String s) {
		return internalHtmlSafeString(s, false);
	}

	public static String normalizeZipCode(String s) {
		if (s == null || s.length() == 0) {
			return "";
		} else if (s.matches("^(\\d{5})(0+)$")) {
			return s.replaceAll("^(\\d{5})(0+)$", "$1");
		} else if (s.matches("^(\\d{5})-(0+)$")) {
			return s.replaceAll("^(\\d{5})-(0+)$", "$1");
		} else if (s.matches("^(\\d{5})(\\d{4})$")) {
			return s.replaceAll("^(\\d{5})(\\d{4})$", "$1-$2");
		} else {
			return s;
		}
	}

	public static String normalizePhoneNumber(String s) {
		if (s == null || s.length() == 0) {
			return "";
		} else if (s.matches("^1*[-0\\(\\)]+$")) {
			return "";
		} else if (s.matches("^1-\\(*(\\d{3})\\)*-(\\d{3})-(\\d{4})$")) {
			return s.replaceAll("^1-\\(*(\\d{3})\\)*-(\\d{3})-(\\d{4})$", "$1-$2-$3");
		} else if (s.matches("^\\((\\d{3})\\)-*(\\d{3})-(\\d{4})$")) {
			return s.replaceAll("^\\((\\d{3})\\)-*(\\d{3})-(\\d{4})$", "$1-$2-$3");
		} else if (s.matches("^(\\d{3})(\\d{3})(\\d{4})$")) {
			return s.replaceAll("^(\\d{3})(\\d{3})(\\d{4})$", "$1-$2-$3");
		} else if (s.matches("^(\\d{3})(\\d{4})$")) {
			return s.replaceAll("^(\\d{3})(\\d{4})$", "$1-$2");
		} else if (s.matches("^-(\\d{3})-(\\d{4})$")) {
			return s.replaceAll("^-(\\d{3})-(\\d{4})$", "$1-$2");
		} else if (s.matches("^-+$")) {
			return s.replaceAll("^-+$", "");
		} else {
			return s;
		}
	}

}