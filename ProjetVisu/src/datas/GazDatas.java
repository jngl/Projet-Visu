package datas;

import io.Reader;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import exception.MyOutOfBoundException;
import utils.Tuple;
import utils.Utils;

public class GazDatas {
	private static final int dateline = 12;
	private static final int beginDataColumn = 1;
	
	private List<List<Tuple<Point2D.Double, Double>>> datas;
	private List<Tuple<Point2D.Double, Tuple<Double, String>>> stations;
	private List<Date> dates;
	private double minLatitude;
	private double maxLatitude;
	private double minLongitude;
	private double maxLongitude;
	
	public GazDatas(String path) {
		stations = new ArrayList<Tuple<Point2D.Double, Tuple<Double, String>>>();
		minLatitude = Double.MAX_VALUE;
		minLongitude = Double.MAX_VALUE;
		maxLatitude = Double.MIN_VALUE;
		maxLongitude = Double.MIN_VALUE;
		datas = new ArrayList<List<Tuple<Point2D.Double, Double>>>();
		dates = new ArrayList<Date>();
		Reader file = new Reader(path);
		String[] beginDay = file.datas.getData(dateline, beginDataColumn).split("/");
		String[] beginHour = file.datas.getData(dateline + 1, beginDataColumn).split(":");
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(Utils.getDate(Integer.parseInt(beginDay[0]), Integer.parseInt(beginDay[1]), Integer.parseInt(beginDay[2]),
					Integer.parseInt(beginHour[0]), Integer.parseInt(beginHour[1]), Integer.parseInt(beginHour[2])));
		} catch (NumberFormatException | MyOutOfBoundException e) {
			e.printStackTrace();
		}
		int col = beginDataColumn;
		while(col < file.datas.getWidth() && !file.datas.getData(dateline + 1, col).equals("")) {
			List<Tuple<Point2D.Double, Double>> liste = new ArrayList<Tuple<Point2D.Double, Double>>();
			int line = dateline + 2;
			Locations locations = Locations.getInstance();
			while(!file.datas.getData(line, 0).equals("")) {
				if(!file.datas.getData(line, col).equals("-")) {
					int index = locations.getIndex(file.datas.getData(line, 0));
					if(index != -1) {
						liste.add(new Tuple<Point2D.Double, Double>(new Point2D.Double(locations.coord_long[index], locations.coord_lat[index]),
								Double.parseDouble(file.datas.getData(line, col).replace(',', '.'))));
						if(locations.coord_lat[index] > maxLatitude)
							maxLatitude = locations.coord_lat[index];
						if(locations.coord_lat[index] < minLatitude)
							minLatitude = locations.coord_lat[index];
						if(locations.coord_long[index] > maxLongitude)
							maxLongitude = locations.coord_long[index];
						if(locations.coord_long[index] < minLongitude)
							minLongitude = locations.coord_long[index];
						if(col == beginDataColumn)
							stations.add(new Tuple<Point2D.Double, Tuple<Double,String>>(new Point2D.Double(locations.coord_long[index], locations.coord_lat[index]),
									new Tuple<Double, String>(Double.parseDouble(file.datas.getData(line, col).replace(',', '.')), locations.nom[index])));
					}
				}
				++line;
			}
			datas.add(liste);
			dates.add(cal.getTime());
			cal.add(Calendar.HOUR_OF_DAY, 1);
			++col;
		}
	}
	
	public List<Tuple<Point2D.Double, Double>> getDatas(Date date) {
		int i = 0;
		while(i < dates.size() && !Utils.getString(dates.get(i)).equals(Utils.getString(date))) {
			++i;
		}
		if(i < datas.size()) {
			return datas.get(i);
		}
		return null;
	}
	
	public void setUniqueDate(Date date) {
		int i = 0;
		while(i < dates.size() && !Utils.getString(dates.get(i)).equals(Utils.getString(date))) {
			++i;
		}
		if(i < datas.size()) {
			List<Tuple<Point2D.Double, Double>> result = datas.remove(i);
			Date resultDate = dates.remove(i);
			datas.add(0, result);
			dates.add(0, resultDate);
		}
	}
	
	public List<Tuple<Point2D.Double, Double>> getUniqueDateDatas() {
		return datas.get(0);
	}
	
	public Date getBeginDate() {
		return dates.get(0);
	}
	
	public Date getEndDate() {
		return dates.get(dates.size() - 1);
	}
	
	public Date[] getDates() {
		return dates.toArray(new Date[dates.size()]);
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
	
	public List<Tuple<java.awt.geom.Point2D.Double, Tuple<Double, String>>> getStations() {
		return stations;
	}
}
