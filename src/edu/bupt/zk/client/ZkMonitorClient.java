package edu.bupt.zk.client;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import edu.bupt.thrift.service.ServerMetrics;
import edu.bupt.thrift.service.ZkTService;
import edu.bupt.zookeeper.conf.PConf;
import edu.bupt.zookeeper.conf.impl.MetricsConf;
import edu.bupt.zookeeper.factory.ZkFactory;
import edu.bupt.zookeeper.util.MetricsUtil;

/** 
 * @author 孙元成 E-mail:stevesun521@gmail.com 
 * @version 创建时间：2012-6-14 下午10:29:27 
 * 类说明 
 */
public class ZkMonitorClient implements ZkTService.Iface{
	public ZkMonitorClient(){}
    public void startClient() {
        TTransport transport;
        try {
            transport = new TSocket("59.64.158.62", 1234);
            TProtocol protocol = new TBinaryProtocol(transport);
            ZkTService.Client client = new ZkTService.Client(protocol);
            transport.open();
            
            System.out.println("执行一次ping");
            List<String> list=client.getAllCamsByServerIP("59.64.158.62");
            System.out.println(list.size());
            transport.close();
        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (TException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
    	ZkMonitorClient client = new ZkMonitorClient();
       // client.startClient();
    	try {
			client.addCamera("59.64.158.62", "59.64.156.170");
//    		client.deleteCamera("59.64.158.62", "59.64.156.170");
		} catch (TException e) {
			e.printStackTrace();
		}
    }

	@Override
	public boolean addCamera(String serverip, String camip) throws TException {
		if(serverip.equals("")||serverip==null) return false;
		TTransport transport = new TSocket(serverip, 1234);
		TProtocol protocol = new TBinaryProtocol(transport);
		ZkTService.Client client = new ZkTService.Client(protocol);
		transport.open();
		boolean ret=client.addCamera(serverip, camip);
		transport.close();
		return ret;
	}

	@Override
	public boolean deleteCamera(String serverip, String camip)
			throws TException {
		if(serverip.equals("")||serverip==null) return false;
		TTransport transport = new TSocket(serverip, 1234);
		TProtocol protocol = new TBinaryProtocol(transport);
		ZkTService.Client client = new ZkTService.Client(protocol);
		transport.open();
		boolean ret=client.deleteCamera(serverip, camip);;
		transport.close();
		return ret;
	}

	@Override
	public List<String> getAllCamsByServerIP(String serverip) throws TException {
		if(serverip.equals("")||serverip==null) return null;
		TTransport transport = new TSocket(serverip, 1234);
		TProtocol protocol = new TBinaryProtocol(transport);
		ZkTService.Client client = new ZkTService.Client(protocol);
		transport.open();
		List<String>list=client.getAllCamsByServerIP(serverip);
		transport.close();
		return list;
	}

	@Override
	public boolean deleteServerX(String serverip) throws TException {
		if(serverip.equals("")||serverip==null) return false;
		TTransport transport = new TSocket(serverip, 1234);
		TProtocol protocol = new TBinaryProtocol(transport);
		ZkTService.Client client = new ZkTService.Client(protocol);
		transport.open();
		boolean ret=client.deleteServerX(serverip);
		transport.close();
		return ret;
	}

	@Override
	public double getMetrics(String serverip) throws TException {
		if(serverip.equals("")||serverip==null) return -1.0;
		MetricsConf metricsConf=null;
		try {
			metricsConf = new MetricsConf(ZkFactory.getZkInstance());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		double ret=metricsConf.getMetricsByIP(serverip);
//		TTransport transport = new TSocket(serverip, 1234);
//		TProtocol protocol = new TBinaryProtocol(transport);
//		ZkTService.Client client = new ZkTService.Client(protocol);
//		double ret=client.getMetrics(serverip);
		return ret;
	}

	@Override
	public Map<String, Double> getAllMetrics() throws TException {
		MetricsConf metricsConf=null;
		try {
			metricsConf = new MetricsConf(ZkFactory.getZkInstance());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Hashtable<String, Double> hashtable=(Hashtable<String, Double>) metricsConf.getMetrisTable();
		return hashtable;
	}

	@Override
	public ServerMetrics getBestMetricsEntry() throws TException {
		MetricsConf metricsConf=null;
		try {
			metricsConf = new MetricsConf(ZkFactory.getZkInstance());
		} catch (IOException e) {
			e.printStackTrace();
		}
		Hashtable<String, Double> hashtable=(Hashtable<String, Double>) metricsConf.getMetrisTable();
        String bestServerIP=MetricsUtil.getBestServerXIP(hashtable);
        Double metrics =  metricsConf.getMetricsByIP(bestServerIP);
		ServerMetrics serverMetrics=new ServerMetrics(bestServerIP, metrics);
		return serverMetrics;
	}

	@Override
	public Map<String, Double> getBestMetEntry() throws TException {
		MetricsConf metricsConf=null;
		try {
			metricsConf = new MetricsConf(ZkFactory.getZkInstance());
		} catch (IOException e) {
			e.printStackTrace();
		}
		Hashtable<String, Double> hashtable=(Hashtable<String, Double>) metricsConf.getMetrisTable();
        String bestServerIP=MetricsUtil.getBestServerXIP(hashtable);
        Double metrics =  metricsConf.getMetricsByIP(bestServerIP);
		Hashtable<String, Double> rethash=new Hashtable<String, Double>();
		rethash.put(bestServerIP, metrics);
		return rethash;
	}

	@Override
	public boolean transferCam(String fromserver, String toserver, String camip)
			throws TException {
		// TODO Auto-generated method stub
		return false;
	}
}
