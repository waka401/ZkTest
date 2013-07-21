package edu.bupt.zookeeper.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.log4j.Logger;

/**
 * @author 孙元成 E-mail:stevesun521@gmail.com
 * @version 创建时间：2012-5-27 下午9:24:18 类说明
 */
public class Test {
	static class A {
		public int a;

		public A(int a) {
			this.a = a;
		}

		// @Override
		// public int hashCode() {
		// // TODO Auto-generated method stub
		// return 1;
		// }
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			else if (obj instanceof A) {
				A b = (A) obj;
				if (b.a == this.a)
					return true;
			}
			return false;
		}

	}

	public static void main(String args[]) {
		// ArrayList<A> alist=new ArrayList<A>();
		// alist.add(new A(2));
		// alist.add(new A(3));
		// alist.add(new A(4));
		// alist.add(new A(5));
		// alist.add(new A(6));
		// alist.add(new A(7));
		// ArrayList<A> blist=new ArrayList<A>();
		// blist.add(new A(5));
		// blist.add(new A(7));
		// alist.removeAll(blist);
		//
		// for (A a : alist) {
		// System.out.println(a.a);
		// }
		Hashtable<String, Integer> tm = new Hashtable<String, Integer>();
		tm.put("123123", 5);
		tm.put("22", 6);
		tm.put("33", 3);
		tm.put("1", 10);
		List arrayList = new ArrayList(tm.entrySet());
		for (Object object : arrayList) {
			Map.Entry<String, Integer> entry = (Entry<String, Integer>) object;
//			System.out.println(entry.getKey());
			System.out.println(entry.getValue());
		}
		Collections.sort(arrayList, new Comparator<Object>() {
			@Override
			public int compare(Object o1, Object o2) {
				Map.Entry obj1 = (Map.Entry) o1;
				Map.Entry obj2 = (Map.Entry) o2;
				return ((Integer) obj2.getValue()).compareTo((Integer) obj1
						.getValue());
			}
		});
	    for (Object object : arrayList) {
	    	Entry<String, Integer> entry=(Entry<String, Integer>) object;
	    	System.out.println(entry.getValue());
		}
	
	}
}
