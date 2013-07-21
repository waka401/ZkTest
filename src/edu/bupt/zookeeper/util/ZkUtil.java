package edu.bupt.zookeeper.util;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author 孙元成 E-mail:stevesun521@gmail.com
 * @version 创建时间：2012-5-23 下午11:56:19 类说明
 */
public class ZkUtil {
	public static ArrayList<String> diff(ArrayList<String> shotList,ArrayList<String> longList) {
		ArrayList<String> deadNameList = new ArrayList(longList);
		deadNameList.removeAll(shotList);
		
		return deadNameList;
	}

	public static void main(String args[]) {
		ArrayList<String> longList = new ArrayList<String>();
		longList.add("a");
		longList.add("b");
		longList.add("c");
		longList.add("d");
		longList.add("e");
		ArrayList<String> shortList = new ArrayList<String>();
		shortList.add("a");
		shortList.add("b");
		shortList.add("c");
		ArrayList<String> diffList = ZkUtil.diff(shortList, longList);
		for (String string : diffList) {
			System.out.println(string);
		}
	}

}
