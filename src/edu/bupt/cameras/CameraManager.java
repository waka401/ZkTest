package edu.bupt.cameras;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.concurrent.LinkedBlockingQueue;

public class CameraManager implements ICamManager {

	public ArrayList<AxisCamera> cameras = new ArrayList<AxisCamera>();
	public LinkedBlockingQueue<String> assertQueue = new LinkedBlockingQueue<String>();
    private Hashtable<String, CameraThread> camstable=new Hashtable<String, CameraThread>();
	public CameraManager() {

	}
    @Override
	public void addCamera(ArrayList<String> iplist) {
		int n = iplist.size();
		int cameranum = cameras.size();
		for (int i = 0; i < n; i++) {
			String ip = iplist.get(i);
			cameras.add(new AxisCamera(Util.makeURL(ip), ip));
		}
		for (int i = cameranum; i < cameras.size(); i++) {
			CameraThread t = new CameraThread(cameras.get(i), i);
			camstable.put(cameras.get(i).getIP(), t);
			t.start();
		}
	}
	@Override
	public void deleteCamera(ArrayList<String> iplist) {
		// TODO 删除该摄像头对应的摄像头
		int n = iplist.size();
		for (int i = 0; i < n; i++) {
			String ip = iplist.get(i);
			cameras.remove((new AxisCamera(Util.makeURL(ip), ip)));
			Thread cThread=camstable.get(ip);
			cThread.interrupt();//停止线程
		}
	}
}
