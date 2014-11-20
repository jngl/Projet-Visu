package datas;

import io.Reader;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import utils.Tuple;

public class GazDatas {
	private static final int dateline = 12;
	
	private List<Tuple<Point2D.Double, Double>> datas;
	private double minLatitude;
	private double maxLatitude;
	private double minLongitude;
	private double maxLongitude;
	
	public GazDatas(String path, Date date) {
		minLatitude = Double.MAX_VALUE;
		minLongitude = Double.MAX_VALUE;
		maxLatitude = Double.MIN_VALUE;
		maxLongitude = Double.MIN_VALUE;
		datas = new ArrayList<Tuple<Point2D.Double, Double>>();
		Reader file = new Reader(path);
		String day = utils.Utils.getString(date).split("-")[0];
		int hour = Integer.parseInt(utils.Utils.getString(date).split("-")[1].split(":")[0]);
		int column = file.datas.searchInLine(day, dateline) + hour;
		if(column == hour - 1) {
			System.err.println("No such date : " + day + " in file : " + path);
		}
		else {
			int line = dateline + 1;
			Locations locations = Locations.getInstance();
			while(!file.datas.getData(line, 0).equals("")) {
				if(!file.datas.getData(line, column).equals("-")) {
					int index = locations.getIndex(file.datas.getData(line, 0));
					if(index != -1) {
						datas.add(new Tuple<Point2D.Double, Double>(new Point2D.Double(locations.coord_long[index], locations.coord_lat[index]),
								Double.parseDouble(file.datas.getData(line, column).replace(',', '.'))));
						if(locations.coord_lat[index] > maxLatitude)
							maxLatitude = locations.coord_lat[index];
						if(locations.coord_lat[index] < minLatitude)
							minLatitude = locations.coord_lat[index];
						if(locations.coord_long[index] > maxLongitude)
							maxLongitude = locations.coord_long[index];
						if(locations.coord_long[index] < minLongitude)
							minLongitude = locations.coord_long[index];
					}
				}
				++line;
			}
		}
	}
	
	public List<Tuple<Point2D.Double, Double>> getDatas() {
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
}
