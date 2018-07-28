package com.amos.tong.serializable;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class Forest{


	public static void main(String[] args){

		Forest f = new Forest();

		try{

			FileOutputStream fs = new FileOutputStream("Forest.ser");

			ObjectOutputStream os = new ObjectOutputStream(fs);

			os.writeObject(f);

			os.close();

		}catch(Exception e){
			e.printStackTrace();
//			java.io.NotSerializableException: com.amos.tong.serializable.Forest
//			over !
//				at java.io.ObjectOutputStream.writeObject0(Unknown Source)
//				at java.io.ObjectOutputStream.writeObject(Unknown Source)
//				at com.amos.tong.serializable.Forest.main(Forest.java:19)

		}
		System.out.println("over !");
	}

}

class Tree{
	//private int i=123;
}