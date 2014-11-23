package window;

import java.util.Date;

import datas.GazDatas;
import datas.InterpolatedDatas;

public class LoadingInterpolatedDatas implements Runnable {
	private GazDatas gazDatas;
	private Date date;
	private int method;
	private int width;
	private int height;
	private Window window;
	
	public LoadingInterpolatedDatas(GazDatas gazDatas, Date date, int method, int width, int height, Window window) {
		this.gazDatas = gazDatas;
		this.date = date;
		this.method = method;
		this.width = width;
		this.height = height;
		this.window = window;
		window.setChargement(0.0);
		Thread t = new Thread(this);
		t.start();
	}

	public void run() {
		new InterpolatedDatas(gazDatas, date, method, 1, width, height, window);
	}
}
