package io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import utils.Strings;

public class Reader {
	public Strings datas;
	
	public Reader(String path) {
		List<String[]> list = new ArrayList<String[]>();
		try {
			FileReader fr = new FileReader(path);
			BufferedReader br = new BufferedReader(fr);
			String line = "";
			while ((line = br.readLine()) != null) {
				list.add(line.split(";"));
			}
			br.close();
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		datas = new Strings(list);
	}
}
