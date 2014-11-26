package com.test.main;


public class MainTest01 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(getParasStr(0));
		
		String s = "123456789";
		System.out.println(s.substring(0, 1));
		System.out.println(s.substring(2, 4));
	}

	public static String getParasStr(int length) {
		if (length <= 0) {
			return null;
		} else {
			StringBuffer sb = new StringBuffer();
			for (int j = 0; j < length - 1; j++) {
				sb.append("?,");
			}
			sb.append("?");

			return sb.toString();
		}
	}

}
