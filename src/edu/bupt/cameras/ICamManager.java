package edu.bupt.cameras;

import java.util.ArrayList;

/** 
 * @author 孙元成 E-mail:stevesun521@gmail.com 
 * @version 创建时间：2012-6-15 下午2:03:17 
 * 类说明 
 */
public interface ICamManager {
	public void addCamera(ArrayList<String> iplist) ;
	public void deleteCamera(ArrayList<String> iplist);
}