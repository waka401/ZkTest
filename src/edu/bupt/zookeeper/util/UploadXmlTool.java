package edu.bupt.zookeeper.util;

import java.io.IOException;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooDefs.Ids;

import edu.bupt.zookeeper.monitor.GMonitor;

/**
 * @author 孙元成 E-mail:stevesun521@gmail.com
 * @version 创建时间：2012-6-5 下午1:40:25 类说明
 */
public class UploadXmlTool {
	public static final int SESSION_TIMEOUT = 30000;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String xmlfile = "global.xml";
		String content = XmlUtil.xmlChangeString(xmlfile);
		String zkurl = "59.64.156.120:2181";
		try {
			ZooKeeper zk = new ZooKeeper(zkurl, UploadXmlTool.SESSION_TIMEOUT,
					new GMonitor());
			zk.create("/xml", content.getBytes(), Ids.OPEN_ACL_UNSAFE,
					CreateMode.PERSISTENT);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}
