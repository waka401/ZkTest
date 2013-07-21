package edu.bupt.cameras;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Main {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		CameraManager ca = new CameraManager();
		// String a = "59.64.156.170";
		// String b = "59.64.156.235";
		ArrayList<String> s = new ArrayList<String>();
		// s.add(a);
		BufferedReader in = new BufferedReader(new InputStreamReader(
				new FileInputStream("config")));
		String tem = null;
		while ((tem = in.readLine()) != null) {
			s.add(tem);
		}
		ca.addCamera(s);
	}
}
