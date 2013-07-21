package edu.bupt.zookeeper.monitor;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import edu.bupt.cameras.CameraManager;
import edu.bupt.zookeeper.beans.CameraX;
import edu.bupt.zookeeper.beans.ServerX;

import edu.bupt.zookeeper.conf.IGConf;
import edu.bupt.zookeeper.conf.PConf;
import edu.bupt.zookeeper.conf.impl.GConf;
import edu.bupt.zookeeper.conf.impl.MetricsConf;
import edu.bupt.zookeeper.util.MetricsUtil;
import edu.bupt.zookeeper.util.ZkUtil;

/**
 * @author 孙元成 E-mail:stevesun521@gmail.com
 * @version 创建时间：2012-5-4 下午10:34:02 类说明
 */
public class NodeMonitor implements Watcher {
	private Logger logger = Logger.getLogger(NodeMonitor.class);
	private int totalnum = 0;
	public boolean dead = false;
	private ZooKeeper zk = null;
	private String parentPath = "";
	private CameraManager cameraManager;
	private ArrayList<String> serverList = new ArrayList<String>();

	public NodeMonitor(ZooKeeper zk, String parentPath,
			CameraManager cameraManager) throws KeeperException,
			InterruptedException {
		this.zk = zk;
		this.parentPath = parentPath;
		this.serverList = (ArrayList<String>) this.zk.getChildren(
				PConf.getValue("parentPath"), false);
		this.totalnum = serverList.size();
		this.cameraManager = cameraManager;
		System.out.println("initial NodeMonitor");
		System.out.println("inital totoal " + totalnum);
	}

	public int getTotalnum() {
		return totalnum;
	}

	public void setTotalnum(int totalnum) {
		this.totalnum = totalnum;
	}

	@Override
	public void process(WatchedEvent event) {
		logger.info("Even happened path" + event.getPath());
		logger.info("before the child num is " + this.totalnum);
		logger.info("Now the child list is");
		ArrayList<String> _nowList = null;
		ArrayList<String> _oldList = this.serverList;
		IGConf gConf = new GConf(zk);
		MetricsConf metricsConf=new MetricsConf(zk);
		try {
			_nowList = (ArrayList<String>) zk.getChildren(parentPath,new NodeMonitor(zk, PConf.getValue("parentPath"),cameraManager));
			int oldtotal = this.totalnum;
			int nowtotal = _nowList.size();
			logger.info("now the child num is " + nowtotal);
			if (oldtotal > nowtotal) {
				ArrayList<String> deadServeNameList = ZkUtil.diff(_nowList,_oldList); // 求出减少的主机名可以时多个
				logger.info("减少了" + deadServeNameList.size() + "节点"+ event.getPath());
				/*
				 * 此处有个bug 减少多个主机时不是最优的
				 */
				ArrayList<ServerX> deadList = new ArrayList<ServerX>();
				for (int i = 0; i < deadServeNameList.size(); i++) {
					deadList.add(gConf.getServerXByServerName(deadServeNameList.get(i)));
				}
				for (ServerX ds:deadList) {
					metricsConf.deleteMetrics(ds.getIp());
					logger.info("删除/metrics中 " + ds.getIp()+ "在/metrics全局中的配置信息");
				}
//				ServerX serverXBest = gConf.findBestServer(deadList); // 选出当前状态下最好的节点
				String bestServerIP=MetricsUtil.getBestServerXIP(metricsConf.getMetrisTable()); //查找当前状态最好的主机
				ServerX serverXBest=gConf.getServerXByIp(bestServerIP);
				String ipLocalStr = Inet4Address.getLocalHost().getHostAddress();
				logger.info("最好的主机IP是"+serverXBest.getIp());
				if (ipLocalStr.equals(serverXBest.getIp())) { // 如果本机是选取出的最好的用于处理减少的节点所对应的主机，
																// 则把挂了的摄像头转移到本机
					logger.info("本机是最优主机 ，将处理宕机的主机名时");
					for (int i = 0; i < deadServeNameList.size(); i++) {
						logger.info("正在处理宕机" + deadServeNameList.get(i));
						ServerX deadServerX = gConf.getServerXByServerName(deadServeNameList.get(i));
						ArrayList<CameraX> deadCams = deadServerX.getList();
						ArrayList<String> deadCamIPsList = new ArrayList<String>();
						// 发送的格式
						for (CameraX cameraX : deadCams) {
							deadCamIPsList.add(cameraX.getCip());
							logger.info("Camera " + cameraX.getCip()+ "转移到本机 CameraManager进行采集 ");
						}
						boolean ret = gConf.insertDeadCamsToServerX(deadCamIPsList, ipLocalStr);
						logger.info("Now CameraManager begin add CamsIP");
						// 1 至此获得了宕机负责的camera ip ，接下来需要把这些camera
						// ip传给选出的bestServer
						// 将这些deadCamIPsList发给本地的采集程序进行采集
						 cameraManager.addCamera(deadCamIPsList);
						logger.info("转移摄像头结束");
						logger.info("删除/xml" + deadServeNameList.get(i)+ "在全局中的配置信息");
						gConf.delServerX(deadServerX);
					}
					//更新本机的新的metrics
					double now_metics=MetricsUtil.calcMetrics(cameraManager);
					metricsConf.updateMetricsByIP(ipLocalStr, now_metics);
					logger.info("更新本机的新的metrics为"+now_metics);
				}
			} else if (oldtotal < nowtotal) {
				logger.info("增加了一个节点" + event.getPath());
			}
			this.totalnum = nowtotal;
			this.serverList = _nowList;
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		if (_nowList != null) {
			for (String server : _nowList) {
				logger.info("server name is :" + server);
			}
		} else {
			logger.info(parentPath + "has no children");
		}
	}

	public void notifyCacheServer(ServerX deadServerX) {
		// 要做如下几步
		// 1 .通知宕机对应的缓存机上传缓存内容
		// 2 .宕机本身缓存着其他主机的摄像头所对应的缓存，需要将这部分缓存任务转移到bestServerX所对应的缓存机进行缓存
	}

}
