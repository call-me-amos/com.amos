package com.amos.tong.hashmap;

import java.util.HashMap;
import java.util.Map;

public class TestHashMap {

 static int i;
	public static void main(String[] args) {
		HashMap<String, String> map =new HashMap<String, String>();
		map.get(null);
		map.put(null, null);
		for (Map.Entry<String, String> entery : map.entrySet()) {
			
			
		}
		
		int MAXIMUM_CAPACITY = 1 << 30;
		System.out.println(MAXIMUM_CAPACITY);//1073741824
		
		System.out.println(Integer.MAX_VALUE);//2147483647 
		System.out.println(Integer.MIN_VALUE);//-2147483648
		System.out.println(1 << 31);//-2147483648
		System.out.println((1 << 31)-1);//2147483647
		
		System.out.println("=================");
		System.out.println(Float.MAX_VALUE);
		final int j;
		
		
		
	}
	
	TestHashMap (){
		this.i=1;
	}
}
