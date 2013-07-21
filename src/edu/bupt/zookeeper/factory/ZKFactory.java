package edu.bupt.zookeeper.factory;

import java.io.IOException;

import org.apache.zookeeper.ZooKeeper;

import edu.bupt.zookeeper.conf.Config;
import edu.bupt.zookeeper.conf.PConf;
import edu.bupt.zookeeper.main.Executor;
import edu.bupt.zookeeper.monitor.GMonitor;

/** 
 * @author 孙元成 E-mail:stevesun521@gmail.com 
 * @version 创建时间：2012-6-5 下午1:43:02 
 * 类说明 
 */
public class ZkFactory {
	 private final static String zkurl=PConf.getValue("zkip")+":"+PConf.getValue("zkport");
     private static ZooKeeper zooKeeper=null;
     public  synchronized static ZooKeeper getZkInstance() throws IOException{
    	 if(zooKeeper==null){
    		 zooKeeper =new ZooKeeper(zkurl, Config.SESSION_TIMEOUT, new GMonitor());
    	 }
    	 return zooKeeper;
     }

}
