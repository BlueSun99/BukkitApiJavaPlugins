package com.bluesun99.test;

public class TestMain {

	public static void main(String[] args) {
		String s = "VIKING".toLowerCase();
		s = Character.toUpperCase(s.charAt(0)) + s.substring(1);
		System.out.println(s);
	}

}
