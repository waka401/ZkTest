package edu.bupt.zookeeper.main;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooDefs.Ids;


import edu.bupt.cameras.CameraManager;
import edu.bupt.zookeeper.conf.IGConf;
import edu.bupt.zookeeper.conf.PConf;
import edu.bupt.zookeeper.conf.impl.GConf;
import edu.bupt.zookeeper.conf.impl.MetricsConf;
import edu.bupt.zookeeper.factory.CameraManagerFactory;
import edu.bupt.zookeeper.factory.ZkFactory;
import edu.bupt.zookeeper.monitor.GMonitor;
import edu.bupt.zookeeper.monitor.NodeMonitor;
import edu.bupt.zookeeper.util.MetricsUtil;

/**
 * @author 孙元成 E-mail:stevesun521@gmail.com
 * @version 创建时间：2012-5-5 上午8:41:58 类说明
 */
public class Executor implements Runnable {
	private Logger logger=Logger.getLogger(this.getClass());
	private ZooKeeper zk = null;
	private static final int SESSION_TIMEOUT = 30000;
	private NodeMonitor nm;
	private int num = 0;
	private GMonitor gm;
    private CameraManager cameraManager;
	public Executor(String ip, int port) throws IOException, KeeperException, InterruptedException  {
		       logger.info("初始化CameraManager----------------------------------------");
		       cameraManager=CameraManagerFactory.getCameraManagerInstance();
               initCameraManager(cameraManager); //先初始化cameraManager ,initZk中需要用到
               logger.info("初始化ZK----------------------------------------");
               initZk(ip, port);
               logger.info("初始化weight");
               initWeight();
	}
	//初始化该台主机的权重情况
	public void initWeight(){
		try {
			double metrics=MetricsUtil.calcMetrics(cameraManager);
			String ip=Inet4Address.getLocalHost().getHostAddress();
			MetricsConf metricsConf=new MetricsConf(zk);
			metricsConf.registerMetricsByIP(ip, metrics);
			logger.info("register weight");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
	}
	public void initZk(String ip, int port) throws IOException, KeeperException, InterruptedException{
		String zkurl = ip + ":" + port;
		gm = new GMonitor();
//		zk = new ZooKeeper(zkurl, Executor.SESSION_TIMEOUT, gm);
		zk = ZkFactory.getZkInstance();
		String hostname = Inet4Address.getLocalHost().getHostName();
		String localip = Inet4Address.getLocalHost().getHostAddress();
		String nodedata = hostname + ":" + localip;
		String parentpath=PConf.getValue("parentPath");
		zk.create(parentpath+"/"+hostname, nodedata.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		if (zk.exists(PConf.getValue("globalPath"), gm) == null) {  //如果/xml全局配置不存在，则重新建立
			zk.create(PConf.getValue("globalPath"), PConf.getValue("xmlinit").getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		}
		if (zk.exists(PConf.getValue("metricsPath"), gm) == null) {  //如果/metrics全局配置不存在，则重新建立
			zk.create(PConf.getValue("metricsPath"), PConf.getValue("metricsinit").getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		}
		IGConf gconf=new GConf(zk);
		gconf.initRegister(); //启动后初始化注册本机内容
		nm = new NodeMonitor(zk,parentpath,cameraManager);
		ArrayList<String> serverlist = (ArrayList<String>) zk.getChildren(parentpath, nm);
		for (String server : serverlist) {
			logger.info("server name is :" + server);
		}
	}
	public void initCameraManager(CameraManager cameraManager){
		ArrayList<String> camList=new ArrayList<String>();
		String[] camips=PConf.getValue("initCamsIPS").split(",");
		for (String camip : camips) {
			camList.add(camip);
		}
		cameraManager.addCamera(camList);
	}
	@Override
	public void run() {
		while (true) {
			synchronized (this) {
			//	System.out.println("num-->" + num++);
			}
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	public static void main(String args[]){
		try {
			Executor executor=new Executor(PConf.getValue("zkip"), Integer.parseInt(PConf.getValue("zkport")));
			Thread executorThread=new Thread(executor);
			executorThread.start();
//			new Executor(PConf.getValue("zkip"), Integer.parseInt(PConf.getValue("zkport"))).run();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
