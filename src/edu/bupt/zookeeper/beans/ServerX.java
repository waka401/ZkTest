package edu.bupt.zookeeper.beans;

import java.util.ArrayList;

/**
 * @author 孙元成 E-mail:stevesun521@gmail.com
 * @version 创建时间：2012-5-14 下午9:20:50 类说明
 */
public class ServerX implements Comparable<ServerX> {

	private String servername;
	private String ip;
	private ArrayList<CameraX> list;

	public String getServername() {
		return servername;
	}

	@Override
	public int hashCode() {
		return 1;
	}

	@Override
	public boolean equals(Object obj) {
		if(this==obj) return true;
		else if (obj instanceof ServerX) {
			ServerX sx=(ServerX)obj;
			if(this.ip.equals(sx.ip)||this.servername.equals(sx.servername))return true;
		}
		return false;
	}

	public void setServername(String servername) {
		this.servername = servername;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public ArrayList<CameraX> getList() {
		return list;
	}

	public void setList(ArrayList<CameraX> list) {
		this.list = list;
	}

	@Override
	public int compareTo(ServerX o) {
		if(this.list.size()>o.getList().size()) return 1;
		else if(this.list.size()<o.getList().size()) return -1;
		else return 0;
	}


}
