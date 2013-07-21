package edu.bupt.zookeeper.conf.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import edu.bupt.zookeeper.beans.CameraX;
import edu.bupt.zookeeper.beans.ServerX;
import edu.bupt.zookeeper.conf.IGConf;
import edu.bupt.zookeeper.conf.PConf;
import edu.bupt.zookeeper.monitor.GMonitor;
import edu.bupt.zookeeper.util.XmlUtil;

/**
 * @author 孙元成 E-mail:stevesun521@gmail.com
 * @version 创建时间：2012-5-21 下午8:15:38 类说明
 * @description 根据zk路径 读取全局配置信息
 */
/**
 * @author Damon
 * 
 */
public class GConf implements IGConf {
	private Logger logger = Logger.getLogger(this.getClass());
	private ZooKeeper zk;

	public GConf(ZooKeeper zk) {
		this.zk = zk;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.bupt.zookeeper.conf.IGConf#initG()
	 */
	@Override
	public void initG() {
		String initConfStr = XmlUtil.xmlChangeString(PConf
				.getValue("initLocalConf"));
		try {
			zk.setData(PConf.getValue("globalPath"), initConfStr.getBytes(), -1);
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.bupt.zookeeper.conf.IGConf#getServerXByServerName(java.lang.String)
	 */
	@Override
	public ServerX getServerXByServerName(String servername) {
		ServerX serverX = null;
		SAXReader reader = new SAXReader();
		Document xmlDoc;
		try {
			InputStream is = new ByteArrayInputStream(zk.getData(
					PConf.getValue("globalPath"), new GMonitor(), new Stat()));
			xmlDoc = reader.read(is);
			Element root = xmlDoc.getRootElement();
			List<Element> elements = root.selectNodes("//server");
			for (Element element : elements) {
				if (element.elementText("server-name").equals(servername)) {
					serverX = new ServerX();
					serverX.setIp(element.elementText("server-ip"));
					serverX.setServername(element.elementText("server-name"));
					ArrayList<CameraX> camList = new ArrayList<CameraX>();
					List<Element> elements2 = element.element("cameras")
							.elements();
					for (Element e : elements2) {
						CameraX cameraX = new CameraX();
						cameraX.setCip(e.elementText("c-ip"));
						camList.add(cameraX);
					}
					serverX.setList(camList);
					break;
				}
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (KeeperException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return serverX;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.bupt.zookeeper.conf.IGConf#getServerXByIp(java.lang.String)
	 */
	@Override
	public ServerX getServerXByIp(String serverIp) { // 根据serverIP返回serverX对象
		ServerX serverX = null;
		SAXReader reader = new SAXReader();
		Document xmlDoc;
		try {
			InputStream is = new ByteArrayInputStream(zk.getData(
					PConf.getValue("globalPath"), new GMonitor(), new Stat()));
			xmlDoc = reader.read(is);
			Element root = xmlDoc.getRootElement();
			List<Element> elements = root.selectNodes("//server");
			for (Element element : elements) {
				if (element.elementText("server-ip").equals(serverIp)) {
					serverX = new ServerX();
					serverX.setIp(serverIp);
					serverX.setServername(element.elementText("server-name"));
					ArrayList<CameraX> camList = new ArrayList<CameraX>();
					List<Element> elements2 = element.element("cameras")
							.elements();
					for (Element e : elements2) {
						CameraX cameraX = new CameraX();
						cameraX.setCip(e.elementText("c-ip"));
						camList.add(cameraX);
					}
					serverX.setList(camList);
					break;
				}
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (KeeperException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return serverX;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.bupt.zookeeper.conf.IGConf#registerNewServerX()
	 */
	@Override
	public boolean registerNewServerX() { // 使用本地init.xml进行注册新节点
		boolean ret = false;
		File xmlFile = new File(PConf.getValue("initLocalConf"));
		SAXReader reader = new SAXReader();
		Document xmlDoc; // 本地init.xml
		ServerX serverX = new ServerX();
		try {
			xmlDoc = reader.read(xmlFile);
			Element root = xmlDoc.getRootElement();
			List<Element> list = root.selectNodes("//server");
			for (Element element : list) {
				String localIP = Inet4Address.getLocalHost().getHostAddress();
				// serverX.setIp(element.elementText("server-ip"));
				serverX.setIp(localIP);
				String localServerName = Inet4Address.getLocalHost()
						.getHostName();
				// serverX.setServername(element.elementText("server-name"));
				serverX.setServername(localServerName);
				List<Element> oneList = element.element("cameras").elements(
						"camera");
				ArrayList<CameraX> camList = new ArrayList<CameraX>();
				for (Element ele : oneList) {
					camList.add(new CameraX(ele.elementText("c-ip")));
				}
				serverX.setList(camList);
				break;
			}
			ret = registerNewServerX(serverX);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.bupt.zookeeper.conf.IGConf#registerNewServerX(edu.bupt.zookeeper.
	 * beans.ServerX)
	 */
	@Override
	public boolean initRegister() throws UnknownHostException {
		boolean ret = false;
		String localIP = Inet4Address.getLocalHost().getHostAddress();
		String localHostName = Inet4Address.getLocalHost().getHostName();
		if (getServerXByIp(localIP) == null) { // 如果已有主机则不注册
			SAXReader reader = new SAXReader();
			Document xmlDoc;
			try {
				InputStream is = new ByteArrayInputStream(zk.getData(
						PConf.getValue("globalPath"), new GMonitor(),
						new Stat()));
				xmlDoc = reader.read(is);
				Element root = xmlDoc.getRootElement();
				List<Element> elements = root.elements();
				Element serverElement = DocumentHelper.createElement("server");
				Element snElement = DocumentHelper.createElement("server-name");
				snElement.setText(localHostName);
				Element sipElement = DocumentHelper.createElement("server-ip");
				sipElement.setText(localIP);
				Element camsElement = DocumentHelper.createElement("cameras");
				String[] initCamsIPS = PConf.getValue("initCamsIPS").split(",");
				int cams_num = initCamsIPS.length;
				for (int i = 0; i < cams_num; i++) {
					Element onecam = DocumentHelper.createElement("camera");
					Element cam_cip = DocumentHelper.createElement("c-ip");
					cam_cip.setText(initCamsIPS[i]);
					onecam.add(cam_cip);
					camsElement.add(onecam);
				}
				serverElement.add(snElement);
				serverElement.add(sipElement);
				serverElement.add(camsElement);
				elements.add(elements.size(), serverElement);
				String xmlString = xmlDoc.asXML();
				zk.setData(PConf.getValue("globalPath"), xmlString.getBytes(),
						-1);
				logger.info("注册成功" + localHostName);
				ret = true;
			} catch (KeeperException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else { // 如果已经存在则重新注册
			ServerX serverX = getServerXByIp(localIP);
			if (delServerX(serverX)) {
				logger.info("删除节点" + serverX.getServername());
			}
			if (initRegister()) {
				logger.info("已经重新注册" + serverX.getServername());
			}
		}
		return ret;
	}

	@Override
	public boolean registerNewServerX(ServerX serverX) { // 注册后重新写入到zk
		boolean ret = false;
		if (getServerXByIp(serverX.getIp()) == null) {
			SAXReader reader = new SAXReader();
			Document xmlDoc;
			try {
				InputStream is = new ByteArrayInputStream(zk.getData(
						PConf.getValue("globalPath"), new GMonitor(),
						new Stat()));
				xmlDoc = reader.read(is);
				Element root = xmlDoc.getRootElement();
				List<Element> elements = root.elements();
				Element serverElement = DocumentHelper.createElement("server");
				Element snElement = DocumentHelper.createElement("server-name");
				snElement.setText(serverX.getServername());
				Element sipElement = DocumentHelper.createElement("server-ip");
				sipElement.setText(serverX.getIp());
				Element camsElement = DocumentHelper.createElement("cameras");
				int cams_num = serverX.getList().size();
				for (int i = 0; i < cams_num; i++) {
					Element onecam = DocumentHelper.createElement("camera");
					Element cam_cip = DocumentHelper.createElement("c-ip");
					cam_cip.setText(serverX.getList().get(i).getCip());
					onecam.add(cam_cip);
					camsElement.add(onecam);
				}
				serverElement.add(snElement);
				serverElement.add(sipElement);
				serverElement.add(camsElement);
				elements.add(elements.size() - 1, serverElement);
				String xmlString = xmlDoc.asXML();
				zk.setData(PConf.getValue("globalPath"), xmlString.getBytes(),
						-1);
				ret = true;
			} catch (DocumentException e) {
				e.printStackTrace();
			} catch (KeeperException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			System.out.println("is Exsit");
		}
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.bupt.zookeeper.conf.IGConf#updateCameraXsOnServerX(edu.bupt.zookeeper
	 * .beans.ServerX)
	 */
	@Override
	public int updateCameraXsOnServerX(ServerX serverX) {
		ArrayList<CameraX> oriCamsList = getCameraXListByServerX(serverX
				.getIp()); // 更新前对于serverX所对应的cameras列表
		ArrayList<CameraX> nowCamsList = serverX.getList(); // 当前serverX中的cameras列表
		ArrayList<CameraX> tempCamsList = new ArrayList<CameraX>(); // 多出的ip
		System.out.println(oriCamsList.size());
		System.out.println(nowCamsList.size());
		for (int i = 0; i < nowCamsList.size(); i++) {
			System.out.println(nowCamsList.get(i).getCip());
			int j = 0;
			for (; j < oriCamsList.size(); j++) {
				if (nowCamsList.get(i).getCip()
						.equals(oriCamsList.get(j).getCip())) {
					System.out.println(nowCamsList.get(i).getCip() + "="
							+ oriCamsList.get(j).getCip());
					continue;
				}
			}
			System.out.println("---------" + j);
			if (j == oriCamsList.size()) {
				tempCamsList.add(nowCamsList.get(i));
				System.out.println("add :" + nowCamsList.get(i).getCip());
			}
		}
		int num = tempCamsList.size();
		SAXReader reader = new SAXReader();
		Document xmlDoc;
		try {
			InputStream is = new ByteArrayInputStream(zk.getData(
					PConf.getValue("globalPath"), new GMonitor(), new Stat()));
			xmlDoc = reader.read(is);
			Element root = xmlDoc.getRootElement(); // Get the root node of XML
			List<Element> elements = root.selectNodes("//server");
			for (Element element : elements) {
				if (element.elementText("server-ip").equals(serverX.getIp())) {
					for (int i = 0; i < tempCamsList.size(); i++) {
						List<Element> oneCamList = element.element("cameras")
								.elements("camera");
						Element onecam = DocumentHelper.createElement("camera");
						Element cipEle = DocumentHelper.createElement("c-ip");
						cipEle.setText(tempCamsList.get(i).getCip());
						onecam.add(cipEle);
						oneCamList.add(oneCamList.size(), onecam);
					}
					break;
				}
			}
			String xmlString = xmlDoc.asXML();
			zk.setData(PConf.getValue("globalPath"), xmlString.getBytes(), -1);
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return num;
	}

	@Override
	public ArrayList<CameraX> getCameraXListByServerX(String serverIp) {
		ServerX serverX = getServerXByIp(serverIp);
		if (serverX != null) {
			return serverX.getList();
		}
		return null;
	}

	@Override
	public ServerX findBestServer() {
		ArrayList<ServerX> serverList = new ArrayList<ServerX>();
		ServerX serverX = null;
		// File xmlFile = new File("D:/global.xml");
		SAXReader reader = new SAXReader();
		Document xmlDoc;
		try {
			InputStream is = new ByteArrayInputStream(zk.getData(
					PConf.getValue("globalPath"), new GMonitor(), new Stat()));
			xmlDoc = reader.read(is);
			Element root = xmlDoc.getRootElement(); // Get the root node of XML
			List<Element> elements = root.selectNodes("//server");
			for (Element element : elements) {
				System.out.println(element.elementText("server-ip"));
				serverX = new ServerX();
				serverX.setIp(element.elementText("server-ip"));
				serverX.setServername(element.elementText("server-name"));
				ArrayList<CameraX> camList = new ArrayList<CameraX>();
				List<Element> elements2 = element.element("cameras").elements();
				for (Element e : elements2) {
					CameraX cameraX = new CameraX();
					System.out.println(e.elementText("c-ip"));
					cameraX.setCip(e.elementText("c-ip"));
					camList.add(cameraX);
				}
				serverX.setList(camList);
				serverList.add(serverX);
				System.out.println(elements2.size());
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (serverList.size() > 0) { // 排序取出cameras数目最少的serverX
			Collections.sort(serverList);
		}
		serverX = serverList.get(0);
		return serverX;
	}

	@Override
	public ServerX findBestServer(ArrayList<ServerX> deadList) {
		ArrayList<ServerX> serverList = new ArrayList<ServerX>();
		ServerX serverX = null;
		// File xmlFile = new File("D:/global.xml");
		SAXReader reader = new SAXReader();
		Document xmlDoc;
		try {
			InputStream is = new ByteArrayInputStream(zk.getData(
					PConf.getValue("globalPath"), new GMonitor(), new Stat()));
			xmlDoc = reader.read(is);
			Element root = xmlDoc.getRootElement(); // Get the root node of XML
			List<Element> elements = root.selectNodes("//server");
			for (Element element : elements) {
				System.out.println(element.elementText("server-ip"));
				serverX = new ServerX();
				serverX.setIp(element.elementText("server-ip"));
				serverX.setServername(element.elementText("server-name"));
				ArrayList<CameraX> camList = new ArrayList<CameraX>();
				List<Element> elements2 = element.element("cameras").elements();
				for (Element e : elements2) {
					CameraX cameraX = new CameraX();
					System.out.println(e.elementText("c-ip"));
					cameraX.setCip(e.elementText("c-ip"));
					camList.add(cameraX);
				}
				serverX.setList(camList);
				serverList.add(serverX);
				System.out.println(elements2.size());
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		serverList.removeAll(deadList);
		if (serverList.size() > 0) { // 排序取出cameras数目最少的serverX
			Collections.sort(serverList);
		}
		serverX = serverList.get(0);
		return serverX;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.bupt.zookeeper.conf.IGConf#delDeadServerX(edu.bupt.zookeeper.beans
	 * .ServerX)
	 */
	@Override
	public boolean delServerX(ServerX serverX) {
		boolean ret = false;
		SAXReader reader = new SAXReader();
		Document xmlDoc;
		try {
			InputStream is = new ByteArrayInputStream(zk.getData(
					PConf.getValue("globalPath"), new GMonitor(), new Stat()));
			xmlDoc = reader.read(is);
			Element root = xmlDoc.getRootElement(); // Get the root node of XML
			List<Element> elements = root.selectNodes("//server");
			int len = elements.size();
			for (int i = len - 1; i >= 0; i--) {
				if (elements.get(i).elementText("server-ip")
						.equals(serverX.getIp())
						|| elements.get(i).elementText("server-name")
								.equals(serverX.getServername())) {
					System.out.println("delete +" + i);
					root.remove(elements.get(i));
				}
			}
			String xmlString = xmlDoc.asXML();
			zk.setData(PConf.getValue("globalPath"), xmlString.getBytes(), -1);
			ret = true;
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return ret;
	}

	public ServerX addAtoB(ServerX serverXA, ServerX serverXB) { // 将A中的cameras
																	// 加到B中

		if (serverXA.getList().size() > 0) {
			for (CameraX cameraX : serverXA.getList()) {
				serverXB.getList().add(cameraX);
			}
		}
		return serverXB;
	}

	public static void main(String args[]) throws IOException {
		 ZooKeeper zk = new ZooKeeper("59.64.156.120:2181", 30000,
		 new GMonitor());
		 IGConf gConf=new GConf(zk);
		 gConf.deleteCamera("59.64.156.113", "1.1.1.1");
		// ServerX serverX=new ServerX();
		// serverX.setIp("59.64.156.112");
		// GConf.delDeadServerX(zk, serverX);
		// GConf.initG(zk);
		// GConf.getServerXByIp(zk,"59.64.156.110");
		// ServerX serverX = GConf.findBestServer();
		// System.out.println(serverX.getServername());

		// ServerX serverX2=new ServerX();
		// serverX2.setIp("192.168.1.1");
		// serverX2.setServername("sunyuancheng");
		// ArrayList<CameraX> camlist=new ArrayList<CameraX>();
		// camlist.add(new CameraX("129.1.1.1"));
		// camlist.add(new CameraX("129.1.1.11"));
		// camlist.add(new CameraX("129.1.1.2"));
		// camlist.add(new CameraX("129.1.1.4"));
		// camlist.add(new CameraX("129.1.1.7"));
		// camlist.add(new CameraX("129.1.1.9"));
		// camlist.add(new CameraX("129.1.1.111"));
		// camlist.add(new CameraX("129.1.1.222"));
		// serverX2.setList(camlist);
		// int num=GConf.updateCameraXsOnServerX(zk, serverX2);
		// System.out.println(num);

		// boolean ret=GConf.registerNewServerX(zk,serverX2);
		// System.out.println(ret);
	}

	@Override
	public boolean insertDeadCamsToServerX(ArrayList<String> camIpList,
			String serverXip) {
		SAXReader reader = new SAXReader();
		Document xmlDoc;
		try {
			InputStream is = new ByteArrayInputStream(zk.getData(
					PConf.getValue("globalPath"), new GMonitor(), new Stat()));
			xmlDoc = reader.read(is);
			Element root = xmlDoc.getRootElement();
			List<Element> elements = root.selectNodes("//server");
			for (Element element : elements) {
				if (element.elementText("server-ip").equals(serverXip)) {
					List<Element> oneCamList = element.element("cameras")
							.elements("camera");
					int insertCamNum = camIpList.size();
					for (int j = 0; j < insertCamNum; j++) {
						Element onecam = DocumentHelper.createElement("camera");
						Element cipEle = DocumentHelper.createElement("c-ip");
						cipEle.setText(camIpList.get(j));
						onecam.add(cipEle);
						oneCamList.add(oneCamList.size(), onecam);
					}
					break;
				}
			}
			String xmlString = xmlDoc.asXML();
			zk.setData(PConf.getValue("globalPath"), xmlString.getBytes(), -1);
			return true;
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (KeeperException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
   //删除一个server上的一个摄像头ip
	@Override
	public boolean deleteCamera(String serverip, String camip) {
		logger.info("Call gConf deleteCamera");
		boolean ret = false;
		SAXReader reader = new SAXReader();
		Document xmlDoc;
		try {
			InputStream is = new ByteArrayInputStream(zk.getData(
					PConf.getValue("globalPath"), new GMonitor(), new Stat()));
			xmlDoc = reader.read(is);
			Element root = xmlDoc.getRootElement(); // Get the root node of XML
			List<Element> elements = root.selectNodes("//server");
			for (Element element : elements) {
				if (element.elementText("server-ip").equals(serverip)){
					logger.info("Call gConf "+element.elementText("server-ip"));
					List<Element> oneCamList = element.element("cameras").elements("camera");
					int len=oneCamList.size();
					for (int i = len-1; i >=0; i++) {
						if(oneCamList.get(i).elementText("c-ip").equals(camip)){
							oneCamList.remove(i);
							logger.info("删除 调用deleteCamera");
							System.out.println("删除 调用deleteCamera");
							break;
						}
					}
					break;
				}
			}
			String xmlString = xmlDoc.asXML();
			zk.setData(PConf.getValue("globalPath"), xmlString.getBytes(), -1);
			ret = true;
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return ret;
	}
    //添加一个摄像头的ip到指定的serverx
	@Override
	public boolean addCamera(String serverip, String camip) {
		boolean ret=false;
		if (serverip.equals("") || serverip == null || camip.equals("")
				|| camip == null)
			return false;
		SAXReader reader = new SAXReader();
		Document xmlDoc;
		try {
			InputStream is = new ByteArrayInputStream(zk.getData(PConf.getValue("globalPath"), new GMonitor(), new Stat()));
			xmlDoc = reader.read(is);
			Element root=xmlDoc.getRootElement();
			List<Element> elements = root.selectNodes("//server");
			for (Element element : elements) {
				if (element.elementText("server-ip").equals(serverip)){
					List<Element> oneCamList = element.element("cameras").elements("camera");
//				    int i=0;
//				    for(;i<oneCamList.size()&&oneCamList.get(i).elementText("c-ip").equals(camip);i++);
//				    if(i==oneCamList.size()){ //不含有这个ip则加入，如果含有则跳出
//                 实验测试时摄像头的ip时重复的
				    Element newCamElement=DocumentHelper.createElement("camera");
				    Element newCamipElement=DocumentHelper.createElement("c-ip");
				    newCamipElement.setText(camip);
				    newCamElement.add(newCamipElement);
				    oneCamList.add(oneCamList.size(),newCamElement);
				    String xmlString = xmlDoc.asXML();
					zk.setData(PConf.getValue("globalPath"), xmlString.getBytes(),-1);
					ret=true;
					break;
//				    } 
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

}
