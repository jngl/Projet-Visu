package main;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import datas.GazDatas;
import datas.Locations;
import exception.MyOutOfBoundException;
import utils.*;

public class Main {
	public static void main(String[] args) {
		Test.printGazDatas();
	}
	
	@SuppressWarnings("unused")
	private static class Test {
		public static void printLocations() {
			for(int i = 0; i < Locations.getInstance().indicatif.length; ++i) {
				String s = Locations.getInstance().indicatif[i] + "   " + Locations.getInstance().coord_lat[i] + "   " +
						Locations.getInstance().coord_long[i] + "   " + Locations.getInstance().nom[i];
				System.out.println(s);
			}
		}
		
		public static void printGazDatas() {
			GazDatas gazDatas = null;
			try {
				gazDatas = new GazDatas("particulepm10aout2012.csv", Utils.getDate(8, 1, 2012, 4, 0, 0));
			} catch (MyOutOfBoundException e) {
				e.printStackTrace();
			}
			for(int i = 0; i < gazDatas.datas.size(); ++i) {
				System.out.println(gazDatas.datas.get(i).toString());
			}
		}
	}
}
