package utils;

import java.util.List;

public class Strings {
	private String[][] datas;
	private int width;
	private int height;
	
	public Strings(List<String[]> strings) {
		height = strings.size();
		width = 0;
		for(int i = 0; i < height; ++i) {
			if(strings.get(i).length > width)
				width = strings.get(i).length;
		}
		datas = new String[strings.size()][width];
		for(int i = 0; i < height; ++i) {
			for(int j = 0; j < width; ++j) {
				if(j < strings.get(i).length) {
					datas[i][j] = strings.get(i)[j];
				}
				else {
					datas[i][j] = "";
				}
			}
		}
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	public String getData(int i, int j) {
		return datas[i][j];
	}
	
	public Tuple<Integer, Integer> searchString(String s) {
		int line = 0;
		int column = 0;
		Tuple<Integer, Integer> result = null;
		while(result == null && line < height) {
			while(result == null && column < width) {
				if(s.equalsIgnoreCase(getData(line, column)))
					result = new Tuple<Integer, Integer>(line, column);
				++column;
			}
			++line;
		}
		return result;
	}
	
	public int searchInColumn(String s, int column) {
		int line = 0;
		int result = -1;
		while(result == -1 && line < height) {
			if(s.equalsIgnoreCase(getData(line, column)))
				result = line;
			++line;
		}
		return result;
	}

	
	public int searchInLine(String s, int line) {
		int column = 0;
		int result = -1;
		while(result == -1 && column < width) {
			if(s.equalsIgnoreCase(getData(line, column)))
				result = column;
			++column;
		}
		return result;
	}
}
