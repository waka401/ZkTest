package edu.bupt.cameras;

import java.net.Inet4Address;
import java.util.*;

public abstract class Util {
	// config�ļ����·��
	public static final String CONFIG_PATH = "ips";
	// URLͷ
	public static final String URL_HEADER = "http://";
	// URLβ
	public static final String URL_TAILER = "/axis-cgi/mjpg/video.cgi";
	// ת����ļ���hdfs�����λ��
	public static final String HDFS_PATH_HEADER = "hdfs://server1:8020/user/buptmc/";
	// ת��ǰ�ļ��ڱ��ر���λ��
	public static final String LOCAL_PATH_HEADER = "";
	

	// �������ͷip�������ͷ�������URL
	public static String makeURL(String cameraIp) {
		return Util.URL_HEADER + cameraIp + Util.URL_TAILER;
	}

	// Ϊ�������ɵ��ļ������ʽ�������ڼ�cameraid(sn),�������յ�ʱ��Ҫ��ǰ�����IP
	public static String makeFilename( String cameraIp,int i,String strType) {
		Calendar c = Calendar.getInstance();
		String num=Integer.toString(i);
		return "" + cameraIp+"-"+c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1)
				+ "-" + c.get(Calendar.DAY_OF_MONTH) + "-"
				+ c.get(Calendar.HOUR_OF_DAY) + "-" + c.get(Calendar.MINUTE)
				+ "-" + c.get(Calendar.SECOND) +"-"+num+"." + strType;
	}
}
