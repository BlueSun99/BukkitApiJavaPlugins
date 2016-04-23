package com.bluesun99.test;

public class TestMain {

	public static void main(String[] args) {
		for (Class<?> c : AnotherClass.class.getDeclaredClasses())
		{
			System.out.println(c.getSimpleName());
		}
		//System.out.println();
		
		System.out.println(java.util.Locale.KOREAN);
	}

}
