package edu.bupt.cameras;

import java.io.*;

/**
 * The general camera interface
 * @author codekitten
 * @version 1.0
 * @see AxisCamera
 */
public abstract class Camera {
	
	/**
	 * The hdfs server need this input stream to accept the movie stream
	 * @return the input stream which comes from the camera in order to transfer the data of media
	 */
	abstract InputStream getInputStream();
}
