package edu.bupt.zookeeper.factory;

import edu.bupt.cameras.CameraManager;

/** 
 * @author 孙元成 E-mail:stevesun521@gmail.com 
 * @version 创建时间：2012-6-15 下午4:01:44 
 * 类说明 
 */
public class CameraManagerFactory {
  private static CameraManager cameraManager=new CameraManager();
  public static CameraManager getCameraManagerInstance(){
	  return cameraManager;
  }
}
