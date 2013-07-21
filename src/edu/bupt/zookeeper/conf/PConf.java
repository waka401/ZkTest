package edu.bupt.zookeeper.conf;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/** 
 * @author 孙元成 E-mail:stevesun521@gmail.com 
 * @version 创建时间：2012-5-14 下午7:58:47 
 * 类说明 
 */
public final class PConf {
	private static final String PROPERTY_FILE = "config.properties";
	public static String getValue(String key){
		String ret=null;
		Properties properties=new Properties();
		try {
			InputStream in = new BufferedInputStream(new FileInputStream( PROPERTY_FILE));
		    properties.load(in);
		    ret=properties.getProperty(key);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return ret;
	}
	public static void main(String[] args) {
	  System.out.println(PConf.getValue("parentPath"));
	}

}
