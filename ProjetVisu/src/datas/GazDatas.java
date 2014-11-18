package datas;

import io.Reader;

import java.text.DateFormat;
import java.util.HashMap;

import utils.Tuple;

public class GazDatas {
	private HashMap<Tuple<Double, Double>, Double> datas;
	
	public GazDatas(String path, DateFormat date, int hour) throws exception.MyOutOfBoundException {
		exception.MyOutOfBoundException.test("hour", hour, 0, 23);
		Reader file1 = new Reader(path);
	}
}
