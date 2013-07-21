package edu.bupt.cameras;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.URI;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

public class CameraThread extends Thread {
	private AxisCamera camera;
    private boolean stopflag=false;
	private int i = 0;
    private FileSystem fs =null;
    private OutputStream out=null;
    private Configuration conf=null;
    private FileStatus ft =null;
	public CameraThread(AxisCamera camera, int i) {
		this.camera = camera;
        conf=new Configuration();
		this.setI(i);
	}

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

	public String getData(String filename, String hdfsfile) throws IOException {
		String data = ("camera_id:" + camera.getIP() + ";media_name:"
				+ filename + ";media_type:" + getFileType(filename)
				+ ";media_size:" + getFileSize(hdfsfile) + ";");
		// System.out.println(data);
		return data;
	}


	public long getFileSize(String hdfsfile) throws IOException {
		fs = FileSystem.get(URI.create(hdfsfile), conf);
		ft= fs.getFileStatus(new Path(hdfsfile));
		Long filesize = ft.getLen();
		return filesize;
	}

	public String getFileType(String decodedfile) {
		String[] str;
		str = decodedfile.split("[.]");
		int n = str.length;
		return str[n - 1];
	}


	@Override
	public void run() {
		try {
			while (!stopflag) {
				String hostname=Inet4Address.getLocalHost().getHostName();
				String filename = Util.makeFilename(camera.getIP(),
						this.getI(), "mjpeg");
				String hdfsfile = Util.HDFS_PATH_HEADER +hostname+"/"+ filename;
				BufferedInputStream in = new BufferedInputStream(this.camera.getInputStream());
				if (in != null) {
					System.out.println(hdfsfile + "  transfer  start ");
					fs= FileSystem.get(URI.create(hdfsfile), conf);
					out = fs.create(new Path(hdfsfile));
					IOUtils.copyBytes(in, out, 4096, true);
					System.out.println(hdfsfile + "  transfer  over ");
					out.close();
				}
			}
		} catch (IOException e) {
           if(Thread.currentThread().isInterrupted()){
        	   stopflag=true;  //如果中断设置线程停止
           }else{
        	   throw new RuntimeException();
           }
		}
	}

	@Override
	public void interrupt() {
		try {
			//关闭流引起IO中断
			this.camera.getInputStream().close();
			if(fs!=null) fs.close();
			if (out!=null) out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		super.interrupt();
	}

}
