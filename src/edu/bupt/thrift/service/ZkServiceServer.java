package edu.bupt.thrift.service;

import org.apache.log4j.Logger;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TBinaryProtocol.Factory;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.server.TThreadPoolServer.Args;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;
import org.apache.zookeeper.ZooKeeper;
import edu.bupt.cameras.CameraManager;
import edu.bupt.thrift.service.impl.ZkTServiceImpl;

/** 
 * @author 孙元成 E-mail:stevesun521@gmail.com 
 * @version 创建时间：2012-6-14 下午10:29:15 
 * 类说明 
 */
public class ZkServiceServer extends Thread {
	 private Logger logger=Logger.getLogger(ZkServiceServer.class);
     private ZooKeeper zk;
     private CameraManager cameraManager;
     public ZkServiceServer(){};
     public ZkServiceServer(ZooKeeper zk,CameraManager cameraManager){
    	 this.zk=zk;
    	 this.cameraManager=cameraManager;
     }
	 public void startServer() {
         try {
             TServerSocket serverTransport = new TServerSocket(1234);
             @SuppressWarnings("rawtypes")
			ZkTService.Processor processor = new ZkTService.Processor(new ZkTServiceImpl(cameraManager,zk));
             Factory portFactory = new TBinaryProtocol.Factory(true, true);
             Args args = new Args(serverTransport);
             args.processor(processor);
             args.protocolFactory(portFactory);
             TServer server = new TThreadPoolServer(args);
             System.out.println("begin to serve");
             logger.info("start ZkServiceServer");
             server.serve();   
         } catch (TTransportException e) {
        	 logger.fatal("start ZkServiceServer failed!!!");
             e.printStackTrace();
         }
	 }
	@Override
	public void run() {
	   this.startServer();
	}

}
