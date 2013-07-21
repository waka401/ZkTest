package edu.bupt.zookeeper.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import edu.bupt.cameras.CameraManager;

/**
 * @author 孙元成 E-mail:stevesun521@gmail.com
 * @version 创建时间：2012-6-12 下午4:46:37 类说明
 */
public class MetricsUtil {
	public static double calcMetrics(CameraManager ca) throws UnknownHostException {
//		Random random=new Random(System.currentTimeMillis());
//		double metrics=random.nextDouble();
//		return metrics;
		double value1 = (ca.cameras.size() * 0.25) / 100;// 带宽占用率
		double value2 = 0;// value值插入的时候从2开始，因为如果是1的话
		String hostname = InetAddress.getLocalHost().getHostName();
		if (hostname == "server2") {
			while (value2 == 0) {
				value2 = Math.random() * 0.1;
			}
		}
		if (hostname == "server10" || hostname == "server11") {
			while (value2 != 1)
				value2 = Math.random() * 0.2 + 0.8;
		} else
			value2 = Math.random() * 0.7 + 0.1;
		return value1 * value2;// 值越小的优先级越高
	}

	public static String getBestServerXIP(Hashtable<String, Double> hashtable) {
		String ip = null;
		if (hashtable.size() <= 0)
			return null;
		List<Entry<String, Double>> list = new ArrayList<Entry<String, Double>>(
				hashtable.entrySet());
		Collections.sort(list, new Comparator() {
			@Override
			public int compare(Object o1, Object o2) {
				Entry<String, Double> entry1 = (Entry<String, Double>) o1;
				Entry<String, Double> entry2 = (Entry<String, Double>) o2;
				return entry1.getValue().compareTo(entry2.getValue());
			}
		});
		for (Entry<String, Double> entry : list) {
			System.err.println(entry.getKey());
		}
		return list.get(0).getKey();
	}

	public static void main(String args[]) {
//		Hashtable<String, Integer> tm = new Hashtable<String, Integer>();
//		tm.put("123123", 5);
//		tm.put("22", 6);
//		tm.put("33", 3);
//		tm.put("1", 10);
	}
}
