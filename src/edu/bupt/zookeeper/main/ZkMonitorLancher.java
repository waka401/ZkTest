package edu.bupt.zookeeper.main;

import java.io.IOException;

import org.apache.zookeeper.KeeperException;

import edu.bupt.thrift.service.ZkServiceServer;
import edu.bupt.zookeeper.conf.PConf;
import edu.bupt.zookeeper.factory.CameraManagerFactory;
import edu.bupt.zookeeper.factory.ZkFactory;

/** 
 * @author 孙元成 E-mail:stevesun521@gmail.com 
 * @version 创建时间：2012-6-15 下午3:20:09 
 * 类说明 
 */
public class ZkMonitorLancher {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Executor executor=new Executor(PConf.getValue("zkip"), Integer.parseInt(PConf.getValue("zkport")));
			Thread executorThread=new Thread(executor);
			ZkServiceServer zkServiceServer=new ZkServiceServer(ZkFactory.getZkInstance(), CameraManagerFactory.getCameraManagerInstance());
			executorThread.start();
			zkServiceServer.start();
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
