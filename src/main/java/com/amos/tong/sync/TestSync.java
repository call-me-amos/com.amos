package com.amos.tong.sync;

public class TestSync {
	public static void main(String[] args) {
		MyThread t1 = new MyThread();
		MyThread t2 = new MyThread();
		MyThread t3 = new MyThread();
		t1.start();
		t2.start();
		t3.start();
		System.out.println("over");
	}
}
class MyThread extends Thread{

	@Override
	public synchronized void run() {
		// 实例级别的锁
		System.out.println(this.getName()+"开始。。。");
		test ();// 断点 A
	}
	
	public synchronized static void test () {
		// 对象级别的锁
		int a=123;
		System.out.println("a="+a);
		a = 456;
		int b=456;// 断点 B
		System.out.println("a+b="+a+b);
	}
}