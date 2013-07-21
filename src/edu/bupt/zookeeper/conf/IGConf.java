package edu.bupt.zookeeper.conf;

import java.net.UnknownHostException;
import java.util.ArrayList;

import edu.bupt.zookeeper.beans.CameraX;
import edu.bupt.zookeeper.beans.ServerX;

/**
 * @author 孙元成 E-mail:stevesun521@gmail.com
 * @version 创建时间：2012-5-23 下午10:10:22 类说明
 */
public interface IGConf {

	public abstract void initG();

	public abstract ServerX getServerXByServerName(String servername);

	public abstract ServerX getServerXByIp(String serverIp);

	public abstract boolean registerNewServerX();

	public abstract boolean registerNewServerX(ServerX serverX);

	public abstract int updateCameraXsOnServerX(ServerX serverX);

	public abstract ArrayList<CameraX> getCameraXListByServerX(String serverIp);

	public abstract ServerX findBestServer();

	public abstract boolean delServerX(ServerX serverX);

	public abstract ServerX addAtoB(ServerX deadServerX, ServerX serverXBest);

	public abstract boolean initRegister() throws UnknownHostException;

	public abstract  ServerX findBestServer(ArrayList<ServerX> deadList);

	public abstract boolean insertDeadCamsToServerX(ArrayList<String> camIpList, String serverXip);
	
	public abstract boolean deleteCamera(String serverip, String camip);
	
	public abstract boolean addCamera(String serverip, String camip) ;
	

}