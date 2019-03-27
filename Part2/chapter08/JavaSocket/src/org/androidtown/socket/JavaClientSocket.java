package org.androidtown.socket;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * 표준 자바로 만든 클라이언트 소켓
 * 
 * 1. 서버에 연결한 후,
 * 2. 문자열을 Object 타입으로 보내고,
 * 3. Object를 받아 출력함.
 * 
 * @author Mike
 *
 */
public class JavaClientSocket {
	
	public static void main(String[] args) {

		try {
			Socket aSocket = new Socket("localhost", 11001);
			System.out.println("클라이언트 소켓이 만들어졌습니다. 호스트 : localhost, 포트 : 11001");
			
			ObjectOutputStream outstream = new ObjectOutputStream(aSocket.getOutputStream());
			outstream.writeObject("Hello.");
			outstream.flush();
			System.out.println("보낸 데이터 : Hello.");

			ObjectInputStream instream = new ObjectInputStream(aSocket.getInputStream());
			Object obj = instream.readObject();
			System.out.println("받은 데이터 : " + obj);
			
			aSocket.close();
			System.out.println("소켓 닫음.");

		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
}
