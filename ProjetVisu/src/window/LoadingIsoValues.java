package window;

import datas.InterpolatedDatas;
import datas.IsoValues;

public class LoadingIsoValues implements Runnable {
	private InterpolatedDatas interpolatedDatas;
	private Window window;
	private double isoValue;
	
	public LoadingIsoValues(InterpolatedDatas interpolatedDatas, double isoValue, Window window) {
		this.interpolatedDatas = interpolatedDatas;
		this.isoValue = isoValue;
		this.window = window;
		window.setChargement(0.0);
		Thread t = new Thread(this);
		t.start();
	}

	public void run() {
		new IsoValues(interpolatedDatas, isoValue, window);
	}
}
