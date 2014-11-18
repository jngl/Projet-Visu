package datas;

import io.Reader;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import utils.Triplet;

public class GazDatas {
	private static final int dateline = 12;
	
	public List<Triplet<Double, Double, Double>> datas;
	
	public GazDatas(String path, Date date) {
		datas = new ArrayList<Triplet<Double,Double,Double>>();
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
					if(index != -1)
						add(locations.coord_lat[index], locations.coord_long[index], Double.parseDouble(file.datas.getData(line, column).replace(',', '.')));
				}
				++line;
			}
		}
	}
	
	private void add(Double latitude, Double longitude, Double value) {
		int i = 0;
		while(i < datas.size() && datas.get(i).x < latitude)
			++i;
		datas.add(i, new Triplet<Double, Double, Double>(latitude, longitude, value));
	}
}
