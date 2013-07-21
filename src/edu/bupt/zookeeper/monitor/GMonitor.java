package edu.bupt.zookeeper.monitor;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

/** 
 * @author 孙元成 E-mail:stevesun521@gmail.com 
 * @version 创建时间：2012-5-5 上午10:06:52 
 * 类说明 一个空监听 
 */
public class GMonitor implements Watcher {

	@Override
	public void process(WatchedEvent event) {
		System.out.println(event.getPath()+" "+this.getClass().getName());

	}

}
