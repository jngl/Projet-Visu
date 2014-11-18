package main;

import datas.Locations;

public class Main {
	public static void main(String[] args) {
		
	}
	
	private static class Test {
		public static void printLocations() {
			for(int i = 0; i < Locations.getInstance().indicatif.length; ++i) {
				String s = Locations.getInstance().indicatif[i] + "   " + Locations.getInstance().coord_lat[i] + "   " +
						Locations.getInstance().coord_long[i] + "   " + Locations.getInstance().nom[i];
				System.out.println(s);
			}
		}
	}
}
