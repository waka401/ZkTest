package edu.bupt.zookeeper.test;

/**
 * @author 孙元成 E-mail:stevesun521@gmail.com
 * @version 创建时间：2012-6-14 下午2:43:26 类说明
 */
public class RunnableThread implements Runnable {
	public void run() {
		int i = 0;
		while (i <= 10) {
			System.out.println(i);
			i++;
		}
	}

	public static void main(String[] args) {
		new Thread(new RunnableThread()).start();
	}
}