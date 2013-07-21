package edu.bupt.cameras;

import java.net.*;
import java.io.*;

public class AxisCamera extends Camera{
	private String strURL;
	private String strIP;

	public AxisCamera(String strURL, String strIP) {
		this.strURL = strURL;
		this.strIP = strIP;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof AxisCamera))
			return false;
		AxisCamera p = (AxisCamera)obj;
		return strIP.equals(p.getIP()); 
	}

	public void setURL(String strURL) {
		this.strURL = strURL;
	}

	public String getURL() {
		return this.strURL;
	}

	public String getIP() {
		return this.strIP;
	}

	public InputStream getInputStream() {
		try {
			URL url = new URL(this.strURL);
			HttpURLConnection conn;
			conn = (HttpURLConnection) url.openConnection();
			return conn.getInputStream();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
