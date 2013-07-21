package edu.bupt.zookeeper.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import edu.bupt.zookeeper.beans.ServerX;

/** 
 * @author 孙元成 E-mail:stevesun521@gmail.com 
 * @version 创建时间：2012-5-14 下午9:04:03 
 * 类说明 
 */
public class XmlUtil {
	public static String xmlChangeString(String fileName) {
		try {
			SAXReader saxReader = new SAXReader();
			InputStream is = new BufferedInputStream(new FileInputStream(
					new File(fileName)));
			Document tempDocument = saxReader.read(is);
			return tempDocument.asXML();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void strChangeXML(String str) throws IOException {
		SAXReader saxReader = new SAXReader();
		Document document;
		try {
			document = saxReader.read(new ByteArrayInputStream(str.getBytes("UTF-8")));
			OutputFormat format = OutputFormat.createPrettyPrint();
			/** 将document中的内容写入文件中 */
			XMLWriter writer = new XMLWriter(new FileWriter(new File("D:\\output.xml")), format);
			writer.write(document);
			writer.close();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
    }
	public static ServerX XMLToServerX(String xmlfileName){
		ServerX serverX=new ServerX();
		SAXReader saxReader = new SAXReader();
		Document document;
		try {
			document=saxReader.read(new File(xmlfileName));
			
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    return serverX;
	}
}
