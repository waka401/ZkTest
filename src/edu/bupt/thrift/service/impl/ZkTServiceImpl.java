package edu.bupt.thrift.service.impl;

/** 
 * @author 孙元成 E-mail:stevesun521@gmail.com 
 * @version 创建时间：2012-6-14 下午9:47:45 
 * 类说明 
 */
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.thrift.TException;
import org.apache.zookeeper.ZooKeeper;

import edu.bupt.cameras.CameraManager;
import edu.bupt.thrift.service.*;
import edu.bupt.zookeeper.beans.CameraX;
import edu.bupt.zookeeper.conf.impl.GConf;
import edu.bupt.zookeeper.conf.impl.MetricsConf;
import edu.bupt.zookeeper.util.MetricsUtil;

public class ZkTServiceImpl implements ZkTService.Iface {
	private Logger logger = Logger.getLogger(ZkTServiceImpl.class);
	// 此处CameraManager 需要考虑同步问题
	private CameraManager cameraManager = null;
	private ZooKeeper zk = null;
	private GConf gConf = null;
	private MetricsConf mConf = null;

	public ZkTServiceImpl(CameraManager cameraManager, ZooKeeper zk) {
		this.cameraManager = cameraManager;
		this.zk = zk;
		gConf = new GConf(zk);
		mConf = new MetricsConf(zk);
	}

	// 手动添加一个serverip的对应的摄像头ip
	@Override
	public boolean addCamera(String serverip, String camip)
			throws TException {
		logger.info("CALL addCamera");
		boolean ret = false;
			if (gConf.addCamera(serverip, camip)) {
				synchronized (cameraManager) {
					ArrayList<String> camlist = new ArrayList<String>(); // TODO
																			// cameraManager接口需要重构
					camlist.add(camip);
					cameraManager.addCamera(camlist);
					logger.info("CALL SERVER addCamera");
				}
				try {
					double now_metics = MetricsUtil.calcMetrics(cameraManager);
					mConf.updateMetricsByIP(serverip, now_metics);
					logger.info("更新metrics");
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				ret = true;
				logger.info("CALL SERVER addCamera over!");
			}
		return ret;
	}

	// 删除服务器serverip管理的一个对应的摄像头ip
	@Override
	public boolean deleteCamera(String serverip, String camip)
			throws TException {
		boolean ret = false;
		logger.info("CALL deleteCamera ...................");
		if (gConf.deleteCamera(serverip, camip)) {
			synchronized (cameraManager) {
				ArrayList<String> camlist = new ArrayList<String>(); // TODO
																		// cameraManager接口需要重构
				camlist.add(camip);
				cameraManager.deleteCamera(camlist);
			}
			logger.info("CALL deleteCamera finishing");
			ret = true;
		} else {
			logger.warn("CALL deteteCamera warning");
		}
		return ret;
	}

	@Override
	public List<String> getAllCamsByServerIP(String serverip) throws TException {
		ArrayList<CameraX> listc = gConf.getCameraXListByServerX(serverip);
		List<String> camips = new ArrayList<String>();
		for (CameraX cameraX : listc) {
			camips.add(cameraX.getCip());
		}
		logger.info("CALL getAllCamsByServerIP");
		return camips;
	}

	@Override
	public boolean deleteServerX(String serverip) throws TException {
		if (gConf.delServerX(gConf.getServerXByIp(serverip)) == true) {
			logger.info("CALL deleteServerX Successful");
			return true;
		}
		return false;
	}

	@Override
	public double getMetrics(String serverip) throws TException {
		Double metrics = mConf.getMetricsByIP(serverip);
		return metrics;
	}

	@Override
	public Map<String, Double> getAllMetrics() throws TException {
		Hashtable<String, Double> metricstable = new Hashtable<String, Double>();
		metricstable = mConf.getMetrisTable();
		return metricstable;
	}

	@Override
	public ServerMetrics getBestMetricsEntry() throws TException {
		String bestserverip = MetricsUtil.getBestServerXIP(mConf
				.getMetrisTable());
		Double metrics = mConf.getMetricsByIP(bestserverip);
		ServerMetrics serverMetrics = new ServerMetrics(bestserverip, metrics);
		return serverMetrics;
	}

	@Override
	public Map<String, Double> getBestMetEntry() throws TException {
		String bestserverip = MetricsUtil.getBestServerXIP(mConf
				.getMetrisTable());
		Double metrics = mConf.getMetricsByIP(bestserverip);
		Hashtable<String, Double> table = new Hashtable<String, Double>();
		table.put(bestserverip, metrics);
		return table;
	}

	// 将一个摄像头从一个节点手动转移到其他节点进行
	@Override
	public boolean transferCam(String fromserver, String toserver, String camip)
			throws TException {
		boolean addret=false,delret=false;
		logger.info("Call transferCam operation start");
		if(addret=addCamera(toserver,camip)){
			logger.info("transferCam add cam operation successfully!");
		}else{
			logger.info("transferCam add cam operation failed!");
		}
		if(delret=deleteCamera(fromserver, camip)){
			logger.info("transferCam delete cam operation successfully!");
		}else{
			logger.info("transferCam delete cam operation failed!");
		}
		logger.info("Call transferCam operation finished");
		return addret&&delret;
	}

}
