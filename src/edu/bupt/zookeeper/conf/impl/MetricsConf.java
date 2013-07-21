package edu.bupt.zookeeper.conf.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jets3t.service.multithread.UpdateACLEvent;

import edu.bupt.zookeeper.conf.PConf;
import edu.bupt.zookeeper.monitor.GMonitor;
import edu.bupt.zookeeper.util.MetricsUtil;

/**
 * @author 孙元成 E-mail:stevesun521@gmail.com
 * @version 创建时间：2012-6-12 下午4:09:02 类说明
 */
public class MetricsConf {
	private ZooKeeper zk;

	public MetricsConf(ZooKeeper zk) {
		this.zk = zk;
	}

	public double getMetricsByIP(String ip) {
		System.out.println("call getMetricsByIP"+ip);
		SAXReader reader = new SAXReader();
		Document xmlDoc;
		double ret = 0.0;
		try {
			InputStream is = new ByteArrayInputStream(zk.getData(
					PConf.getValue("metricsPath"), new GMonitor(), new Stat()));
			xmlDoc = reader.read(is);
			Element root = xmlDoc.getRootElement();
			List<Element> elements = root.selectNodes("//server");
			for (Element ele : elements) {
				if (ele.elementText("server-ip").equals(ip)) {
					ret = Double.parseDouble(ele.elementText("weight"));
					break;
				}
			}
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return ret;
	}
    public Hashtable<String, Double> getMetrisTable(){
    	Hashtable<String, Double> metricTable=new Hashtable<String, Double>();
    	SAXReader reader = new SAXReader();
		Document xmlDoc;
		try {
			InputStream is = new ByteArrayInputStream(zk.getData(PConf.getValue("metricsPath"), new GMonitor(), new Stat()));
			xmlDoc=reader.read(is);
			Element root=xmlDoc.getRootElement();
			List<Element> elements=root.selectNodes("//server");
			for (Element element : elements) {
				metricTable.put(element.elementText("server-ip"), Double.parseDouble(element.elementText("weight")));
			}
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
    	return metricTable;
    }
	public boolean updateMetricsByIP(String ip, double metrics) {
		SAXReader reader = new SAXReader();
		Document xmlDoc;
		boolean ret = false;
		try {
			InputStream is = new ByteArrayInputStream(zk.getData(PConf.getValue("metricsPath"), new GMonitor(), new Stat()));
			xmlDoc = reader.read(is);
			Element root = xmlDoc.getRootElement();
			List<Element> elements = root.selectNodes("//server");
			for (Element ele : elements) {
				if (ele.elementText("server-ip").equals(ip)) {
					ele.element("weight").setText(String.valueOf(metrics));
					ret=true;
					break;
				}
			}
			String xmlString = xmlDoc.asXML();
			zk.setData(PConf.getValue("metricsPath"), xmlString.getBytes(), -1);
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return ret;
	}
	public boolean isExist(String ip){
		boolean ret=false;
		SAXReader reader = new SAXReader();
		Document xmlDoc;
		try {
			InputStream is = new ByteArrayInputStream(zk.getData(PConf.getValue("metricsPath"), new GMonitor(), new Stat()));
			xmlDoc = reader.read(is);
			Element root = xmlDoc.getRootElement();
			List<Element> elements = root.selectNodes("//server");
			for (Element ele : elements) {
				if (ele.elementText("server-ip").equals(ip)) {
					ret = true;
					break;
				}
			}
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return ret;
	}
	public boolean deleteMetrics(String ip){
		SAXReader reader = new SAXReader();
		Document xmlDoc;
		boolean ret = false;
		try {
			InputStream is = new ByteArrayInputStream(zk.getData(PConf.getValue("metricsPath"), new GMonitor(), new Stat()));
			xmlDoc = reader.read(is);
			Element root = xmlDoc.getRootElement();
			List<Element> elements = root.selectNodes("//server");
			for (int i=elements.size()-1;i>0;i--) {
				if (elements.get(i).elementText("server-ip").equals(ip)) {
					System.out.println(ip);
					root.remove(elements.get(i));
					ret=true;
					break;
				}
			}
			String xmlString = xmlDoc.asXML();
			zk.setData(PConf.getValue("metricsPath"), xmlString.getBytes(), -1);
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return ret;
	}
	public void registerMetricsByIP(String ip, double metrics) {
		if(isExist( ip)==false){
			System.out.println("存在");
			SAXReader reader = new SAXReader();
			Document xmlDoc;
			try {
				InputStream is = new ByteArrayInputStream(zk.getData(PConf.getValue("metricsPath"), new GMonitor(), new Stat()));
				xmlDoc=reader.read(is);
				Element root = xmlDoc.getRootElement();
				List<Element> elements = root.elements();
				Element serverElement = DocumentHelper.createElement("server");
				Element snElement = DocumentHelper.createElement("server-ip");
				snElement.setText(ip);
				Element wElement = DocumentHelper.createElement("weight");
				wElement.setText(String.valueOf(metrics));
				serverElement.add(snElement);
				serverElement.add(wElement);
				elements.add(elements.size(),serverElement);
				String xmlString = xmlDoc.asXML();
				zk.setData(PConf.getValue("metricsPath"), xmlString.getBytes(),-1);
			} catch (KeeperException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (DocumentException e) {
				e.printStackTrace();
			}
		}
	}
	public static void main(String args[]) {
		try {
			ZooKeeper zk = new ZooKeeper("59.64.156.120:2181", 30000,new GMonitor());
			MetricsConf mc = new MetricsConf(zk);
//			double mString = mc.getMetricsByIP("59.64.158.6");
//			System.out.println(mString);
//			mc.updateMetricsByIP("59.64.158.246", 1.44);
			double mString = mc.getMetricsByIP("59.64.157.115");
			System.out.println(mString);
//			System.out.println(mString);
//			Hashtable<String , Double> hashtable=mc.getMetrisTable();
//			String ip=MetricsUtil.getBestServerXIP(hashtable);
//			System.out.println(ip);
//			mc.deleteMetrics("59.64.158.62");
//			mc.registerMetricsByIP("1.1.1.1", 0.74);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
