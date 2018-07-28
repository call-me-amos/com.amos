package com.amos.tong.lambda;

import java.util.ArrayList;
import java.util.List;

public class LambdaTest {

	public static void main(String[] args) {

		Thread t1 = new Thread(() -> System.out.println("aa"));
		Thread t2 = new Thread(() -> {
			System.out.println("aa");
			System.out.println("aa");
		});

		List<String> list1 = new ArrayList<String>();
		list1.sort((String o1, String o2) -> {
			return o1.compareTo(o2);
		});
		list1.sort((o1, o2) -> {
			return o1.compareTo(o2);
		});
	}
}
