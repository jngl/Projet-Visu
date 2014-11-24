package datas;

import java.awt.geom.Point2D;
import java.util.Date;

import window.Window;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

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
	
	private double[] hardyAlpha;
	private double hardyR=0.815;
	
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
			gazDatas.setUniqueDate(this.date);
			
			int N=gazDatas.getUniqueDateDatas().size();
			double[][] matrix = new double[N][N];
			Point2D.Double[] tab = new Point2D.Double[N];
			for(int line = 0; line <  N; ++line){
				tab[line] = gazDatas.getUniqueDateDatas().get(line).x;
				for(int column = 0; column < N; ++column){
					
					Point2D.Double p1 = gazDatas.getUniqueDateDatas().get(line).x;
					Point2D.Double p2 = gazDatas.getUniqueDateDatas().get(column).x;
					
					matrix[line][column]=Math.sqrt(hardyR + Math.pow(p1.distance(p2),2));

				}
			}
			
			double[] vecF = new double[N];
			
			for(int i = 0; i<N; ++i){
				vecF[i]=gazDatas.getUniqueDateDatas().get(i).y;
			}
			
			RealMatrix coefficients = new Array2DRowRealMatrix(matrix, false);
			DecompositionSolver solver = new LUDecomposition(coefficients).getSolver();
			RealVector constants = new ArrayRealVector(vecF, false);
			RealVector solution = solver.solve(constants);
			
			hardyAlpha = solution.toArray();
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
		double result=0;
		Point2D.Double point = getEarthPostions(line, column);
		for(int i = 0; i < gazDatas.getUniqueDateDatas().size(); ++i){
			double hkx=Math.sqrt(hardyR+Math.pow(point.distance(gazDatas.getUniqueDateDatas().get(i).x), 2));
			result+=hardyAlpha[i]*hkx;
		}
		return result;
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
