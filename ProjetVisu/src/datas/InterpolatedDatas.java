package datas;

import java.awt.geom.Point2D;
import java.util.Date;

import window.Window;

public class InterpolatedDatas {
	public static final int ShepardMethod = 1;
	public static final int HardyMethod = 2;
	
	private double[][] datas;
	private Date date;
	private double minLatitude;
	private double maxLatitude;
	private double minLongitude;
	private double maxLongitude;
	private double minValue;
	private double maxValue;
	
	private double ShepardPower;
	private double pixelHeight;
	private double pixelWidth;
	
	public InterpolatedDatas(GazDatas gazDatas, Date date, int method, double ShepardPower, int width, int height, Window window) {
		datas = new double[height][width];
		this.date = date;
		this.ShepardPower = ShepardPower;
		minLatitude = gazDatas.getMinLatitude() - (gazDatas.getMaxLatitude() - gazDatas.getMinLatitude())/10;
		maxLatitude = gazDatas.getMaxLatitude() + (gazDatas.getMaxLatitude() - gazDatas.getMinLatitude())/10;
		minLongitude = gazDatas.getMinLongitude() - (gazDatas.getMaxLongitude() - gazDatas.getMinLongitude())/10;
		maxLongitude = gazDatas.getMaxLongitude() + (gazDatas.getMaxLongitude() - gazDatas.getMinLongitude())/10;
		pixelHeight = (maxLatitude - minLatitude) / (double)(height - 1);
		pixelWidth = (maxLongitude - minLongitude) / (double)(width - 1);
		minValue = Double.MAX_VALUE;
		maxValue = Double.MIN_VALUE;
		
		if(method == HardyMethod){
			
		}
		
		for(int line = 0; line < height; ++line) {
			for(int column = 0; column < width; ++column) {
				if(method == ShepardMethod) {
					datas[line][column] = fillDatasShepardMethod(line, column, gazDatas);
				}
				else if(method ==  HardyMethod) {
					datas[line][column] = fillDatasHardyMethod(line, column, gazDatas);
				}
				if(datas[line][column] < minValue) {
					minValue = datas[line][column];
				}
				if(datas[line][column] > maxValue) {
					maxValue = datas[line][column];
				}
				if(window != null) {
					window.setChargement((double) (line * width + column) / (double) (height * width));
				}
			}
		}
		if(window != null) {
			window.setInterpolatedDatas(this);
		}
	}
	
	private double fillDatasShepardMethod(int line, int column, GazDatas gazDatas) {
		Point2D.Double point = getEarthPostions(line, column);
		double sumValues = 0;
		double sumDist = 0;
		gazDatas.setUniqueDate(date);
		for(int i = 0; i < gazDatas.getUniqueDateDatas().size(); ++i) {
			double dist = 1.0/Math.pow(point.distance(gazDatas.getUniqueDateDatas().get(i).x), ShepardPower);
			sumValues += dist * gazDatas.getUniqueDateDatas().get(i).y;
			sumDist += dist;
		}
		return sumValues / sumDist;
	}
	
	private double fillDatasHardyMethod(int line, int column, GazDatas gazDatas) {
		return 0;
	}
	
	private Point2D.Double getEarthPostions(int line, int column) {
		return new Point2D.Double(minLongitude + column * pixelWidth, maxLatitude - line * pixelHeight);
	}
	
	public double[][] getDatas() {
		return datas;
	}

	public double getMinLatitude() {
		return minLatitude;
	}

	public double getMaxLatitude() {
		return maxLatitude;
	}

	public double getMinLongitude() {
		return minLongitude;
	}

	public double getMaxLongitude() {
		return maxLongitude;
	}

	public double getMaxValue() {
		return maxValue;
	}

	public double getMinValue() {
		return minValue;
	}
}
