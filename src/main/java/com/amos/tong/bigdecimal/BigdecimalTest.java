package com.amos.tong.bigdecimal;

import java.math.BigDecimal;

public class BigdecimalTest {
	public static void main(String[] args) {
		BigDecimal b1 = new BigDecimal("0.01");
		BigDecimal b2 = new BigDecimal("0.01");
		
		BigDecimal b3 = b1.multiply(b2);
		System.out.println("ss="+b3); // ss=0.0001
		//System.out.println("ss="+b3.setScale(newScale));
		
	}
}
