package window;

import java.util.Date;
import java.util.List;

import datas.GazDatas;
import datas.InterpolatedDatas;

public class LoadingInterpolatedDatas implements Runnable {
	private GazDatas gazDatas;
	private List<Date> dates;
	private int method;
	private int width;
	private int height;
	private Window window;
	
	public LoadingInterpolatedDatas(GazDatas gazDatas, List<Date> dates, int method, int width, int height, Window window) {
		this.gazDatas = gazDatas;
		this.dates = dates;
		this.method = method;
		this.width = width;
		this.height = height;
		this.window = window;
		window.setChargement(0.0);
		Thread t = new Thread(this);
		t.start();
	}

	public void run() {
		for(int i = 0; i < dates.size(); ++i)
			new InterpolatedDatas(gazDatas, dates.get(i), method, 2, width, height, window);
	}
}
